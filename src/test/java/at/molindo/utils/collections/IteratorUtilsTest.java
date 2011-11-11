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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

import at.molindo.utils.data.Function;

public class IteratorUtilsTest {

	@Test
	public void testObject() {
		Iterator<String> iter = IteratorUtils.object("foo");

		assertTrue(iter.hasNext());
		assertEquals("foo", iter.next());
		assertFalse(iter.hasNext());

		try {
			iter.remove();
			fail();
		} catch (UnsupportedOperationException e) {
		}
		try {
			iter.next();
			fail();
		} catch (NoSuchElementException e) {
		}
	}

	@Test
	public void testFilter() {
		List<Integer> list = Arrays.asList(1, 2, 3, null, 5);

		assertEquals(Arrays.asList(1, 3, 5),
				CollectionUtils.list(IteratorUtils.filter(list, new Function<Integer, Boolean>() {

					@Override
					public Boolean apply(Integer input) {
						return input != null && input % 2 == 1;
					}
				})));

		assertEquals(Arrays.asList(2, null),
				CollectionUtils.list(IteratorUtils.filter(list, new Function<Integer, Boolean>() {

					@Override
					public Boolean apply(Integer input) {
						return input == null || input % 2 == 0;
					}
				})));

		assertEquals(Arrays.asList(), CollectionUtils.list(IteratorUtils.filter(list, new Function<Integer, Boolean>() {

			@Override
			public Boolean apply(Integer input) {
				return false;
			}
		})));
	}

	@Test
	public void testEquals() {
		final Iterable<String> l1, l2, l3;

		l1 = Arrays.asList("foo", "bar");
		l2 = Arrays.asList("foo", "bar");
		l3 = Arrays.asList("foo");

		assertTrue(IteratorUtils.equals(l1, l1));
		assertTrue(IteratorUtils.equals(l1, l2));
		assertFalse(IteratorUtils.equals(l1, l3));
		assertFalse(IteratorUtils.equals(l2, l3));
	}
}
