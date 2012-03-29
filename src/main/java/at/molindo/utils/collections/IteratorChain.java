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

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorChain<T> implements Iterator<T> {

	private final Iterator<Iterator<T>> _iterators;

	private Iterator<T> _next;
	private Iterator<T> _last;

	public static <T> IteratorChain<T> chainIterables(Iterable<? extends Iterable<T>> iterables) {
		return new IteratorChain<T>(iterators(iterables));
	}

	public static <T> IteratorChain<T> chainIterables(Iterator<? extends Iterable<T>> iterables) {
		return new IteratorChain<T>(iterators(iterables));
	}

	public IteratorChain(Iterable<Iterator<T>> iterators) {
		this(iterators.iterator());
	}

	public IteratorChain(Iterator<Iterator<T>> iterator) {
		// make a copy
		_iterators = IteratorUtils.list(iterator).iterator();

		// init
		if (_iterators.hasNext()) {
			_next = _iterators.next();
		}
	}

	@Override
	public boolean hasNext() {
		if (_next == null) {
			return false;
		} else if (_next.hasNext()) {
			return true;
		} else if (_iterators.hasNext()) {
			_next = _iterators.next();
			return hasNext();
		} else {
			_next = null;
			return false;
		}
	}

	@Override
	public T next() {
		if (_next == null) {
			throw new NoSuchElementException();
		}
		T o = _next.next();
		_last = _next;
		return o;
	}

	@Override
	public void remove() {
		if (_last == null) {
			throw new IllegalStateException("next() has not yet been called");
		}
		_last.remove();
	}

}
