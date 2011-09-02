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

public class BusinessKeyHashMap<K, V extends IBusinessKey<K>> extends HashMap<K, V> implements IBusinessKeyMap<K, V> {

	private static final long serialVersionUID = 1L;

	private transient IBusinessKeySet<K, V> _valueSet;

	public static <K, V extends IBusinessKey<K>> BusinessKeyHashMap<K, V> newMap(Class<V> cls) {
		return new BusinessKeyHashMap<K, V>();
	}

	public static <K, V extends IBusinessKey<K>> BusinessKeyHashMap<K, V> newMap(Iterable<V> c) {
		BusinessKeyHashMap<K, V> map = newMap();
		map.putAll(c);
		return map;
	}

	public static <K, V extends IBusinessKey<K>> BusinessKeyHashMap<K, V> newMap() {
		return new BusinessKeyHashMap<K, V>();
	}

	@Override
	public V put(V v) {
		return put(v.getBusinessKey(), v);
	}

	@Override
	public void putAll(Iterable<V> c) {
		for (V v : c) {
			put(v);
		}
	}

	@Override
	public IBusinessKeySet<K, V> values() {
		if (_valueSet == null) {
			_valueSet = BusinessKeySet.newSet(this);
		}
		return _valueSet;
	}

}
