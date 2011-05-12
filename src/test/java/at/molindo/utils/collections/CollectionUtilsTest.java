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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import at.molindo.utils.data.Function;
import at.molindo.utils.data.FunctionUtils;

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

	@Test
	public void find() {
		List<Integer> list = CollectionBuilder.list(1, 2, 3, 4, 5).get();

		Function<Integer, Double> f1 = new Function<Integer, Double>() {

			@Override
			public Double apply(Integer input) {
				return input / 2.0;
			}

		};
		assertEquals(4, (int) CollectionUtils.find(list, f1, 2.0));
		assertEquals(CollectionBuilder.list(4).get(), CollectionUtils.findAll(list, f1, 2.0));

		Function<Integer, Boolean> f2 = new Function<Integer, Boolean>() {

			@Override
			public Boolean apply(Integer input) {
				return input % 2 == 0;
			}

		};

		assertEquals(2, (int) CollectionUtils.find(list, f2));
		assertEquals(CollectionBuilder.list(2, 4).get(), CollectionUtils.findAll(list, f2));
	}

	@Test
	public void addAll() {
		List<Integer> list = CollectionBuilder.list(1, 2, 3, 4, 5).get();

		Set<String> strings = CollectionUtils.addAll(new HashSet<String>(), list, FunctionUtils.toStringFunction());
		assertEquals(CollectionBuilder.set("1", "2", "3", "4", "5").get(), strings);
	}

	@Test
	public void putAll() {
		List<Integer> list = CollectionBuilder.list(1, 2, 3, 4, 5).get();
		Map<String, Integer> strings = CollectionUtils.putAll(new HashMap<String, Integer>(), list,
				FunctionUtils.toStringFunction());
		assertEquals(CollectionBuilder.set("1", "2", "3", "4", "5").get(), strings.keySet());
		assertEquals(CollectionBuilder.set(1, 2, 3, 4, 5).get(), new HashSet<Integer>(strings.values()));
	}
}
