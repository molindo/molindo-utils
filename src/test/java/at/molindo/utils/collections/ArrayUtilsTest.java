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

import static at.molindo.utils.collections.ArrayUtils.empty;
import static at.molindo.utils.collections.ArrayUtils.first;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import at.molindo.utils.data.HexUtils;

public class ArrayUtilsTest {

	@Test
	public void testEqualsBytes() {
		byte[] b1 = HexUtils.bytes("0123456789abcdef");
		byte[] b2 = HexUtils.bytes("0023456789abcd00");

		assertTrue(ArrayUtils.equals(b1, b2, 1, 6));
		assertTrue(!ArrayUtils.equals(b1, b2, 0, 6));
		assertTrue(!ArrayUtils.equals(b1, b2, 1, 7));
	}

	@Test
	public void testEmpty() {
		assertTrue(empty((Object[]) null));
		assertTrue(empty(new Object[0]));
		assertFalse(empty(new Object[1]));
	}

	@Test
	public void testFirst() {
		assertNull(first((Object[]) null));
		assertNull(first(new Object[0]));
		assertEquals("foo", first(new Object[] { "foo" }));
	}

	@Test
	public void testToIterable() {

		assertNotNull(ArrayUtils.toIterable(null));
		assertNotNull(ArrayUtils.toIterable(new String[0]));

		Object array = new int[] { 0, 1, 2, 3 };
		Iterable<Object> iterable = ArrayUtils.toIterable(array);
		Iterator<Object> iter = iterable.iterator();

		assertTrue(iter.hasNext());
		assertEquals(0, iter.next());
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertEquals(3, iter.next());
		assertFalse(iter.hasNext());

		int next = 0;
		for (Object o : iterable) {
			assertTrue(o instanceof Integer);
			assertEquals(next++, o);
		}
	}

	@Test
	public void testAppend() {
		String[] orig = { "foo", "bar", "baz" };
		String[] appended = ArrayUtils.append(orig, "qux");
		assertFalse(orig == appended);
		assertEquals(orig.length + 1, appended.length);
		for (int i = 0; i < orig.length; i++) {
			assertEquals(orig[i], appended[i]);
		}
		assertEquals("qux", appended[orig.length]);
	}
}
