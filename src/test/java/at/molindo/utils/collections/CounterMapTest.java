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

import static at.molindo.utils.collections.CounterMap.UNKNOWN;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import at.molindo.utils.data.Pair;

public class CounterMapTest {

	@Test
	public void testIncrement() {
		CounterMap<String> map = new CounterMap<String>();
		assertEquals(UNKNOWN, map.getCount("foo"));
		assertEquals(1, map.increment("foo"));
		assertEquals(1, map.getCount("foo"));
	}

	@Test
	public void testAdd() {
		CounterMap<String> map = new CounterMap<String>();
		assertEquals(UNKNOWN, map.getCount("bar"));
		assertFalse(map.contains("bar"));
		assertTrue(map.add("bar", 0));
		assertTrue(map.contains("bar"));
		assertFalse(map.add("bar", 1));
		assertEquals(0, map.getCount("bar"));
	}

	@Test
	public void testRemove() {
		CounterMap<String> map = new CounterMap<String>(asList("foo", "bar"), 1);
		assertEquals(1, map.getCount("foo"));
		assertEquals(1, map.getCount("bar"));
		assertEquals(1, map.remove("foo"));
		assertEquals(UNKNOWN, map.remove("qux"));
	}

	@Test
	public void testIterator() {
		CounterMap<String> map = new CounterMap<String>(asList("foo", "bar"), 1);

		int count = 0;
		for (Pair<String, Integer> p : map) {

			if ("foo".equals(p.getKey())) {
				assertEquals(1, (int) p.getValue());
				assertEquals(0, count);
			} else if ("bar".equals(p.getKey())) {
				assertEquals(1, (int) p.getValue());
				assertEquals(1, count);
			} else {
				fail("unexpected key " + p);
			}
			count++;
		}

		assertEquals(2, count);
	}

	@Test
	public void getGet() {
		CounterMap<String> map = new CounterMap<String>(asList("foo", "bar"), 1);
		assertEquals(UNKNOWN, (int) map.get("baz").getValue());
		assertEquals(Pair.pair("foo", 1), map.get("foo"));
		assertEquals(Pair.pair("bar", 1), map.get("bar"));
	}

	@Test
	public void getGetMax() {
		CounterMap<String> map = new CounterMap<String>();
		assertEquals(Pair.pair(null, UNKNOWN), map.getMax());

		map.increment("foo");
		assertEquals(Pair.pair("foo", 1), map.getMax());

		map.increment("bar");
		assertEquals(Pair.pair("foo", 1), map.getMax());

		map.increment("bar");
		assertEquals(Pair.pair("bar", 2), map.getMax());
	}
}
