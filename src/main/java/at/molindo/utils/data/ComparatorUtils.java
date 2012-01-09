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

import java.util.Comparator;

public class ComparatorUtils {

	/**
	 * a comparator that compares {@link Comparable} objects using
	 * {@link #compareTo(Comparable, Object)}
	 * 
	 * @throws ClassCastException
	 *             if the first argument isn't {@link Comparable}
	 * @throws ClassCastException
	 *             if the first argument's type prevents it from being compared
	 *             to the second argument object.
	 */
	public static final Comparator<?> COMPARABLE = new Comparator<Object>() {

		@SuppressWarnings("unchecked")
		@Override
		public int compare(Object o1, Object o2) {
			return ComparatorUtils.compareTo((Comparable<Object>) o1, o2);
		}
	};

	/**
	 * @see #COMPARABLE
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Comparator<T> comparable() {
		return (Comparator<T>) COMPARABLE;
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
	public static <T> int compareTo(Comparable<T> o1, T o2) {
		if (o1 == null) {
			return o2 == null ? 0 : 1;
		} else {
			return o2 == null ? -1 : o1.compareTo(o2);
		}
	}
}
