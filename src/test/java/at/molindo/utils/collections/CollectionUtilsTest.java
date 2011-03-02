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

import static at.molindo.utils.collections.CollectionUtils.putIfAbsent;
import static at.molindo.utils.collections.CollectionUtils.subList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class CollectionUtilsTest {

	@Test
	public void testSubList() {
		List<String> list = Arrays.asList("foo", "bar", "baz", "qux");

		assertEquals(list, subList(list, 0, 100));
		assertEquals(list.subList(2, 4), CollectionUtils.subList(list, 2, 100));
		assertEquals(list.subList(0, 2), CollectionUtils.subList(list, 0, 2));
		assertEquals(list.subList(2, 3), subList(list, 2, 1));
		assertEquals(emptyList(), subList(list, 50, 100));
	}

	@Test
	public void testPutIfAbsent() {
		ConcurrentHashMap<String, Object[]> map = new ConcurrentHashMap<String, Object[]>();

		Object[] old = new Object[0];

		assertSame(old, putIfAbsent(map, "foo", old));
		assertSame(old, putIfAbsent(map, "foo", new Object[1]));
	}
}
