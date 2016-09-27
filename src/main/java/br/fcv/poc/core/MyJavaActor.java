package br.fcv.poc.core;

import static akka.japi.pf.ReceiveBuilder.matchEquals;
import static br.fcv.poc.core.MyJavaActor.Message.WHAT_TIME_IS_IT;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.time.Instant;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import br.fcv.poc.core.ClockServiceBean.ClockInfo;

@Component
@Scope(SCOPE_PROTOTYPE)
public class MyJavaActor extends AbstractActor {

	private static final Logger logger = getLogger(MyJavaActor.class);

	public static enum Message {
		WHAT_TIME_IS_IT
	}

	private final ClockServiceBean clockService;

	@Inject
	public MyJavaActor(ClockServiceBean clockService) {
		this.clockService = requireNonNull(clockService, "clockService cannot be null");

		receive(matchEquals(WHAT_TIME_IS_IT, m -> {

			logger.debug("receive(GREET, {})", m);
			ClockInfo<Instant> instant = this.clockService.getInstant();
			sender().tell(instant, self());
		}).build());
	}
}
