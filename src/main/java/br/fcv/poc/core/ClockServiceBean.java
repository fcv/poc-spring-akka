package br.fcv.poc.core;

import static java.lang.String.format;

import java.time.Instant;
import java.util.List;

import br.fcv.poc.web.SerializationConverter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;

@Component
public class ClockServiceBean {

	public ClockInfo<Instant> getInstant() {
		Thread currrentThread = Thread.currentThread();
		String provider = currrentThread.getName();
		Instant now = Instant.now();
		return new ClockInfo<>(provider, new TraceItem(this.getClass(), currrentThread), now);
	}

	@JsonSerialize(converter = SerializationConverter.class)
	public static class ClockInfo<T> {

		private final String provider;
		private final List<TraceItem> trace;
		private final T value;

		public ClockInfo(String provider, T info) {
			this.provider = provider;
			this.value = info;
			this.trace = ImmutableList.of();
		}

		public ClockInfo(String provider, TraceItem traceItem, T info) {
			this.provider = provider;
			this.trace = ImmutableList.of(traceItem);
			this.value = info;
		}

		private ClockInfo(String provider, List<TraceItem> traceItems, T info) {
			this.provider = provider;
			this.trace = traceItems;
			this.value = info;
		}

		public String getProvider() {
			return provider;
		}

		public T getInfo() {
			return value;
		}

		public List<TraceItem> getTrace() {
			return trace;
		}

		public ClockInfo<T> prependTraceItem(TraceItem item) {
			List<TraceItem> newTrace = ImmutableList.<TraceItem> builder().add(item).addAll(trace).build();
			return new ClockInfo<T>(provider, newTrace, value);
		}

		public ClockInfo<T> prependTraceItems(List<TraceItem> items) {
			List<TraceItem> newTrace = ImmutableList.<TraceItem> builder().addAll(items).addAll(trace).build();
			return new ClockInfo<T>(provider, newTrace, value);
		}

		public ClockInfo<T> appendTraceItem(TraceItem item) {
			List<TraceItem> newTrace = ImmutableList.<TraceItem> builder().addAll(trace).add(item).build();
			return new ClockInfo<T>(provider, newTrace, value);
		}

		public ClockInfo<T> appendTraceItems(List<TraceItem> items) {
			List<TraceItem> newTrace = ImmutableList.<TraceItem> builder().addAll(trace).addAll(items).build();
			return new ClockInfo<T>(provider, newTrace, value);
		}

		@Override
		public String toString() {
			return format("{provider: %s, value: %s}", provider, value);
		}
	}

	public static class TraceItem {

		public final String className;
		public final String threadName;

		public TraceItem(Class<?> clss, Thread thread) {
			this.className = clss.getName();
			this.threadName = thread.getName();
		}

		public String getClassName() {
			return className;
		}

		public String getThreadName() {
			return threadName;
		}
	}
}
