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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapBuilder<K, V, M extends Map<K, V>> {

	public static <K, V> MapBuilder<K, V, HashMap<K, V>> map() {
		return new MapBuilder<K, V, HashMap<K, V>>(new HashMap<K, V>());
	}

	public static <K, V> MapBuilder<K, V, HashMap<K, V>> map(Class<K> keyClass, Class<V> valueClass) {
		return new MapBuilder<K, V, HashMap<K, V>>(new HashMap<K, V>());
	}

	public static <K, V> MapBuilder<K, V, TreeMap<K, V>> sortedMap() {
		return new MapBuilder<K, V, TreeMap<K, V>>(new TreeMap<K, V>());
	}

	public static <K, V> MapBuilder<K, V, TreeMap<K, V>> sortedMap(Class<K> keyClass, Class<V> valueClass) {
		return new MapBuilder<K, V, TreeMap<K, V>>(new TreeMap<K, V>());
	}

	public static <K, V, M extends Map<K, V>> MapBuilder<K, V, M> builder(M map) {
		return new MapBuilder<K, V, M>(map);
	}

	private final M _map;

	protected MapBuilder(M map) {
		if (map == null) {
			throw new NullPointerException("map");
		}
		_map = map;
	}

	public M get() {
		return _map;
	}

	public MapBuilder<K, V, M> put(K key, V value) {
		_map.put(key, value);
		return this;
	}

	public MapBuilder<K, V, M> putAll(Map<? extends K, ? extends V> map) {
		_map.putAll(map);
		return this;
	}
}