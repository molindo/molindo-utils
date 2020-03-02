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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ArrayPairList<K, V> extends ArrayList<Pair<K, V>> implements PairList<K, V> {

	private static final long serialVersionUID = 1L;

	public static <K, V> ArrayPairList<K, V> create() {
		return new ArrayPairList<K, V>();
	}

	public static <K, V> ArrayPairList<K, V> createWithCapacity(int size) {
		return new ArrayPairList<K, V>(size);
	}

	public static <K, V> ArrayPairList<K, V> create(K[] keys, V[] values) {
		return create(Arrays.asList(keys), Arrays.asList(values));
	}

	public static <K, V> ArrayPairList<K, V> create(List<K> keys, List<V> values) {
		if (keys.size() != values.size()) {
			throw new IllegalArgumentException("keys and values must be of same length");
		}

		ArrayPairList<K, V> list = createWithCapacity(keys.size());

		Iterator<K> keysIter = keys.iterator();
		Iterator<V> valuesIter = values.iterator();
		while (keysIter.hasNext()) {
			list.put(keysIter.next(), valuesIter.next());
		}

		return list;
	}

	public ArrayPairList(final int initialCapacity) {
		super(initialCapacity);
	}

	public ArrayPairList() {
		super();
	}

	public ArrayPairList(K[] keys, V[] values) {
		this(Arrays.asList(keys), Arrays.asList(values));
	}

	public ArrayPairList(List<K> keys, List<V> values) {
		super();

		if (keys.size() != values.size()) {
			throw new IllegalArgumentException("keys and values must be of same length");
		}

		Iterator<K> keysIter = keys.iterator();
		Iterator<V> valuesIter = values.iterator();

		while (keysIter.hasNext()) {
			put(keysIter.next(), valuesIter.next());
		}
	}

	/**
	 * Constructs a list containing the elements of the specified collection, in
	 * the order they are returned by the collection's iterator.
	 * 
	 * @param c
	 *            the collection whose elements are to be placed into this list
	 * @throws NullPointerException
	 *             if the specified collection is null
	 */
	public ArrayPairList(final Collection<? extends Pair<K, V>> c) {
		super(c);
	}

	@Override
	public void put(final K key, final V value) {
		add(new Pair<K, V>(key, value));
	}

}
