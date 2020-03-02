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
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class SortedListMap<T, E> extends ListMap<T, E> implements SortedMap<T, List<E>>, Serializable {

	@Override
	protected SortedMap<T, List<E>> newMap() {
		return new TreeMap<T, List<E>>();
	}

	@Override
	protected SortedMap<T, List<E>> getMap() {
		return (SortedMap<T, List<E>>) super.getMap();
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
	public SortedMap<T, List<E>> headMap(final T toKey) {
		return getMap().headMap(toKey);
	}

	@Override
	public T lastKey() {
		return getMap().lastKey();
	}

	@Override
	public SortedMap<T, List<E>> subMap(final T fromKey, final T toKey) {
		return getMap().subMap(fromKey, toKey);
	}

	@Override
	public SortedMap<T, List<E>> tailMap(final T fromKey) {
		return getMap().tailMap(fromKey);
	}

}
