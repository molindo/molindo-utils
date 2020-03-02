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

class BusinessKeySet<K, V extends IBusinessKey<K>> implements IBusinessKeySet<K, V>, Serializable {

	private final IBusinessKeyMap<K, V> _map;

	static <K, V extends IBusinessKey<K>> BusinessKeySet<K, V> newSet(IBusinessKeyMap<K, V> map) {
		return new BusinessKeySet<K, V>(map);
	}

	private BusinessKeySet(IBusinessKeyMap<K, V> map) {
		if (map == null) {
			throw new NullPointerException("map");
		}
		_map = map;
	}

	@Override
	public IBusinessKeyMap<K, V> getMap() {
		return _map;
	}

	@Override
	public int size() {
		return _map.size();
	}

	@Override
	public boolean isEmpty() {
		return _map.isEmpty();
	}

	@Override
	public Iterator<V> iterator() {
		return _map.values().iterator();
	}

	@Override
	public Object[] toArray() {
		return _map.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return _map.values().toArray(a);
	}

	@Override
	public boolean add(V e) {
		K key = e.getBusinessKey();
		V prev = _map.put(key, e);
		if (prev != null) {
			// put back to fit Set.add(..) contract
			_map.put(key, prev);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean addAll(Collection<? extends V> c) {
		boolean changed = false;
		for (V o : c) {
			changed |= add(o);
		}
		return changed;
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof IBusinessKey<?>) {
			Object key = ((IBusinessKey<?>) o).getBusinessKey();
			Object mapped = _map.get(key);
			return mapped != null && mapped.equals(o);
		}
		return false;
	}

	@Override
	public boolean containsKey(K key) {
		return _map.containsKey(key);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean containsAllKeys(Iterable<K> keys) {
		for (K key : keys) {
			if (!containsKey(key)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (o instanceof IBusinessKey<?>) {
			Object key = ((IBusinessKey<?>) o).getBusinessKey();
			Object mapped = _map.get(key);
			if (mapped != null && mapped.equals(o)) {
				return _map.remove(key) != null;
			}
		}
		return false;
	}

	@Override
	public boolean removeKey(K key) {
		return _map.remove(key) != null;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
			changed |= remove(o);
		}
		return changed;
	}

	@Override
	public boolean removeAllKeys(Iterable<K> keys) {
		boolean changed = false;
		for (K key : keys) {
			changed |= removeKey(key);
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		Iterator<V> iter = iterator();
		while (iter.hasNext()) {
			if (!c.contains(iter.next())) {
				iter.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean retainAllKeys(Collection<K> keys) {
		boolean changed = false;
		Iterator<K> iter = _map.keySet().iterator();
		while (iter.hasNext()) {
			if (!keys.contains(iter.next())) {
				iter.remove();
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public void clear() {
		_map.clear();
	}
}