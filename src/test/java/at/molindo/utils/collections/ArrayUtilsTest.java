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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class ArrayUtilsTest {

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
}
