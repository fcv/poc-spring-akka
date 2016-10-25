package br.fcv.poc.core;

import br.fcv.poc.web.SerializationConverter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;

import java.util.List;

import static java.lang.String.format;

/**
 * Created by veronez on 25/10/16.
 */
@JsonSerialize(converter = SerializationConverter.class)
public class ClockInfo<T> {

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
		List<TraceItem> newTrace = ImmutableList.<TraceItem>builder().add(item).addAll(trace).build();
		return new ClockInfo<T>(provider, newTrace, value);
	}

	public ClockInfo<T> prependTraceItems(List<TraceItem> items) {
		List<TraceItem> newTrace = ImmutableList.<TraceItem>builder().addAll(items).addAll(trace).build();
		return new ClockInfo<T>(provider, newTrace, value);
	}

	public ClockInfo<T> appendTraceItem(TraceItem item) {
		List<TraceItem> newTrace = ImmutableList.<TraceItem>builder().addAll(trace).add(item).build();
		return new ClockInfo<T>(provider, newTrace, value);
	}

	public ClockInfo<T> appendTraceItems(List<TraceItem> items) {
		List<TraceItem> newTrace = ImmutableList.<TraceItem>builder().addAll(trace).addAll(items).build();
		return new ClockInfo<T>(provider, newTrace, value);
	}

	@Override
	public String toString() {
		return format("{provider: %s, value: %s}", provider, value);
	}
}
