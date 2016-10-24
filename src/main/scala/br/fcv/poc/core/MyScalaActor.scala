package br.fcv.poc.core

import scala.collection.JavaConverters._

import akka.actor.Actor
import org.springframework.beans.factory.config.ConfigurableBeanFactory._
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import javax.inject.Inject

import br.fcv.poc.core.ClockServiceBean.TraceItem

@Component
@Scope(SCOPE_PROTOTYPE)
class MyScalaActor @Inject()(clockService: ClockServiceBean) extends Actor {

	override def receive: Receive = {
		case MyScalaActor.WhatTimeIsIt(trace) => {
			val instant = clockService.getInstant
			sender ! instant.prependTraceItems(trace.asJava)
		}
	}
}

object MyScalaActor {

	case class WhatTimeIsIt(trace: List[TraceItem] = List())

}
