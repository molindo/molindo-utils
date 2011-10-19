package at.molindo.utils.concurrent;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedPriorityBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E> {

	private final int _capacity;
	private final PriorityQueue<E> _q;

	private final ReentrantLock _lock = new ReentrantLock(true);
	private final Condition _notEmpty = _lock.newCondition();
	private final Condition _notFull = _lock.newCondition();

	public BoundedPriorityBlockingQueue(int capacity) {
		_q = new PriorityQueue<E>(_capacity = capacity);
	}

	public BoundedPriorityBlockingQueue(int capacity, Comparator<? super E> comparator) {
		_q = new PriorityQueue<E>(_capacity = capacity, comparator);
	}

	/**
	 * Inserts the specified element into this priority queue.
	 * 
	 * @param e
	 *            the element to add
	 * @return <tt>true</tt> if the element was added to this queue, else
	 *         <tt>false</tt>
	 * @throws ClassCastException
	 *             if the specified element cannot be compared with elements
	 *             currently in the priority queue according to the priority
	 *             queue's ordering
	 * @throws NullPointerException
	 *             if the specified element is null
	 */
	@Override
	public boolean offer(E e) {
		_lock.lock();
		try {
			if (_q.size() == _capacity) {
				return false;
			} else {
				_q.offer(e);
				_notEmpty.signal();
				return true;
			}
		} finally {
			_lock.unlock();
		}
	}

	@Override
	public void put(E e) throws InterruptedException {
		offer(e, Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	}

	@Override
	public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		_lock.lockInterruptibly();
		try {
			for (;;) {
				if (_q.size() < _capacity) {
					_q.offer(e);
					_notEmpty.signal();
					return true;
				}
				if (nanos <= 0) {
					return false;
				}
				try {
					nanos = _notFull.awaitNanos(nanos);
				} catch (InterruptedException ie) {
					_notFull.signal(); // propagate to non-interrupted thread
					throw ie;
				}
			}
		} finally {
			_lock.unlock();
		}
	}

	@Override
	public E poll() {
		_lock.lock();
		try {
			E e = _q.poll();
			if (e != null) {
				_notFull.signal();
			}
			return e;
		} finally {
			_lock.unlock();
		}
	}

	@Override
	public E take() throws InterruptedException {
		_lock.lockInterruptibly();
		try {
			try {
				while (_q.size() == 0) {
					_notEmpty.await();
				}
			} catch (InterruptedException ie) {
				_notEmpty.signal(); // propagate to non-interrupted thread
				throw ie;
			}
			E x = _q.poll();
			assert x != null;
			_notFull.signal();
			return x;
		} finally {
			_lock.unlock();
		}
	}

	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		long nanos = unit.toNanos(timeout);
		_lock.lockInterruptibly();
		try {
			for (;;) {
				E x = _q.poll();
				if (x != null) {
					_notFull.signal();
					return x;
				}
				if (nanos <= 0) {
					return null;
				}
				try {
					nanos = _notEmpty.awaitNanos(nanos);
				} catch (InterruptedException ie) {
					_notEmpty.signal(); // propagate to non-interrupted thread
					throw ie;
				}
			}
		} finally {
			_lock.unlock();
		}
	}

	@Override
	public E peek() {
		_lock.lock();
		try {
			return _q.peek();
		} finally {
			_lock.unlock();
		}
	}

	/**
	 * Returns the comparator used to order the elements in this queue, or
	 * <tt>null</tt> if this queue uses the {@linkplain Comparable natural
	 * ordering} of its elements.
	 * 
	 * @return the comparator used to order the elements in this queue, or
	 *         <tt>null</tt> if this queue uses the natural ordering of its
	 *         elements
	 */
	public Comparator<? super E> comparator() {
		return _q.comparator();
	}

	@Override
	public int size() {
		_lock.lock();
		try {
			return _q.size();
		} finally {
			_lock.unlock();
		}
	}

	@Override
	public int remainingCapacity() {
		return _capacity - size();
	}

	/**
	 * Removes a single instance of the specified element from this queue, if it
	 * is present. More formally, removes an element {@code e} such that
	 * {@code o.equals(e)}, if this queue contains one or more such elements.
	 * Returns {@code true} if and only if this queue contained the specified
	 * element (or equivalently, if this queue changed as a result of the call).
	 * 
	 * @param o
	 *            element to be removed from this queue, if present
	 * @return <tt>true</tt> if this queue changed as a result of the call
	 */
	@Override
	public boolean remove(Object o) {
		_lock.lock();
		try {
			if (_q.remove(o)) {
				_notFull.signal();
				return true;
			} else {
				return false;
			}
		} finally {
			_lock.unlock();
		}
	}

	/**
	 * Returns {@code true} if this queue contains the specified element. More
	 * formally, returns {@code true} if and only if this queue contains at
	 * least one element {@code e} such that {@code o.equals(e)}.
	 * 
	 * @param o
	 *            object to be checked for containment in this queue
	 * @return <tt>true</tt> if this queue contains the specified element
	 */
	@Override
	public boolean contains(Object o) {
		_lock.lock();
		try {
			return _q.contains(o);
		} finally {
			_lock.unlock();
		}
	}

	/**
	 * Returns an array containing all of the elements in this queue. The
	 * returned array elements are in no particular order.
	 * 
	 * <p>The returned array will be "safe" in that no references to it are
	 * maintained by this queue. (In other words, this method must allocate a
	 * new array). The caller is thus free to modify the returned array.
	 * 
	 * <p>This method acts as bridge between array-based and collection-based
	 * APIs.
	 * 
	 * @return an array containing all of the elements in this queue
	 */
	@Override
	public Object[] toArray() {
		_lock.lock();
		try {
			return _q.toArray();
		} finally {
			_lock.unlock();
		}
	}

	@Override
	public String toString() {
		_lock.lock();
		try {
			return _q.toString();
		} finally {
			_lock.unlock();
		}
	}

	/**
	 * @throws UnsupportedOperationException
	 *             {@inheritDoc}
	 * @throws ClassCastException
	 *             {@inheritDoc}
	 * @throws NullPointerException
	 *             {@inheritDoc}
	 * @throws IllegalArgumentException
	 *             {@inheritDoc}
	 */
	@Override
	public int drainTo(Collection<? super E> c) {
		return drainTo(c, Integer.MAX_VALUE);
	}

	/**
	 * @throws UnsupportedOperationException
	 *             {@inheritDoc}
	 * @throws ClassCastException
	 *             {@inheritDoc}
	 * @throws NullPointerException
	 *             {@inheritDoc}
	 * @throws IllegalArgumentException
	 *             {@inheritDoc}
	 */
	@Override
	public int drainTo(Collection<? super E> c, int maxElements) {
		if (c == null) {
			throw new NullPointerException();
		}
		if (c == this) {
			throw new IllegalArgumentException();
		}
		if (maxElements <= 0) {
			return 0;
		}
		_lock.lock();
		try {
			int n = 0;
			E e;
			while (n < maxElements && (e = _q.poll()) != null) {
				c.add(e);
				++n;
				_notFull.signal();
			}
			return n;
		} finally {
			_lock.unlock();
		}
	}

	/**
	 * Atomically removes all of the elements from this queue. The queue will be
	 * empty after this call returns.
	 */
	@Override
	public void clear() {
		_lock.lock();
		try {
			_q.clear();
			_notFull.signalAll();
		} finally {
			_lock.unlock();
		}
	}

	/**
	 * Returns an array containing all of the elements in this queue; the
	 * runtime type of the returned array is that of the specified array. The
	 * returned array elements are in no particular order. If the queue fits in
	 * the specified array, it is returned therein. Otherwise, a new array is
	 * allocated with the runtime type of the specified array and the size of
	 * this queue.
	 * 
	 * <p>If this queue fits in the specified array with room to spare (i.e.,
	 * the array has more elements than this queue), the element in the array
	 * immediately following the end of the queue is set to <tt>null</tt>.
	 * 
	 * <p>Like the {@link #toArray()} method, this method acts as bridge between
	 * array-based and collection-based APIs. Further, this method allows
	 * precise control over the runtime type of the output array, and may, under
	 * certain circumstances, be used to save allocation costs.
	 * 
	 * <p>Suppose <tt>x</tt> is a queue known to contain only strings. The
	 * following code can be used to dump the queue into a newly allocated array
	 * of <tt>String</tt>:
	 * 
	 * <pre> String[] y = x.toArray(new String[0]);</pre>
	 * 
	 * Note that <tt>toArray(new Object[0])</tt> is identical in function to
	 * <tt>toArray()</tt>.
	 * 
	 * @param a
	 *            the array into which the elements of the queue are to be
	 *            stored, if it is big enough; otherwise, a new array of the
	 *            same runtime type is allocated for this purpose
	 * @return an array containing all of the elements in this queue
	 * @throws ArrayStoreException
	 *             if the runtime type of the specified array is not a supertype
	 *             of the runtime type of every element in this queue
	 * @throws NullPointerException
	 *             if the specified array is null
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		_lock.lock();
		try {
			return _q.toArray(a);
		} finally {
			_lock.unlock();
		}
	}

	/**
	 * Returns an iterator over the elements in this queue. The iterator does
	 * not return the elements in any particular order. The returned
	 * <tt>Iterator</tt> is a "weakly consistent" iterator that will never throw
	 * {@link ConcurrentModificationException}, and guarantees to traverse
	 * elements as they existed upon construction of the iterator, and may (but
	 * is not guaranteed to) reflect any modifications subsequent to
	 * construction.
	 * 
	 * @return an iterator over the elements in this queue
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterator<E> iterator() {
		return new Itr((E[]) toArray());
	}

	/**
	 * Snapshot iterator that works off copy of underlying q array.
	 */
	private class Itr implements Iterator<E> {
		final E[] array; // Array of all elements
		int cursor; // index of next element to return;
		int lastRet; // index of last element, or -1 if no such

		Itr(E[] array) {
			lastRet = -1;
			this.array = array;
		}

		@Override
		public boolean hasNext() {
			return cursor < array.length;
		}

		@Override
		public E next() {
			if (cursor >= array.length) {
				throw new NoSuchElementException();
			}
			lastRet = cursor;
			return array[cursor++];
		}

		@Override
		public void remove() {
			if (lastRet < 0) {
				throw new IllegalStateException();
			}
			Object x = array[lastRet];
			lastRet = -1;
			// Traverse underlying queue to find == element,
			// not just a .equals element.
			_lock.lock();
			try {
				for (Iterator<E> it = _q.iterator(); it.hasNext();) {
					if (it.next() == x) {
						it.remove();
						_notFull.signal();
						return;
					}
				}
			} finally {
				_lock.unlock();
			}
		}
	}

	/**
	 * Saves the state to a stream (that is, serializes it). This merely wraps
	 * default serialization within lock. The serialization strategy for items
	 * is left to underlying Queue. Note that locking is not needed on
	 * deserialization, so readObject is not defined, just relying on default.
	 */
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		_lock.lock();
		try {
			s.defaultWriteObject();
		} finally {
			_lock.unlock();
		}
	}
}
