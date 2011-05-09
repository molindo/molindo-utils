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
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorUtils {

	public static final Iterator<?> EMPTY_ITERATOR = new Iterator<Object>() {

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Object next() {
			throw new NoSuchElementException();
		}

		@Override
		public void remove() {
			throw new IllegalStateException();
		}

	};

	private IteratorUtils() {

	}

	/**
	 * 
	 * @param <T>
	 * @param iter
	 * @return an {@link Iterable} that alway returns the passed
	 *         {@link Iterator} instance
	 */
	public static <T> Iterable<T> iterable(final Iterator<T> iter) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return iter;
			}
		};
	}

	/**
	 * @param <T>
	 * @param iterable
	 * @return {@link Iterable#iterator()} or {@link #EMPTY_ITERATOR} if null
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> iterator(Iterable<T> iterable) {
		return iterable == null ? (Iterator<T>) EMPTY_ITERATOR : iterable.iterator();
	}

	/**
	 * 
	 * @param <T>
	 * @param iter
	 * @return the next element if available, <code>null</code> otherwise
	 */
	public static <T> T next(final Iterator<T> iter) {
		return iter == null || !iter.hasNext() ? null : iter.next();
	}

	/**
	 * 
	 * @param <T>
	 * @param iter
	 * @return an {@link ArrayList} containing all elements
	 */
	public static <T> ArrayList<T> list(final Iterator<T> iter) {
		return addAll(new ArrayList<T>(), iter);
	}

	/**
	 * 
	 * @param <T>
	 * @param iter
	 * @return an {@link ArrayList} containing all elements
	 */
	public static <T> ArrayList<T> list(final Iterator<T> iter, final int initialCapacity) {
		return addAll(new ArrayList<T>(initialCapacity), iter);
	}

	/**
	 * adds all elements from iter to collection
	 * 
	 * @param <T>
	 * @param <C>
	 * @param collection
	 * @param iter
	 * @return
	 */
	public static <T, C extends Collection<T>> C addAll(final C collection, final Iterator<T> iter) {
		while (iter.hasNext()) {
			collection.add(iter.next());
		}
		return collection;
	}

	/**
	 * type save access to {@link #EMPTY_ITERATOR}
	 * 
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> empty() {
		return (Iterator<T>) EMPTY_ITERATOR;
	}

	/**
	 * type safe cast from any array to an {@link Iterable}
	 * 
	 * @param <T>
	 * @param cls
	 * @param list
	 * @return
	 */
	public static <T> Iterable<T> cast(final Class<T> cls, final Object[] list) {
		return cast(cls, Arrays.asList(list));
	}

	/**
	 * a type safe cast for {@link Iterable}
	 * 
	 * @param <T>
	 * @param cls
	 * @param iterable
	 * @return
	 */
	public static <T> Iterable<T> cast(final Class<T> cls, final Iterable<?> iterable) {
		if (cls == null) {
			throw new NullPointerException("cls");
		}
		if (iterable == null) {
			throw new NullPointerException("iterable");
		}

		return new Iterable<T>() {

			@Override
			public Iterator<T> iterator() {
				return new Iterator<T>() {

					private final Iterator<?> _iter = iterable.iterator();

					@Override
					public boolean hasNext() {
						return _iter.hasNext();
					}

					@Override
					public T next() {
						return cls.cast(_iter.next());
					}

					@Override
					public void remove() {
						_iter.remove();
					}
				};
			}
		};
	}

	/**
	 * skips a given number of elements in iterator while there are available
	 * 
	 * @param <T>
	 * @param iter
	 * @param skip
	 * @return
	 */
	public static <T> Iterator<T> skip(final Iterator<T> iter, int skip) {
		while (skip-- > 0 && iter.hasNext()) {
			iter.next();
		}
		return iter;
	}

	/**
	 * don't return more elements than given by max
	 * 
	 * @param <T>
	 * @param iter
	 * @param max
	 * @return
	 */
	public static <T> Iterator<T> max(final Iterator<T> iter, final int max) {
		return new Iterator<T>() {

			int _remaining = max;

			@Override
			public boolean hasNext() {
				return _remaining > 0 && iter.hasNext();
			}

			@Override
			public T next() {
				if (_remaining <= 0) {
					throw new NoSuchElementException();
				}
				_remaining--;
				return iter.next();
			}

			@Override
			public void remove() {
				iter.remove();
			}

		};
	}

	public static <T> Iterator<T> object(final T o) {
		return new Iterator<T>() {

			private boolean _hasNext = o != null;

			@Override
			public boolean hasNext() {
				return _hasNext;
			}

			@Override
			public T next() {
				if (!_hasNext) {
					throw new NoSuchElementException();
				}
				_hasNext = false;
				return o;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}
}
