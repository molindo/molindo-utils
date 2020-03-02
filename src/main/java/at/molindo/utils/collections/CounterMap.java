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
package at.molindo.utils.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.omg.CORBA.UNKNOWN;

import at.molindo.utils.data.Pair;

public class CounterMap<T> implements Iterable<Pair<T, Integer>>, Serializable {

	public static final int UNKNOWN = -1;
	private final Map<T, Counter> _counters = new LinkedHashMap<T, Counter>();

	public CounterMap() {

	}

	public CounterMap(final Collection<T> objects) {

	}

	public CounterMap(final Collection<T> objects, final int start) {
		for (final T o : objects) {
			_counters.put(o, new Counter(start));
		}
	}

	public int getCount(final T obj) {
		final Counter inc = _counters.get(obj);
		return inc != null ? inc.get() : UNKNOWN;
	}

	public Pair<T, Integer> get(T obj) {
		return new Pair<T, Integer>(obj, getCount(obj));
	}

	public int increment(final T obj) {
		final Counter c = _counters.get(obj);
		if (c == null) {
			_counters.put(obj, new Counter(1));
			return 1;
		} else {
			return c.increment();
		}
	}

	public boolean contains(final T obj) {
		return _counters.get(obj) != null;
	}

	public boolean add(final T obj, final int start) {
		if (!_counters.containsKey(obj)) {
			_counters.put(obj, new Counter(start));
			return true;
		} else {
			return false;
		}
	}

	public int remove(final T obj) {
		final Counter inc = _counters.remove(obj);
		return inc == null ? UNKNOWN : inc.get();
	}

	public int size() {
		return _counters.size();
	}

	@Override
	public String toString() {
		return "[CounterMap: " + _counters + "]";
	}

	private static class Counter implements Serializable {
		private int _val;

		protected Counter(final int val) {
			_val = val;
		}

		protected int increment() {
			return ++_val;
		}

		protected int get() {
			return _val;
		}

		@Override
		public String toString() {
			return Integer.toString(_val);
		}

	}

	/**
	 * @return an {@link Iterator} in insertion order
	 */
	@Override
	public Iterator<Pair<T, Integer>> iterator() {
		return new Iterator<Pair<T, Integer>>() {

			private final Iterator<Map.Entry<T, Counter>> _iter = _counters.entrySet().iterator();

			@Override
			public boolean hasNext() {
				return _iter.hasNext();
			}

			@Override
			public Pair<T, Integer> next() {
				final Map.Entry<T, Counter> e = _iter.next();
				return new Pair<T, Integer>(e.getKey(), e.getValue().get());
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	/**
	 * @return the {@link Pair} with the hightes count. If multiple keys have
	 *         the same count, the first one encountered (insertion order) is
	 *         returned. If the map is empty, a {@link Pair} with an empty key
	 *         and {@link UNKNOWN} count is returned
	 */
	public Pair<T, Integer> getMax() {
		if (_counters.isEmpty()) {
			return Pair.pair(null, UNKNOWN);
		}

		Iterator<Pair<T, Integer>> iter = iterator();
		Pair<T, Integer> max = iter.next();
		while (iter.hasNext()) {
			Pair<T, Integer> current = iter.next();
			if (current.getValue() > max.getValue()) {
				max = current;
			}
		}
		return max;

	}
}
