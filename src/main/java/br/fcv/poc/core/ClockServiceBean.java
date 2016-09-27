package br.fcv.poc.core;

import static java.lang.String.format;

import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class ClockServiceBean {

	public ClockInfo<Instant> getInstant() {
		String provider = Thread.currentThread().getName();
		Instant now = Instant.now();
		return new ClockInfo<>(provider, now);
	}

	public static class ClockInfo<T> {

		private final String provider;
		private final T value;

		public ClockInfo(String provider, T info) {
			this.provider = provider;
			this.value = info;
		}

		public String getProvider() {
			return provider;
		}

		public T getInfo() {
			return value;
		}

		@Override
		public String toString() {
			return format("{provided: %s, value: %s}", provider, value);
		}
	}
}
