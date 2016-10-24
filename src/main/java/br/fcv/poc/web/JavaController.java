package br.fcv.poc.web;

import static akka.pattern.Patterns.ask;
import static br.fcv.poc.core.MyJavaActor.Message.WHAT_TIME_IS_IT;
import static java.lang.Thread.currentThread;
import static java.util.Collections.singletonList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.REQUEST_TIMEOUT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static scala.collection.JavaConversions.asScalaBuffer;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import scala.collection.immutable.List;
import scala.concurrent.Future;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.OnSuccess;
import br.fcv.poc.ActorDesc;
import br.fcv.poc.core.ClockServiceBean.ClockInfo;
import br.fcv.poc.core.ClockServiceBean.TraceItem;
import br.fcv.poc.core.MyJavaActor;
import br.fcv.poc.core.MyScalaActor;
import br.fcv.poc.core.MyScalaActor.WhatTimeIsIt;

@RestController
@RequestMapping(value = "/", params = "controllerType=java")
public class JavaController {

	private static final Logger logger = getLogger(JavaController.class);

	@Inject
	private ActorSystem system;

	private final ActorRef javaActor;
	private final ActorRef scalaActor;

	@Inject
	public JavaController(
			@ActorDesc(MyJavaActor.class) ActorRef javaActor,
			@ActorDesc(MyScalaActor.class) ActorRef scalaActor) {

		logger.debug("JavaController(javaActor: {}, scalaActor: {})", javaActor, scalaActor);
		this.javaActor = javaActor;
		this.scalaActor = scalaActor;
	}

	@RequestMapping()
	public DeferredResult<ResponseEntity<?>> nonblockingIndex(
			@RequestParam(value = "actorType", defaultValue = "scala") String actorType) {

		logger.debug("nonblockingIndex(actorType: {})", actorType);

		final DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

		result.onTimeout(() -> {
			result.setErrorResult(status(REQUEST_TIMEOUT));
		});

		ActorRef targetActor;
		Object message;
		long timeout = 1000L;

		boolean dispatchToJavaActor = "java".equals(actorType);
		if (dispatchToJavaActor) {
			targetActor = javaActor;
			message = WHAT_TIME_IS_IT;
		} else {
			targetActor = scalaActor;
			TraceItem traceItem = new TraceItem(this.getClass(), currentThread());
			List<TraceItem> trace = asScalaBuffer(singletonList(traceItem)).toList();
			message = new WhatTimeIsIt(trace);
		}

		Future<Object> future = ask(targetActor, message, timeout);
		future.onSuccess(new OnSuccess<Object>() {

			@Override
			public void onSuccess(Object obj) throws Throwable {
				logger.debug("nonblockingIndex.onSuccess(obj: {})", obj);
				if (obj instanceof ClockInfo<?>) {
					ClockInfo<?> clockInfo = (ClockInfo<?>) obj;
					clockInfo = clockInfo.appendTraceItem(new TraceItem(JavaController.this.getClass(), currentThread()));
					obj = clockInfo;
				}
				result.setResult(ok(obj));
			}
		}, system.dispatcher());

		return result;
	}
}
