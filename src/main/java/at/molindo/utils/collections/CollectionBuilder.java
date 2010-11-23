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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class CollectionBuilder<V, C extends Collection<V>> {

	public static <V> CollectionBuilder<V, ArrayList<V>> list() {
		return builder(new ArrayList<V>());
	}

	public static <V> CollectionBuilder<V, HashSet<V>> set() {
		return builder(new HashSet<V>());
	}

	public static <V> CollectionBuilder<V, TreeSet<V>> sortedSet() {
		return builder(new TreeSet<V>());
	}

	public static <V> CollectionBuilder<V, ArrayList<V>> list(Class<V> cls) {
		return builder(new ArrayList<V>());
	}

	public static <V> CollectionBuilder<V, HashSet<V>> set(Class<V> cls) {
		return builder(new HashSet<V>());
	}

	public static <V> CollectionBuilder<V, TreeSet<V>> sortedSet(Class<V> cls) {
		return builder(new TreeSet<V>());
	}

	public static <V, C extends Collection<V>> CollectionBuilder<V, C> builder(C collection) {
		return new CollectionBuilder<V, C>(collection);
	}

	private final C _collection;

	protected CollectionBuilder(C collection) {
		if (collection == null) {
			throw new NullPointerException("collection");
		}
		_collection = collection;
	}

	public C get() {
		return _collection;
	}

	public CollectionBuilder<V, C> add(V e) {
		_collection.add(e);
		return this;
	}

	public CollectionBuilder<V, C> addAll(Collection<? extends V> c) {
		_collection.addAll(c);
		return this;
	}

	public CollectionBuilder<V, C> addAll(V... e) {
		_collection.addAll(Arrays.asList(e));
		return this;
	}
}