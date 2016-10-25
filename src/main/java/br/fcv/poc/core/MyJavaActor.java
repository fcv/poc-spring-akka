package br.fcv.poc.core;

import static akka.japi.pf.ReceiveBuilder.match;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.time.Instant;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;

import com.google.common.collect.ImmutableList;

@Component
@Scope(SCOPE_PROTOTYPE)
public class MyJavaActor extends AbstractActor {

	private static final Logger logger = getLogger(MyJavaActor.class);

	private final ClockServiceBean clockService;

	@Inject
	public MyJavaActor(ClockServiceBean clockService) {
		this.clockService = requireNonNull(clockService, "clockService cannot be null");

		receive(match(WhatTimeIsIt.class, m -> {

			logger.debug("receive(GREET, {})", m);
			ClockInfo<Instant> instant = this.clockService.getInstant();
			instant = instant.prependTraceItem(new TraceItem(this));
			instant = instant.prependTraceItems(m.trace);
			sender().tell(instant, self());
		}).build());
	}

	public static class WhatTimeIsIt {

		private final List<TraceItem> trace;

		public WhatTimeIsIt() {
			this(ImmutableList.of());
		}

		public WhatTimeIsIt(List<TraceItem> trace) {
			this.trace = ImmutableList.copyOf(trace);
		}

		public static WhatTimeIsIt whatTimeIsIt() {
			return new WhatTimeIsIt();
		}

		public static WhatTimeIsIt whatTimeIsIt(List<TraceItem> trace) {
			return new WhatTimeIsIt(trace);
		}
	}
}
