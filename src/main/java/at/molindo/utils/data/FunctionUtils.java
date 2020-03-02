/**
 * Copyright 2016 Molindo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.molindo.utils.data;

public class FunctionUtils {

	private FunctionUtils() {
	}

	@SuppressWarnings("unchecked")
	public static <T> Function<T, String> toStringFunction() {
		return (Function<T, String>) ToString.INSTANCE;
	}

	public static Function<String, String> toUpperCaseFunction() {
		return ToUpperCase.INSTANCE;
	}

	public static Function<String, String> toLowerCaseFunction() {
		return ToLowerCase.INSTANCE;
	}

	public static Function<String, String> trimFunction() {
		return Trim.INSTANCE;
	}

	public static Function<String, Integer> parseIntFunction() {
		return ParseInt.INSTANCE;
	}

	public static <F, T, I> Function<F, T> chain(final Function<F, I> first, final Function<I, T> second) {
		return new Function<F, T>() {

			@Override
			public T apply(F input) {
				return second.apply(first.apply(input));
			}

		};
	}

	public static <F, T> Function<F, T> value(final T value) {
		return new Function<F, T>() {

			@Override
			public T apply(F input) {
				return value;
			}

		};
	}

	// enum singleton
	public enum ToString implements Function<Object, String> {
		INSTANCE;

		@Override
		public String apply(Object input) {
			return StringUtils.string(input);
		}

	}

	public enum ToLowerCase implements Function<String, String> {
		INSTANCE;

		@Override
		public String apply(String input) {
			return StringUtils.lower(input);
		}

	}

	public enum ToUpperCase implements Function<String, String> {
		INSTANCE;

		@Override
		public String apply(String input) {
			return StringUtils.upper(input);
		}

	}

	public enum Trim implements Function<String, String> {
		INSTANCE;

		@Override
		public String apply(String input) {
			return StringUtils.trim(input);
		}

	}

	public enum ParseInt implements Function<String, Integer> {
		INSTANCE;

		@Override
		public Integer apply(String input) {
			return Integer.parseInt(input);
		}

	}

}
