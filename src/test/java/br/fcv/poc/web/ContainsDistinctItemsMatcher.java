package br.fcv.poc.web;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ContainsDistinctItemsMatcher extends TypeSafeDiagnosingMatcher<Collection<?>> {

	@Override
	protected boolean matchesSafely(Collection<?> items, Description mismatchDescription) {
		Set<?> set = new HashSet<>(items);
		int numberOfDistinctItems = set.size();
		boolean containsDistinctItems = numberOfDistinctItems > 1;
		mismatchDescription
				.appendValueList("[", ",", "]", items)
				.appendText(" contains " + numberOfDistinctItems + " distinct items: ")
				.appendValueList("[", ",", "]", set);
		return containsDistinctItems;
	}

	@Override
	public void describeTo(Description description) {
		description
				.appendText("a collection containing distincting items ");
	}

	public static ContainsDistinctItemsMatcher containsDistinctItems() {
		return new ContainsDistinctItemsMatcher();
	}
}
