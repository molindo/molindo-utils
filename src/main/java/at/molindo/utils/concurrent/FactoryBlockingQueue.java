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

package at.molindo.utils.concurrent;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author stf
 * 
 * @param <E>
 * 
 * @deprecated Synchronization of this class is broken. Using
 *             synchronized(_queue) doesn't work (see FindBugs report)
 */
@Deprecated
public abstract class FactoryBlockingQueue<E> implements BlockingQueue<E> {

	private ArrayBlockingQueue<E> _queue = new ArrayBlockingQueue<E>(1);

	private final void fill() {
		if (_queue.size() == 0) {
			E e = create();
			if (e == null) {
				throw new NullPointerException("e");
			}
			_queue.add(e);
		}
	}

	/**
	 * @return a new element, never null
	 */
	protected abstract E create();

	@Override
	public E remove() {
		// poll never returns null in this implementation
		return poll();
	}

	public E take() throws InterruptedException {
		// poll never returns null in this implementation
		return poll();
	}

	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		// poll never returns null in this implementation
		return poll();
	}

	@Override
	public E poll() {
		E e;

		synchronized (_queue) {
			fill();
			e = _queue.poll();
		}

		if (e == null) {
			throw new RuntimeException("queue empty");
		}
		return e;
	}

	public E element() {
		// peek never returns null in this implementation
		return peek();
	}

	public synchronized E peek() {
		E e;

		synchronized (_queue) {
			fill();
			e = _queue.peek();
		}

		if (e == null) {
			throw new RuntimeException("queue empty");
		}
		return e;
	}

	/**
	 * @return always false
	 */
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	/**
	 * @throws always
	 *             an {@link IllegalStateException}
	 */
	public boolean add(E e) {
		throw new IllegalStateException("operation not supported");
	}

	/**
	 * @return always false
	 */
	public boolean offer(E e) {
		return false;
	}

	/**
	 * WARNING: blocks indefinitely as this queue is always full by definition
	 */
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "UW_UNCOND_WAIT", justification = "see javadoc")
	public void put(E e) throws InterruptedException {
		// this is a very bad idea
		Object mon = new Object();
		synchronized (mon) {
			while (true) {
				mon.wait();
			}
		}
	}

	/**
	 * blocks until timeout as this queue is always full by definition
	 */
	public boolean offer(E e, long timeout, TimeUnit unit)
			throws InterruptedException {

		Object mon = new Object();
		synchronized (mon) {
			mon.wait(unit.toMillis(timeout));
		}
		return false;
	}

	public int drainTo(Collection<? super E> c) {
		return drainTo(c, 1);
	}

	public int drainTo(Collection<? super E> c, int maxElements) {
		if (c == this) {
			throw new IllegalArgumentException("cannot drainTo self");
		}

		if (maxElements == 0) {
			return 0;
		} else {
			c.add(remove());
			return 1;
		}
	}

	public boolean containsAll(Collection<?> c) {
		return _queue.containsAll(c);
	}

	public boolean removeAll(Collection<?> c) {
		return _queue.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		synchronized (_queue) {
			if (_queue.size() == 0) {
				// pretend to remove yet uncreated object
				return true;
			} else {
				return _queue.retainAll(c);
			}
		}
	}

	public boolean remove(Object o) {
		return _queue.remove(o);
	}

	public boolean contains(Object o) {
		return _queue.contains(o);
	}

	public void clear() {
		_queue.clear();
	}

	public boolean isEmpty() {
		return false;
	}

	public int size() {
		return 1;
	}

	public int remainingCapacity() {
		return 0;
	}

	public Object[] toArray() {
		return new Object[] { peek() };
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length == 0) {
			a = (T[]) java.lang.reflect.Array.newInstance(a.getClass()
					.getComponentType(), 1);
		}

		a[0] = (T) peek();

		if (a.length > 1) {
			a[1] = null;
		}

		return a;
	}

	public Iterator<E> iterator() {
		return new Iterator<E>() {

			private E _last;

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public E next() {
				return _last = peek();
			}

			@Override
			public void remove() {
				if (_last == null) {
					throw new IllegalStateException(
							"next() not called or already deleted");
				} else {
					FactoryBlockingQueue.this.remove(_last);
					_last = null;
				}
			}
		};
	}
}
