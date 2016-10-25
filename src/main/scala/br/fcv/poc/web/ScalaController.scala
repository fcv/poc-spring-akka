package br.fcv.poc.web

import javax.inject.Inject

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import br.fcv.poc.ActorDesc
import br.fcv.poc.core.{ClockInfo, MyJavaActor, MyScalaActor, TraceItem}
import com.typesafe.scalalogging.StrictLogging
import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.{ok, status}
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam, RestController}
import org.springframework.web.context.request.async.DeferredResult
import org.springframework.web.context.request.async.DeferredResult.DeferredResultHandler

import scala.collection.JavaConverters._
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

@RestController
@RequestMapping(value = Array("/api/rest/v1/instant"))
class ScalaController(

			@Inject
			private val actorSystem: ActorSystem,

			@Inject
			@ActorDesc(value = classOf[MyScalaActor], name = "MyScalaActor-01")
			private val scalaActor:ActorRef,

			@Inject
			@ActorDesc(value = classOf[MyJavaActor], name = "MyJavaActor-01")
			private val javaActor:ActorRef
		) extends StrictLogging {

	implicit def funToRunnable(fun: () => _) = new Runnable() { def run() = fun() }
	implicit val timeout = Timeout(1 second)

	@RequestMapping
	def getInstant(@RequestParam(value = "actorType", defaultValue = "scala") actorType: String): DeferredResult[ResponseEntity[_ <: Any]] = {

		logger.debug("getInstant()")

		val result = new DeferredResult[ResponseEntity[_ <: Any]]
		result.onTimeout(() => {
			result setErrorResult status(REQUEST_TIMEOUT)
		})

		result onCompletion (() => {
			logger.debug("getInstant.onCompletion()")
		})

		result setResultHandler new DeferredResultHandler {
			override def handleResult(result: scala.Any): Unit = {
				logger.debug("getInstant.handleResult(result: {})", result)
			}
		}

		implicit val dispatcher = actorSystem.dispatcher
		val trace = List(TraceItem(this))
		val (actor, msg) = if ("scala".equalsIgnoreCase(actorType)) {
			(scalaActor, MyScalaActor.WhatTimeIsIt(trace))
		} else {
			(javaActor, MyJavaActor.WhatTimeIsIt.whatTimeIsIt(trace.asJava))
		}

		(actor ? msg) onComplete {
			case Success(value: ClockInfo[_]) => {
				logger.debug("getInstant.onSuccess(value: {})", value)
				result setResult (ok(value.appendTraceItem(TraceItem(this))))
			}
			case Failure(ex) => {
				logger.debug("getInstant.onFailure()", ex)
				result setErrorResult (ex)
			}
		}

		result
	}
}

object TraceItem {
	def apply(clss: Class[_], thread: Thread) = new TraceItem(clss, thread)
	def apply(instance: AnyRef) = new TraceItem(instance)
}
