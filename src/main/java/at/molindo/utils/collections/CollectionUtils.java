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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class CollectionUtils {

	private CollectionUtils() {
	}

	public static <E> HashSet<E> set(E... e) {
		return new HashSet<E>(Arrays.asList(e));
	}

	public static <E> TreeSet<E> sortedSet(E... e) {
		return new TreeSet<E>(Arrays.asList(e));
	}

	public static <E> ArrayList<E> list(E... e) {
		return new ArrayList<E>(Arrays.asList(e));
	}

	/**
	 * a sublist implementation that is diffrent from
	 * {@link List#subList(int, int)} as it handles out of bounds indexes
	 * gracefully
	 */
	public static <T> List<T> subList(final List<T> list, final int first, final int count) {
		if (list.size() < first) {
			return new ArrayList<T>(0);
		}
		return list.subList(first, Math.min(first + count, list.size()));
	}
}