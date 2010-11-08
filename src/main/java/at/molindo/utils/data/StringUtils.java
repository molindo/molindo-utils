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

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StringUtils {
	private StringUtils() {

	}

	public static boolean empty(String string) {
		return string == null || string.isEmpty();
	}

	public static String trim(String string) {
		return string == null ? null : string.trim();
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
	
	/**
	 * 
	 * @param string
	 * @param delim
	 * @param a 
	 * @return number of string written to a
	 * @throws IllegalArgumentException if a is of length 0
	 * @throws ArrayIndexOutOfBoundsException if a is too small to fit all strings
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
	 * null-safe equals
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equals(String s1, String s2) {
		if (s1 == null) {
			return s2 == null;
		} else {
			return s1.equals(s2);
		}
	}
	
	public static Iterable<String> split(final String string, final String split) {
		if (string == null) {
			throw new NullPointerException("string");
		}
		if (split == null) {
			throw new NullPointerException("split");
		}
		if (split.isEmpty()) {
			throw new IllegalArgumentException("split must not be empty");
		}

		return new Iterable<String>() {

			@Override
			public Iterator<String> iterator() {
				return new Iterator<String>() {

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

						int next = string.indexOf(split, _pos);

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
}
