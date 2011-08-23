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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.molindo.utils.data.Function;

public class ListMap<T, E> implements Map<T, List<E>> {

	private final Map<T, List<E>> _map;

	public static <T, E> ListMap<T, E> newListMap() {
		return new ListMap<T, E>();
	}

	public static <T, E> ListMap<T, E> build(Iterable<E> c, Function<E, T> f) {
		ListMap<T, E> map = newListMap();
		for (E e : c) {
			map.put(f.apply(e), e);
		}
		return map;
	}

	public ListMap() {
		_map = newMap();
	}

	protected Map<T, List<E>> newMap() {
		return new HashMap<T, List<E>>();
	}

	protected Map<T, List<E>> getMap() {
		return _map;
	}

	@Override
	public void clear() {
		_map.clear();
	}

	@Override
	public boolean containsKey(final Object key) {
		return _map.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return _map.containsValue(value);
	}

	@Override
	public Set<Entry<T, List<E>>> entrySet() {
		return _map.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		return _map.equals(o);
	}

	@Override
	public List<E> get(final Object key) {
		return _map.get(key);
	}

	@Override
	public int hashCode() {
		return _map.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return _map.isEmpty();
	}

	@Override
	public Set<T> keySet() {
		return _map.keySet();
	}

	public void put(final T key, final E value) {
		getList(key).add(value);
	}

	public void putAll(final T key, final Collection<E> values) {
		getList(key).addAll(values);
	}

	@Override
	public List<E> put(final T key, final List<E> value) {
		return _map.put(key, value);
	}

	@Override
	public void putAll(final Map<? extends T, ? extends List<E>> m) {
		_map.putAll(m);
	}

	public void addAll(final T key, final List<E> values) {
		getList(key).addAll(values);
	}

	@Override
	public List<E> remove(final Object key) {
		return _map.remove(key);
	}

	public boolean remove(final T key, final E value) {
		final List<E> list = _map.get(key);
		return list == null ? false : list.remove(value);
	}

	@Override
	public int size() {
		return _map.size();
	}

	@Override
	public Collection<List<E>> values() {
		return _map.values();
	}

	@Override
	public String toString() {
		return ListMap.class.getSimpleName() + ": " + _map;
	}

	public List<E> getList(final T key) {
		List<E> list = _map.get(key);
		if (list == null) {
			list = new LinkedList<E>();
			_map.put(key, list);
		}
		return list;
	}
}
