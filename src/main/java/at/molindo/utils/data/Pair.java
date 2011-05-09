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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import at.molindo.utils.collections.CollectionBuilder;

public class Pair<A, B> implements Serializable {

	private static final long serialVersionUID = 1L;

	public static <K, V> HashMap<K, V> toHashMap(final List<Pair<K, V>> pairs) {
		final HashMap<K, V> map = new HashMap<K, V>();
		for (final Pair<K, V> pair : pairs) {
			map.put(pair.getKey(), pair.getValue());
		}
		return map;
	}

	private A _first;

	private B _second;

	/**
	 * utility method to create pairs with implicit parameterization
	 * 
	 * @param <K>
	 * @param <V>
	 * @param key
	 * @param val
	 * @return
	 */
	public static <K, V> Pair<K, V> pair(final K key, final V val) {
		return new Pair<K, V>(key, val);
	}

	/**
	 * utility method to create pairs with implicit parameterization
	 * 
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Pair<K, V> pair() {
		return new Pair<K, V>();
	}

	public static <K> PairCollectionBuilder<K, K> pairs(Class<K> kv) {
		return pairs(kv, kv);
	}

	public static <K, V> PairCollectionBuilder<K, V> pairs(K k, V v) {
		PairCollectionBuilder<K, V> pairs = pairs();
		pairs.pair(k, v);
		return pairs;
	}

	public static <K, V> PairCollectionBuilder<K, V> pairs(Class<K> k, Class<V> v) {
		return pairs();
	}

	public static <K, V> PairCollectionBuilder<K, V> pairs() {
		return new PairCollectionBuilder<K, V>();
	}

	/**
	 * Contructs a pair holding two null values.
	 */
	public Pair() {
		_first = null;
		_second = null;
	}

	/**
	 * Contructs a pair holding the given objects.
	 */
	public Pair(final A a, final B b) {
		_first = a;
		_second = b;
	}

	/**
	 * Contructs a pair holding the objects of the given pair.
	 */
	public Pair(final Pair<A, B> p) {
		_first = p.getFirst();
		_second = p.getSecond();
	}

	public Pair(final Entry<A, B> e) {
		_first = e.getKey();
		_second = e.getValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (_first == null ? 0 : _first.hashCode());
		result = prime * result + (_second == null ? 0 : _second.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Pair)) {
			return false;
		}
		final Pair<?, ?> other = (Pair<?, ?>) obj;
		if (_first == null) {
			if (other._first != null) {
				return false;
			}
		} else if (!_first.equals(other._first)) {
			return false;
		}
		if (_second == null) {
			if (other._second != null) {
				return false;
			}
		} else if (!_second.equals(other._second)) {
			return false;
		}
		return true;
	}

	public A getFirst() {
		return _first;
	}

	public void setFirst(final A first) {
		_first = first;
	}

	public B getSecond() {
		return _second;
	}

	public void setSecond(final B second) {
		_second = second;
	}

	// alias for getSecond, inspired by Map.Entry
	public B getValue() {
		return getSecond();
	}

	// alias for getFirst, inspired by Map.Entry
	public A getKey() {
		return getFirst();
	}

	@Override
	public String toString() {
		return "['" + getKey() + "' = '" + getValue() + "']";
	}

	public static class PairCollectionBuilder<K, V> extends CollectionBuilder<Pair<K, V>, List<Pair<K, V>>> {

		protected PairCollectionBuilder() {
			super(new ArrayList<Pair<K, V>>());
		}

		public PairCollectionBuilder<K, V> pair(K key, V value) {
			add(Pair.pair(key, value));
			return this;
		}
	}
}
