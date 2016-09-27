package br.fcv.poc.core

import akka.actor.Actor
import org.springframework.beans.factory.config.ConfigurableBeanFactory._
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import javax.inject.Inject

@Component
@Scope(SCOPE_PROTOTYPE)
class MyScalaActor @Inject()(clockService: ClockServiceBean) extends Actor {

	override def receive: Receive = {
		case MyScalaActor.WhatTimeIsIt() => {
			val instant = clockService.getInstant
			sender ! instant
		}
	}
}

object MyScalaActor {

	case class WhatTimeIsIt()

}
