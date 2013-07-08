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

import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class SortedSetMap<T, E> extends SetMap<T, E> implements SortedMap<T, Set<E>> {

	@Override
	protected SortedMap<T, Set<E>> newMap() {
		return new TreeMap<T, Set<E>>();
	}

	@Override
	protected SortedMap<T, Set<E>> getMap() {
		return (SortedMap<T, Set<E>>) super.getMap();
	}

	@Override
	public Comparator<? super T> comparator() {
		return getMap().comparator();
	}

	@Override
	public T firstKey() {
		return getMap().firstKey();
	}

	@Override
	public SortedMap<T, Set<E>> headMap(final T toKey) {
		return getMap().headMap(toKey);
	}

	@Override
	public T lastKey() {
		return getMap().lastKey();
	}

	@Override
	public SortedMap<T, Set<E>> subMap(final T fromKey, final T toKey) {
		return getMap().subMap(fromKey, toKey);
	}

	@Override
	public SortedMap<T, Set<E>> tailMap(final T fromKey) {
		return getMap().tailMap(fromKey);
	}

}
