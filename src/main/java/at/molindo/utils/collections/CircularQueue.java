package at.molindo.utils.collections;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * {@link Queue} implementation that uses a single, fixed-size buffer as if it
 * were connected end-to-end. This structure lends itself easily to buffering
 * data streams.
 */
public class CircularQueue<T> extends AbstractCollection<T> implements Queue<T> {

	private static final int EMPTY = -1;

	private final T[] _elements;

	/*
	 * index of first element in queue, -1 if empty
	 */
	private int _first = EMPTY;

	/*
	 * index of next element in queue
	 */
	private int _next = 0;

	/**
	 * create a new queue with the given capacity
	 */
	@SuppressWarnings("unchecked")
	public CircularQueue(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("capacity must be >= 1, was" + capacity);
		}
		_elements = (T[]) new Object[capacity];
	}

	@Override
	public boolean offer(T e) {
		return add(e);
	}

	/**
	 * overrides first element in queue if queue is full
	 * 
	 * @see Queue#add(Object)
	 */
	@Override
	public boolean add(T e) {
		if (isFull()) {
			onOverflow();
		}
		if (isFull()) {
			// still full?
			throw new IllegalStateException("Queue full");
		}
		_elements[getAndIncrementNext()] = e;
		return true;
	}

	protected void onOverflow() {
		// discard first
		remove();
	}

	@Override
	public T remove() {
		T e = poll();
		if (e == null) {
			throw new NoSuchElementException();
		}
		return e;
	}

	@Override
	public T poll() {
		if (isEmpty()) {
			return null;
		} else {
			// get and remove element
			T e = _elements[_first];
			_elements[getAndIncrementFirst()] = null;
			return e;
		}
	}

	@Override
	public T element() {
		T e = peek();
		if (e == null) {
			throw new NoSuchElementException();
		}
		return e;
	}

	@Override
	public T peek() {
		if (isEmpty()) {
			return null;
		} else {
			return _elements[_first];
		}
	}

	private int getAndIncrementFirst() {
		int prev = _first;
		_first = (_first + 1) % _elements.length;
		if (_first == _next) {
			_first = EMPTY;
		}
		return prev;
	}

	private int getAndIncrementNext() {
		int prev = _next;
		if (isEmpty()) {
			_first = prev;
		}
		_next = (_next + 1) % _elements.length;
		return prev;
	}

	@Override
	public int size() {
		if (isEmpty()) {
			return 0;
		} else if (isFull()) {
			return _elements.length;
		} else {
			return (_elements.length + _next - _first) % _elements.length;
		}

	}

	public boolean isFull() {
		return _first == _next;
	}

	@Override
	public boolean isEmpty() {
		return _first == EMPTY;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private int _next = CircularQueue.this._first;

			/*
			 * not part of list
			 */
			private final int _end = CircularQueue.this._next;

			@Override
			public boolean hasNext() {
				return _next != EMPTY;
			}

			@Override
			public T next() {
				if (!hasNext()) {
					throw new NoSuchElementException();
				}

				T e = _elements[_next];

				_next = (_next + 1) % _elements.length;
				if (_next == _end) {
					_next = EMPTY;
				}

				return e;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public void clear() {
		_first = EMPTY;
		Arrays.fill(_elements, null);
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
}
