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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SetMap<T, E> implements Map<T, Set<E>>, Serializable {

	private final Map<T, Set<E>> _map;

	public static <T, E> SetMap<T, E> newSetMap() {
		return new SetMap<T, E>();
	}

	public SetMap() {
		_map = newMap();
	}

	protected Map<T, Set<E>> newMap() {
		return new HashMap<T, Set<E>>();
	}

	protected Set<E> newSet() {
		return new LinkedHashSet<E>();
	}

	protected Map<T, Set<E>> getMap() {
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
	public Set<Entry<T, Set<E>>> entrySet() {
		return _map.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		return _map.equals(o);
	}

	@Override
	public Set<E> get(final Object key) {
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

	public void add(final T key, final E value) {
		getSet(key).add(value);
	}

	public boolean addAll(final T key, final Collection<E> values) {
		return getSet(key).addAll(values);
	}

	@Override
	public Set<E> put(final T key, final Set<E> value) {
		return _map.put(key, value);
	}

	@Override
	public void putAll(final Map<? extends T, ? extends Set<E>> m) {
		_map.putAll(m);
	}

	@Override
	public Set<E> remove(final Object key) {
		return _map.remove(key);
	}

	public boolean removeValue(final T key, final E value) {
		final Set<E> set = _map.get(key);
		return set == null ? false : set.remove(value);
	}

	public boolean removeAll(final T key, final Collection<E> values) {
		final Set<E> set = _map.get(key);
		return set == null ? false : set.removeAll(values);
	}

	@Override
	public int size() {
		return _map.size();
	}

	@Override
	public Collection<Set<E>> values() {
		return _map.values();
	}

	@Override
	public String toString() {
		return SetMap.class.getSimpleName() + ": " + _map;
	}

	public Set<E> getSet(final T key) {
		Set<E> set = _map.get(key);
		if (set == null) {
			set = newSet();
			_map.put(key, set);
		}
		return set;
	}
}
