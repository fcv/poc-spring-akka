package br.fcv;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class BarTest {

	private Bar bar;

	@Before
	public void initBar() {
		bar = new Bar();
	}

	@Test
	public void testSum() {

		assertThat(bar.sum(1, 3), is(4));
		assertThat(bar.sum(2, 1), is(3));
	}

	@Test
	public void testDiff() {

		assertThat(bar.diff(8, 4), is(4));
		assertThat(bar.diff(4, 8), is(-4));
	}

}
