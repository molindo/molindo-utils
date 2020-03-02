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

import java.util.Comparator;

public class ComparatorUtils {

	/**
	 * a comparator that compares {@link Comparable} objects using
	 * {@link #nullLowCompareTo(Comparable, Object)}
	 * 
	 * @throws ClassCastException
	 *             if the first argument isn't {@link Comparable}
	 * @throws ClassCastException
	 *             if the first argument's type prevents it from being compared
	 *             to the second argument object.
	 */
	public static final Comparator<?> NULL_LOW_COMPARABLE = new Comparator<Object>() {

		@SuppressWarnings("unchecked")
		@Override
		public int compare(Object o1, Object o2) {
			return ComparatorUtils.nullLowCompareTo((Comparable<Object>) o1, o2);
		}
	};

	/**
	 * a comparator that compares {@link Comparable} objects using
	 * {@link #nullHighCompareTo(Comparable, Object)}
	 * 
	 * @throws ClassCastException
	 *             if the first argument isn't {@link Comparable}
	 * @throws ClassCastException
	 *             if the first argument's type prevents it from being compared
	 *             to the second argument object.
	 */
	public static final Comparator<?> NULL_HIGH_COMPARABLE = new Comparator<Object>() {

		@SuppressWarnings("unchecked")
		@Override
		public int compare(Object o1, Object o2) {
			return ComparatorUtils.nullHighCompareTo((Comparable<Object>) o1, o2);
		}
	};

	/**
	 * @see #NULL_LOW_COMPARABLE
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Comparator<T> nullLowComparable() {
		return (Comparator<T>) NULL_LOW_COMPARABLE;
	}

	/**
	 * @see #NULL_HIGH_COMPARABLE
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Comparator<T> nullHighComparable() {
		return (Comparator<T>) NULL_HIGH_COMPARABLE;
	}

	/**
	 * null-safe invocation of {@link Comparable#compareTo(Object)} placing
	 * <code>null</code> at the beginning of a sorted list (assuming each
	 * <code>null</code> is equal)
	 * 
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 *         Assuming null is less than any object and equal to itself.
	 * 
	 * @see Comparable#compareTo(Object)
	 */
	public static <T> int nullLowCompareTo(Comparable<T> o1, T o2) {
		if (o1 == null) {
			return o2 == null ? 0 : -1;
		} else {
			return o2 == null ? 1 : o1.compareTo(o2);
		}
	}

	/**
	 * null-safe invocation of {@link Comparable#compareTo(Object)} placing
	 * <code>null</code> at the end of a sorted list (assuming each
	 * <code>null</code> is equal)
	 * 
	 * @return a negative integer, zero, or a positive integer as the first
	 *         argument is less than, equal to, or greater than the second.
	 *         Assuming null is greater than any object and equal to itself.
	 * 
	 * @see Comparable#compareTo(Object)
	 */
	public static <T> int nullHighCompareTo(Comparable<T> o1, T o2) {
		if (o1 == null) {
			return o2 == null ? 0 : 1;
		} else {
			return o2 == null ? -1 : o1.compareTo(o2);
		}
	}
}
