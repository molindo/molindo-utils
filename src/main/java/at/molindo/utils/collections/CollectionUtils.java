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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

public class CollectionUtils {

	private CollectionUtils() {
	}

	public static <E> Set<E> unmodifiableSet(Iterable<E> e) {
		return Collections.unmodifiableSet(set(e));
	}

	public static <E> Set<E> unmodifiableSet(E... e) {
		return Collections.unmodifiableSet(set(e));
	}

	public static <E> HashSet<E> set(E... e) {
		return set(Arrays.asList(e));
	}

	public static <E> HashSet<E> set(Iterable<E> e) {
		return IteratorUtils.addAll(new HashSet<E>(), e.iterator());
	}

	public static <E> SortedSet<E> unmodifiableSortedSet(E... e) {
		return Collections.unmodifiableSortedSet(sortedSet(e));
	}

	public static <E> SortedSet<E> unmodifiableSortedSet(Iterable<E> e) {
		return Collections.unmodifiableSortedSet(sortedSet(e));
	}

	public static <E> TreeSet<E> sortedSet(E... e) {
		return sortedSet(Arrays.asList(e));
	}

	public static <E> TreeSet<E> sortedSet(Iterable<E> e) {
		return IteratorUtils.addAll(new TreeSet<E>(), e.iterator());
	}

	public static <E> List<E> unmodifiableList(E... e) {
		return Collections.unmodifiableList(list(e));
	}

	public static <E> List<E> unmodifiableList(Iterable<E> e) {
		return Collections.unmodifiableList(list(e));
	}

	public static <E> ArrayList<E> list(E... e) {
		return new ArrayList<E>(Arrays.asList(e));
	}

	public static <E> ArrayList<E> list(Iterable<E> e) {
		return IteratorUtils.addAll(new ArrayList<E>(), e.iterator());
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

	public static boolean empty(Collection<?> c) {
		return c == null || c.isEmpty();
	}

	public static boolean empty(Map<?, ?> c) {
		return c == null || c.isEmpty();
	}

	public static <T> T first(Collection<T> c) {
		return c == null ? null : IteratorUtils.next(c.iterator());
	}

	public static <K, V> Map.Entry<K, V> first(Map<K, V> c) {
		return c == null ? null : IteratorUtils.next(c.entrySet().iterator());
	}

	public static <K> K firstKey(Map<K, ?> c) {
		return c == null ? null : IteratorUtils.next(c.keySet().iterator());
	}

	public static <V> V firstValue(Map<?, V> c) {
		return c == null ? null : IteratorUtils.next(c.values().iterator());
	}

	public static <K, V> V putIfAbsent(ConcurrentMap<K, V> map, K key, V value) {
		V current = map.putIfAbsent(key, value);
		return current != null ? current : value;
	}
}