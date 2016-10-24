package br.fcv.poc.web;

import static java.lang.Thread.currentThread;
import br.fcv.poc.core.ClockServiceBean.ClockInfo;
import br.fcv.poc.core.ClockServiceBean.TraceItem;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Used only to add a new {@link TraceItem} regarding serialization process and
 * which Thread actually handled it.
 * 
 * @author veronez
 *
 */
public class SerializationConverter extends StdConverter<ClockInfo<?>, ClockInfo<?>> {

	@Override
	public ClockInfo<?> convert(ClockInfo<?> value) {
		return value.appendTraceItem(new TraceItem(this.getClass(), currentThread()));
	}

}
