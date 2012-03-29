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

import at.molindo.utils.data.Function;
import at.molindo.utils.data.ObjectUtils;

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

	public static final Iterable<?> EMPTY_ITERABLE = new Iterable<Object>() {

		@Override
		public Iterator<Object> iterator() {
			return empty();
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
	 * transforms an {@link Iterator} of {@link Iterable}s to an
	 * {@link Iterator} of {@link Iterator}s
	 */
	public static <T> Iterator<Iterator<T>> iterators(Iterator<? extends Iterable<T>> iterables) {
		return transform(iterables, new Function<Iterable<T>, Iterator<T>>() {

			@Override
			public Iterator<T> apply(Iterable<T> input) {
				return iterator(input);
			}
		});
	}

	/**
	 * transforms an {@link Iterable} of {@link Iterable}s to an
	 * {@link Iterable} of {@link Iterator}s
	 */
	public static <T> Iterable<Iterator<T>> iterators(Iterable<? extends Iterable<T>> iterables) {
		return transform(iterables, new Function<Iterable<T>, Iterator<T>>() {

			@Override
			public Iterator<T> apply(Iterable<T> input) {
				return iterator(input);
			}
		});
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
	 * type save access to {@link #EMPTY_ITERABLE}
	 * 
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterable<T> emptyIterable() {
		return (Iterable<T>) EMPTY_ITERABLE;
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

	public static <T> Iterable<T> filter(final Iterable<T> iterable, final Function<T, Boolean> filter) {
		return new Iterable<T>() {

			@Override
			public Iterator<T> iterator() {
				return filter(iterable.iterator(), filter);
			}

		};
	}

	/**
	 * 
	 * @param <T>
	 * @param iter
	 * @param filter
	 *            function that returns true if object is allowed
	 * @return
	 */
	public static <T> Iterator<T> filter(final Iterator<T> iter, final Function<T, Boolean> filter) {
		return new Iterator<T>() {

			private T _next = findNext();
			private boolean _hasNext;

			private T findNext() {
				while (iter.hasNext()) {
					T next = iter.next();
					if (Boolean.TRUE.equals(filter.apply(next))) {
						_hasNext = true;
						return next;
					}
				}
				_hasNext = false;
				return null;
			}

			@Override
			public boolean hasNext() {
				return _hasNext;
			}

			@Override
			public T next() {
				T next = _next;
				_next = findNext();
				return next;
			}

			@Override
			public void remove() {
				iter.remove();
			}

		};
	}

	public static <F, T> Iterator<T> transform(final Iterator<? extends F> iter, final Function<F, T> f) {
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public T next() {
				return f.apply(iter.next());
			}

			@Override
			public void remove() {
				iter.remove();
			}
		};
	}

	public static <F, T> Iterable<T> transform(final Iterable<? extends F> iterable, final Function<F, T> f) {
		return new Iterable<T>() {

			@Override
			public Iterator<T> iterator() {
				return transform(iterable.iterator(), f);
			}
		};
	}

	public static boolean equals(Iterable<?> iterable1, Iterable<?> iterable2) {
		return equals(iterable1.iterator(), iterable2.iterator());
	}

	public static boolean equals(Iterator<?> iter1, Iterator<?> iter2) {
		while (iter1.hasNext() && iter2.hasNext()) {
			if (!ObjectUtils.equals(iter1.next(), iter2.next())) {
				return false;
			}
		}
		return iter1.hasNext() == iter2.hasNext();
	}
}
