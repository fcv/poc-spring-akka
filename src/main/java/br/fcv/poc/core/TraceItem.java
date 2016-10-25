package br.fcv.poc.core;

/**
 * Created by veronez on 25/10/16.
 */
public class TraceItem {

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
