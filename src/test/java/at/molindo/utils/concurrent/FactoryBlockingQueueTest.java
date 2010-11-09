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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class FactoryBlockingQueueTest {

	private static FactoryBlockingQueue<Integer> q() {
		return new FactoryBlockingQueue<Integer>() {

			private int _next = 0;

			@Override
			protected Integer create() {
				return _next++;
			}

		};
	}

	@Test(expected = IllegalStateException.class)
	public void testAdd() {
		q().add(42);
	}

	@Test
	public void testAddAll() {
		assertFalse(q().addAll(Arrays.asList(42)));
	}

	@Test
	public void testClear() {
		FactoryBlockingQueue<Integer> q = q();
		Integer i = q.peek();
		assertTrue(q.contains(i));

		q.clear();

		assertFalse(q.contains(i));
	}

	@Test
	public void testContains() {
		FactoryBlockingQueue<Integer> q = q();
		assertFalse(q.contains(42));

		Integer i = q.peek();
		assertTrue(q.contains(i));

		assertEquals(i, q.poll());

		assertFalse(q.contains(i));
	}

	@Test
	public void testContainsAll() {
		FactoryBlockingQueue<Integer> q = q();
		assertFalse(q.contains(42));

		Integer i = q.peek();
		assertTrue(q.containsAll(Arrays.asList(i)));
		assertFalse(q.containsAll(Arrays.asList(i, 42)));

		assertEquals(i, q.poll());

		assertFalse(q.containsAll(Arrays.asList(i)));
	}

	@Test
	public void testDrainToCollectionOfQsuperE() {
		assertEquals(1, q().drainTo(new ArrayList<Integer>()));
	}

	@Test
	public void testDrainToCollectionOfQsuperEInt() {
		FactoryBlockingQueue<Integer> q = q();
		assertEquals(1, q.drainTo(new ArrayList<Integer>(), 10));
		assertEquals(0, q.drainTo(new ArrayList<Integer>(), 0));
	}

	@Test
	public void testElement() {
		assertNotNull(q().element());
	}

	@Test
	public void testIsEmpty() {
		assertFalse(q().isEmpty());
	}

	@Test(expected = IllegalStateException.class)
	public void testIterator() {
		FactoryBlockingQueue<Integer> q = q();
		Iterator<Integer> iter = q.iterator();

		assertTrue(iter.hasNext());

		Integer i = iter.next();

		assertTrue(q.contains(i));

		iter.remove();

		assertFalse(q.contains(i));

		assertTrue(iter.hasNext());

		iter.remove();
	}

	@Test
	public void testOfferE() {
		assertFalse(q().offer(42));
	}

	@Test
	public void testOfferELongTimeUnit() throws InterruptedException {
		assertFalse(q().offer(42, 50, TimeUnit.MILLISECONDS));
	}

	@Test
	public void testPeek() {
		FactoryBlockingQueue<Integer> q = q();
		Integer i = q.peek();
		assertNotNull(i);
		assertSame(i, q.peek());
		assertTrue(q.contains(i));
	}

	@Test
	public void testPoll() {
		FactoryBlockingQueue<Integer> q = q();
		Integer i = q.poll();
		assertNotNull(i);
		assertFalse(q.contains(i));
		assertFalse(i.equals(q.poll()));
	}

	@Test
	public void testPollLongTimeUnit() throws InterruptedException {
		FactoryBlockingQueue<Integer> q = q();
		Integer i = q.poll(20, TimeUnit.MILLISECONDS);
		assertNotNull(i);
		assertFalse(q.contains(i));
		assertFalse(i.equals(q.poll(20, TimeUnit.MILLISECONDS)));
	}

	@Test(expected = InterruptedException.class)
	public void testPut() throws InterruptedException {

		final Thread t = Thread.currentThread();
		new Thread() {
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				t.interrupt();
			}
		}.start();

		q().put(42);
	}

	@Test
	public void testRemainingCapacity() {
		assertEquals(0, q().remainingCapacity());
	}

	@Test
	public void testRemove() {
		FactoryBlockingQueue<?> q = q();
		assertNotNull(q.remove());
	}

	@Test
	public void testRemoveAll() {
		FactoryBlockingQueue<Integer> q = q();

		Integer i = q.peek();

		assertTrue(q.contains(i));
		q.removeAll(Arrays.asList(i));
		assertFalse(q.contains(i));

	}

	@Test
	public void testRemoveObject() {
		FactoryBlockingQueue<Integer> q = q();
		assertFalse(q.contains(42));
		Integer i = q.peek();
		assertTrue(q.contains(i));
		q.remove(i);
		assertFalse(q.contains(i));
	}

	@Test
	public void testRetainAll() {
		FactoryBlockingQueue<Integer> q = q();
		assertTrue(q.retainAll(Arrays.asList(42)));

		Integer i = q.peek();
		assertFalse(q.retainAll(Arrays.asList(i, 42)));

		q.remove(i);
		assertTrue(q.retainAll(Arrays.asList(i, 42)));
	}

	@Test
	public void testSize() {
		assertEquals(1, q().size());
	}

	@Test
	public void testTake() throws InterruptedException {
		FactoryBlockingQueue<?> q = q();
		assertNotNull(q.take());
	}

	@Test
	public void testToArray() {
		FactoryBlockingQueue<Integer> q = q();
		assertArrayEquals(new Integer[] { q.peek() }, q.toArray());
	}

	@Test
	public void testToArrayTArray() {
		FactoryBlockingQueue<Integer> q = q();
		assertArrayEquals(new Integer[] { q.peek(), null, 42 }, q.toArray(new Integer[] { 42, 42, 42 }));
	}

}
