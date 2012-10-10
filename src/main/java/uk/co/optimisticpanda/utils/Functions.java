package uk.co.optimisticpanda.utils;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CharMatcher.BREAKING_WHITESPACE;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class Functions {

	public static class LowerAndReplaceWhitespaceWithUnderscores implements
			Function<String, String> {
		@Override
		public String apply(String input) {
			String noWhiteSpace = BREAKING_WHITESPACE.removeFrom(input);
			return UPPER_CAMEL.to(LOWER_UNDERSCORE, noWhiteSpace);
		}
	}

	public static class With {
		public static Predicate<Named> name(final String name) {
			return new Predicate<Named>() {
				public boolean apply(Named named) {
					return named.getName().equals(name);
				}
			};
		}
	}

	public static class ToName implements Function<Named, String> {
		@Override
		public String apply(Named input) {
			return input.getName();
		}
	}
	
	public static interface Named {
		String getName();
	}
}
