package br.fcv.poc.core;

import static java.util.Objects.requireNonNull;
import static java.lang.System.identityHashCode;
import static java.lang.Thread.currentThread;

/**
 * Created by veronez on 25/10/16.
 */
public class TraceItem {

	private final String className;
	private final String instanceId;
	private final String threadName;

	public TraceItem(Class<?> clss, String instanceId, Thread thread) {
		requireNonNull(clss, "clss cannot be null");
		requireNonNull(instanceId, "instanceId cannot be null");
		requireNonNull(thread, "thread cannot be null");
		this.className = clss.getName();
		this.instanceId = instanceId;
		this.threadName = thread.getName();
	}

	public TraceItem(Object instance, Thread thread) {
		requireNonNull(instance, "instance cannot be null");
		requireNonNull(thread, "thread cannot be null");
		this.className = instance.getClass().getName();
		this.instanceId = String.valueOf(identityHashCode(instance));
		this.threadName = thread.getName();
	}

	public TraceItem(Object instance) {
		requireNonNull(instance, "instance cannot be null");
		this.className = instance.getClass().getName();
		this.instanceId = String.valueOf(identityHashCode(instance));
		this.threadName = currentThread().getName();
	}

	public String getClassName() {
		return className;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public String getThreadName() {
		return threadName;
	}
}
