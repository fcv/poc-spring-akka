package br.fcv.poc.web

import scala.concurrent.duration.DurationInt
import scala.util.Failure
import scala.util.Success

import org.springframework.http.HttpStatus.REQUEST_TIMEOUT
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.async.DeferredResult

import com.typesafe.scalalogging.StrictLogging

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import br.fcv.poc.ActorDesc
import br.fcv.poc.core.MyScalaActor
import javax.inject.Inject

@RestController
@RequestMapping(value = Array("/"))
class ScalaController(

			@Inject
			private val actorSystem: ActorSystem,

			@Inject
			@ActorDesc(value = classOf[MyScalaActor], name = "MyScalaActor-01")
			private val scalaActor:ActorRef
		) extends StrictLogging {

	implicit def funToRunnable(fun: () => _) = new Runnable() { def run() = fun() }
	implicit val timeout = Timeout(1 second)

	@RequestMapping
	def open(): DeferredResult[ResponseEntity[_ <: Any]] = {

		logger.debug("open()")

		val result = new DeferredResult[ResponseEntity[_ <: Any]]
		result.onTimeout(() => {
			result setErrorResult status(REQUEST_TIMEOUT)
		})

		implicit val dispatcher = actorSystem.dispatcher

		(scalaActor ? MyScalaActor.WhatTimeIsIt()) onComplete {
			case Success(value) => {
				logger.debug("open.onSuccess(value: {})", value)
				result setResult (ok(value))
			}
			case Failure(ex) => {
				logger.debug("open.onFailure()", ex)
				result setErrorResult (ex)
			}
		}

		result
	}
}
