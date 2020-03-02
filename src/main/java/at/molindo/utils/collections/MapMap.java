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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MapMap<K1, K2, V> implements Map<K1, Map<K2, V>>, Serializable {

	private final Map<K1, Map<K2, V>> _map;

	public static <K1, K2, V> MapMap<K1, K2, V> newMapMap() {
		return new MapMap<K1, K2, V>();
	}

	public MapMap() {
		_map = newMap();
	}

	protected Map<K1, Map<K2, V>> newMap() {
		return new HashMap<K1, Map<K2, V>>();
	}

	protected Map<K1, Map<K2, V>> getMap() {
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
	public Set<Entry<K1, Map<K2, V>>> entrySet() {
		return _map.entrySet();
	}

	@Override
	public boolean equals(final Object o) {
		return _map.equals(o);
	}

	@Override
	public Map<K2, V> get(final Object key) {
		return _map.get(key);
	}

	public V get(final K1 key1, K2 key2) {
		Map<K2, V> map = _map.get(key1);
		return map == null ? null : map.get(key2);
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
	public Set<K1> keySet() {
		return _map.keySet();
	}

	public V put(final K1 key1, final K2 key2, V value) {
		return getMap(key1).put(key2, value);
	}

	@Override
	public Map<K2, V> put(final K1 key, final Map<K2, V> value) {
		return _map.put(key, value);
	}

	@Override
	public void putAll(final Map<? extends K1, ? extends Map<K2, V>> m) {
		_map.putAll(m);
	}

	public void putAll(final K1 key, final Map<K2, V> values) {
		getMap(key).putAll(values);
	}

	@Override
	public Map<K2, V> remove(final Object key) {
		return _map.remove(key);
	}

	public V removeValue(final K1 key, final K2 value) {
		final Map<K2, V> map = _map.get(key);
		return map == null ? null : map.remove(value);
	}

	@Override
	public int size() {
		return _map.size();
	}

	@Override
	public Collection<Map<K2, V>> values() {
		return _map.values();
	}

	@Override
	public String toString() {
		return MapMap.class.getSimpleName() + ": " + _map;
	}

	public Map<K2, V> getMap(final K1 key) {
		Map<K2, V> map = _map.get(key);
		if (map == null) {
			map = new LinkedHashMap<K2, V>();
			_map.put(key, map);
		}
		return map;
	}
}
