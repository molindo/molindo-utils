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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import at.molindo.utils.data.Function;

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
		return IteratorUtils.addAll(new HashSet<E>(), IteratorUtils.iterator(e));
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
		return IteratorUtils.addAll(new TreeSet<E>(), IteratorUtils.iterator(e));
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
		return IteratorUtils.addAll(new ArrayList<E>(), IteratorUtils.iterator(e));
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
		return c == null ? null : IteratorUtils.next(IteratorUtils.iterator(c));
	}

	public static <K, V> Map.Entry<K, V> first(Map<K, V> c) {
		return c == null ? null : IteratorUtils.next(IteratorUtils.iterator(c.entrySet()));
	}

	public static <K> K firstKey(Map<K, ?> c) {
		return c == null ? null : IteratorUtils.next(IteratorUtils.iterator(c.keySet()));
	}

	public static <V> V firstValue(Map<?, V> c) {
		return c == null ? null : IteratorUtils.next(IteratorUtils.iterator(c.values()));
	}

	public static <K, V> V putIfAbsent(ConcurrentMap<K, V> map, K key, V value) {
		V current = map.putIfAbsent(key, value);
		return current != null ? current : value;
	}

	public static <T, V> T find(Collection<T> c, Function<T, Boolean> f) {
		for (T t : c) {
			if (f.apply(t) == Boolean.TRUE) {
				return t;
			}
		}
		return null;
	}

	public static <T, V> List<T> findAll(Collection<T> c, Function<T, Boolean> f) {
		List<T> list = new LinkedList<T>();
		for (T t : c) {
			if (f.apply(t) == Boolean.TRUE) {
				list.add(t);
			}
		}
		return list;
	}

	public static <T, V> T find(Collection<T> c, Function<T, V> f, V match) {
		for (T t : c) {
			if (match.equals(f.apply(t))) {
				return t;
			}
		}
		return null;
	}

	public static <T, V> List<T> findAll(Collection<T> c, Function<T, V> f, V match) {
		List<T> list = new LinkedList<T>();
		for (T t : c) {
			if (match.equals(f.apply(t))) {
				list.add(t);
			}
		}
		return list;
	}

	public static <C extends Collection<T>, T, F> C add(C to, F o, Function<? super F, T> f) {
		to.add(f.apply(o));
		return to;
	}

	public static <C extends Collection<T>, T, F> C addAll(C to, Collection<? extends F> from, Function<? super F, T> f) {
		for (F o : from) {
			to.add(f.apply(o));
		}
		return to;
	}

	public static <M extends Map<T, F>, T, F> M put(M to, F o, Function<? super F, T> f) {
		to.put(f.apply(o), o);
		return to;
	}

	public static <M extends Map<T, F>, T, F> M putAll(M to, Collection<? extends F> from, Function<? super F, T> f) {
		for (F o : from) {
			to.put(f.apply(o), o);
		}
		return to;
	}

	public static <T> List<T> resize(List<T> list, int size) {
		return resize(list, size, null);
	}

	public static <T> List<T> resize(List<T> list, int size, T defaultValue) {
		int currentSize = list.size();
		while (currentSize > size) {
			list.remove(--currentSize);
		}
		while (currentSize++ < size) {
			list.add(defaultValue);
		}
		return list;
	}

	/**
	 * set index of list to obj, resizing list if necessary
	 * 
	 * @param <T>
	 * @param list
	 * @param idx
	 * @param obj
	 * @return
	 */
	public static <T> List<T> set(List<T> list, int idx, T obj) {
		return set(list, idx, obj, null);
	}

	public static <T> List<T> set(List<T> list, int idx, T obj, T defaultValue) {
		int currentSize = list.size();
		while (currentSize++ <= idx) {
			list.add(defaultValue);
		}
		list.set(idx, obj);
		return list;
	}
}