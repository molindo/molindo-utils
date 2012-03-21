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

package at.molindo.utils.collections;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayUtils {
	public static boolean equals(byte[] a, byte[] a2, int off, int len) {
		if (off < 0 || len < 0 || len > length(a) - off || len > length(a2) - off) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return true;
		}

		if (a == a2) {
			return true;
		}
		if (a == null || a2 == null) {
			return false;
		}

		for (int i = off; i < off + len; i++) {
			if (a[i] != a2[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return <code>true</code> if a is not null and a.length > 0
	 */
	public static <T> boolean empty(T[] a) {
		return a == null || a.length == 0;
	}

	/**
	 * @return <code>true</code> if a is not null and a.length > 0
	 */
	public static boolean empty(byte[] a) {
		return a == null || a.length == 0;
	}

	/**
	 * @return <code>true</code> if a is not null and a.length > 0
	 */
	public static boolean empty(int[] a) {
		return a == null || a.length == 0;
	}

	/**
	 * @return <code>true</code> if a is not null and a.length > 0
	 */
	public static boolean empty(long[] a) {
		return a == null || a.length == 0;
	}

	/**
	 * @return <code>true</code> if a is not null and a.length > 0
	 */
	public static boolean empty(float[] a) {
		return a == null || a.length == 0;
	}

	/**
	 * @return <code>true</code> if a is not null and a.length > 0
	 */
	public static boolean empty(double[] a) {
		return a == null || a.length == 0;
	}

	/**
	 * @return <code>true</code> if a is not null and a.length > 0
	 */
	public static boolean empty(char[] a) {
		return a == null || a.length == 0;
	}

	/**
	 * @return a[0] if array isn't empty
	 * @see #empty(Object[])
	 */
	public static <T> T first(T[] a) {
		return empty(a) ? null : a[0];
	}

	/**
	 * @return a[0] if array isn't empty
	 * @see #empty(Object[])
	 */
	public static byte first(byte[] a) {
		return empty(a) ? 0x00 : a[0];
	}

	/**
	 * @return a[0] if array isn't empty
	 * @see #empty(Object[])
	 */
	public static int first(int[] a) {
		return empty(a) ? 0 : a[0];
	}

	/**
	 * @return a[0] if array isn't empty
	 * @see #empty(Object[])
	 */
	public static long first(long[] a) {
		return empty(a) ? 0L : a[0];
	}

	/**
	 * @return a[0] if array isn't empty
	 * @see #empty(Object[])
	 */
	public static float first(float[] a) {
		return empty(a) ? 0.0F : a[0];
	}

	/**
	 * @return a[0] if array isn't empty
	 * @see #empty(Object[])
	 */
	public static double first(double[] a) {
		return empty(a) ? 0.0 : a[0];
	}

	/**
	 * @return a[0] if array isn't empty
	 * @see #empty(Object[])
	 */
	public static char first(char[] a) {
		return empty(a) ? 0x00 : a[0];
	}

	/**
	 * @return null-safe length of array
	 */
	public static <T> int length(T[] a) {
		return a == null ? 0 : a.length;
	}

	/**
	 * @return null-safe length of array
	 */
	public static int length(byte[] a) {
		return a == null ? 0 : a.length;
	}

	/**
	 * @return null-safe length of array
	 */
	public static int length(int[] a) {
		return a == null ? 0 : a.length;
	}

	/**
	 * @return null-safe length of array
	 */
	public static int length(long[] a) {
		return a == null ? 0 : a.length;
	}

	/**
	 * @return null-safe length of array
	 */
	public static int length(float[] a) {
		return a == null ? 0 : a.length;
	}

	/**
	 * @return null-safe length of array
	 */
	public static int length(double[] a) {
		return a == null ? 0 : a.length;
	}

	/**
	 * @return null-safe length of array
	 */
	public static int length(char[] a) {
		return a == null ? 0 : a.length;
	}

	public static Iterable<Object> toIterable(final Object array) {
		if (array == null) {
			return IteratorUtils.emptyIterable();
		}

		if (!array.getClass().isArray()) {
			throw new IllegalArgumentException("object not of type array, was " + array.getClass().getName());
		}

		if (Array.getLength(array) == 0) {
			return IteratorUtils.emptyIterable();
		}

		return new Iterable<Object>() {

			@Override
			public Iterator<Object> iterator() {
				return new Iterator<Object>() {

					private int _i = 0;
					private final int _length = Array.getLength(array);

					@Override
					public boolean hasNext() {
						return _i < _length;
					}

					@Override
					public Object next() {
						return Array.get(array, _i++);
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	/**
	 * appends an object to an array
	 * 
	 * @param <T>
	 * @param a
	 * @param o
	 * @return
	 */
	public static <T> T[] append(T[] a, T o) {
		@SuppressWarnings("unchecked")
		T[] copy = (T[]) Array.newInstance(a.getClass().getComponentType(), a.length + 1);
		System.arraycopy(a, 0, copy, 0, a.length);
		copy[a.length] = o;
		return copy;
	}
}
