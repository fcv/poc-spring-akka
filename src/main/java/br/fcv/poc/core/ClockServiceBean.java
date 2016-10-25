package br.fcv.poc.core;

import static java.lang.String.format;

import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class ClockServiceBean {

	public ClockInfo<Instant> getInstant() {
		Thread currrentThread = Thread.currentThread();
		String provider = currrentThread.getName();
		Instant now = Instant.now();
		return new ClockInfo<>(provider, new TraceItem(this), now);
	}

}
