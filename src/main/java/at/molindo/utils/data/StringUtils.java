/**
 * Copyright 2010 Molindo GmbH
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import at.molindo.utils.collections.CollectionUtils;

public class StringUtils {
	private StringUtils() {

	}

	public static boolean empty(String string) {
		return string == null || string.isEmpty();
	}

	public static String trim(String string) {
		return string == null ? null : string.trim();
	}

	public static String sub(String s, int length) {
		return sub(s, "", length);
	}

	public static String sub(String s, String suffix, int maxLength) {
		if (suffix == null) {
			suffix = "";
		}
		if (maxLength < suffix.length()) {
			throw new IllegalArgumentException("suffix ('" + suffix + "') must not be longer than maxLength ("
					+ maxLength + ")");
		}

		if (s == null || s.length() <= maxLength) {
			return s;
		}

		return s.substring(0, maxLength - suffix.length()) + suffix;
	}

	public static String afterFirst(final String string, final String delim) {
		if (string == null) {
			return null;
		}
		final int index = string.indexOf(delim);

		if (index == -1) {
			return "";
		}

		return string.substring(index + delim.length());
	}

	public static String afterLast(final String string, final String delim) {
		if (string == null) {
			return null;
		}
		final int index = string.lastIndexOf(delim);

		if (index == -1) {
			return "";
		}

		return string.substring(index + delim.length());
	}

	public static String beforeFirst(final String string, final String delim) {
		if (string == null) {
			return null;
		}
		final int index = string.indexOf(delim);

		if (index == -1) {
			return "";
		}

		return string.substring(0, index);
	}

	public static String beforeLast(final String string, final String delim) {
		if (string == null) {
			return null;
		}
		final int index = string.lastIndexOf(delim);

		if (index == -1) {
			return "";
		}

		return string.substring(0, index);
	}

	public static String startWith(String string, String prefix) {
		if (empty(string)) {
			return prefix;
		} else if (string.startsWith(prefix)) {
			return string;
		} else {
			return prefix + string;
		}
	}

	public static String endWith(String string, String suffix) {
		if (empty(string)) {
			return suffix;
		} else if (string.endsWith(suffix)) {
			return string;
		} else {
			return string + suffix;
		}
	}

	public static String trailing(String string, String suffix) {
		if (string == null) {
			return null;
		}
		return suffix == null || string.endsWith(suffix) ? string : string + suffix;
	}

	public static String leading(String string, String prefix) {
		if (string == null) {
			return null;
		}
		return prefix == null || string.startsWith(prefix) ? string : prefix + string;
	}

	public static String stripTrailing(String string, String suffix) {
		if (string == null) {
			return null;
		}

		if (suffix != null && string.endsWith(suffix)) {
			return string.substring(0, string.length() - suffix.length());
		} else {
			return string;
		}
	}

	public static String stripLeading(String string, String prefix) {
		if (string == null) {
			return null;
		}

		if (prefix != null && string.startsWith(prefix)) {
			return string.substring(prefix.length());
		} else {
			return string;
		}
	}

	/**
	 * 
	 * @param string
	 * @param delim
	 * @param a
	 * @return number of string written to a
	 * @throws IllegalArgumentException
	 *             if a is of length 0
	 * @throws ArrayIndexOutOfBoundsException
	 *             if a is too small to fit all strings
	 * @see #split(String, String)
	 */
	public static int split(String string, String delim, String[] a) {
		if (a.length == 0) {
			throw new IllegalArgumentException();
		}

		int idx = 0;
		for (String s : split(string, delim)) {
			a[idx++] = s;
		}
		return idx;
	}

	/**
	 * splits a string into a pair at the first occurence of delim
	 * 
	 * @param string
	 * @param delim
	 * @return
	 */
	public static Pair<String, String> pair(String string, String delim) {
		if (string == null) {
			return Pair.pair("", "");
		}

		int idx = string.indexOf(delim);
		if (idx < 0) {
			return Pair.pair(string, "");
		} else {
			return Pair.pair(string.substring(0, idx), string.substring(idx + 1));
		}
	}

	/**
	 * null-safe equals
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equals(String s1, String s2) {
		return ObjectUtils.equals(s1, s2);
	}

	public static String upperFirst(String s) {
		return upperFirst(s, Locale.getDefault());
	}

	public static String upperFirst(String s, Locale locale) {
		if (s == null || s.length() < 1) {
			return s;
		} else {
			return s.substring(0, 1).toUpperCase(locale) + s.substring(1);
		}
	}

	public static String lowerFirst(String s) {
		return lowerFirst(s, Locale.getDefault());
	}

	public static String lowerFirst(String s, Locale locale) {
		if (s == null || s.length() < 1) {
			return s;
		} else {
			return s.substring(0, 1).toLowerCase(locale) + s.substring(1);
		}
	}

	public static Iterable<String> split(final String string, final String split) {
		return split(string, split, Integer.MAX_VALUE);
	}

	public static Iterable<String> split(final String string, final String split, final int max) {
		if (string == null) {
			throw new NullPointerException("string");
		}
		if (split == null) {
			throw new NullPointerException("split");
		}
		if (split.isEmpty()) {
			throw new IllegalArgumentException("split must not be empty");
		}
		if (max <= 0) {
			throw new IllegalArgumentException("max must be >= 1, was " + max);
		}

		return new Iterable<String>() {

			@Override
			public Iterator<String> iterator() {
				return new Iterator<String>() {

					private int _count = 0;
					private int _pos = 0;

					@Override
					public boolean hasNext() {
						return _pos >= 0;
					}

					@Override
					public String next() {
						if (!hasNext()) {
							throw new NoSuchElementException();
						}

						int next = ++_count == max ? -1 : string.indexOf(split, _pos);

						String str;

						if (next > 0) {
							str = string.substring(_pos, next);
							_pos = next + split.length();
						} else {
							str = string.substring(_pos);
							_pos = -1;
						}

						return str;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}

				};
			}
		};
	}

	public static String join(String separator, Object... fragments) {
		return join(separator, Arrays.asList(fragments));
	}

	public static String join(String separator, Collection<?> fragments) {
		if (CollectionUtils.empty(fragments)) {
			return "";
		} else if (fragments.size() == 1) {
			return string(CollectionUtils.first(fragments));
		} else {
			if (separator == null) {
				separator = "";
			}

			StringBuffer buf = new StringBuffer(128);
			for (Object fragment : fragments) {
				String frag = string(fragment);
				if (!empty(frag)) {
					buf.append(frag).append(separator);
				}
			}
			buf.setLength(buf.length() - separator.length());
			return buf.toString();
		}
	}

	public static String string(Object o) {
		return o == null ? null : o.toString();
	}
}
