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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

public class CircularQueueTest {

	@Test
	public void size() {
		CircularQueue<String> q = new CircularQueue<String>(3);

		// make backing array non-trivial, i.e. not starting at 0
		q.add("<null>");
		q.remove();

		assertTrue(q.isEmpty());
		assertFalse(q.isFull());
		assertEquals(0, q.size());

		q.offer("foo");
		assertEquals(1, q.size());

		q.offer("bar");
		assertEquals(2, q.size());

		q.offer("qux");
		assertEquals(3, q.size());
		assertFalse(q.isEmpty());
		assertTrue(q.isFull());

		// overwrite "foo"
		q.offer("quux");
		assertEquals(3, q.size());

		assertEquals("bar", q.peek());
		assertEquals(3, q.size());

		assertEquals("bar", q.poll());
		assertEquals(2, q.size());

		assertEquals("qux", q.poll());
		assertEquals(1, q.size());

		assertEquals("quux", q.poll());
		assertEquals(0, q.size());
		assertTrue(q.isEmpty());
		assertFalse(q.isFull());

		assertNull(q.poll());
		assertEquals(0, q.size());

		q.offer("foo");
		assertEquals(1, q.size());
	}

	@Test
	public void iterator() {
		List<String> list = Arrays.asList("foo", "bar", "baz");

		CircularQueue<String> q = new CircularQueue<String>(3);

		// make backing array non-trivial, i.e. not starting at 0
		q.add("<null>");

		q.addAll(list);
		assertTrue(q.containsAll(list));
		assertArrayEquals(list.toArray(), q.toArray());

		Iterator<String> iter = list.iterator();
		assertTrue(iter.hasNext());

		assertEquals("foo", iter.next());
		assertTrue(iter.hasNext());

		try {
			iter.remove();
			fail();
		} catch (UnsupportedOperationException e) {
			// ignore
		}

		assertEquals("bar", iter.next());
		assertTrue(iter.hasNext());

		assertEquals("baz", iter.next());
		assertFalse(iter.hasNext());

		try {
			iter.next();
			fail();
		} catch (NoSuchElementException e) {
			// ignore
		}
	}
}
