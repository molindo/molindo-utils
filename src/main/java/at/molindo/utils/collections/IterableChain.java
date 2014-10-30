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

import static at.molindo.utils.collections.IteratorUtils.iterators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class IterableChain<T> implements Iterable<T> {

	private final Iterable<? extends Iterable<T>> _iterables;

	public static <T> Builder<T> builder() {
		return new Builder<T>();
	}

	public static <T> Builder<T> builder(Class<T> cls) {
		return new Builder<T>();
	}

	public static <T> Builder<T> builder(Iterable<T> iter) {
		return new Builder<T>().add(iter);
	}

	public static <T> Builder<T> builder(T o) {
		return new Builder<T>().add(o);
	}

	public static <T> Builder<T> builder(T... o) {
		return new Builder<T>().add(o);
	}

	public static <T> IterableChain<T> chainIterables(Iterable<? extends Iterable<T>> iterables) {
		return new IterableChain<T>(iterables);
	}

	public IterableChain(Iterable<? extends Iterable<T>> iterables) {
		// make a copy
		_iterables = IteratorUtils.list(iterables);
	}

	@Override
	public Iterator<T> iterator() {
		return new IteratorChain<T>(iterators(_iterables));
	}

	public static class Builder<T> implements Iterable<T> {

		private final List<Iterable<T>> _iterables = new ArrayList<Iterable<T>>();

		private Builder() {
		}

		public Builder<T> add(Iterable<T> iter) {
			_iterables.add(iter);
			return this;
		}

		public Builder<T> add(T o) {
			return add(Collections.singleton(o));
		}

		public Builder<T> add(T... o) {
			return add(Arrays.asList(o));
		}

		public IterableChain<T> build() {
			return new IterableChain<T>(_iterables);
		}

		@Override
		public Iterator<T> iterator() {
			return build().iterator();
		}

	}
}
