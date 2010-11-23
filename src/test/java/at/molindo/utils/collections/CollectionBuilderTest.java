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

import static at.molindo.utils.collections.CollectionBuilder.builder;
import static at.molindo.utils.collections.CollectionBuilder.list;
import static at.molindo.utils.collections.CollectionBuilder.set;
import static at.molindo.utils.collections.CollectionBuilder.sortedSet;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class CollectionBuilderTest {
	@Test
	public void testBuilder() {

		// custom collection

		ArrayList<String> list = builder(new ArrayList<String>()).add("foo").addAll("bar", "baz").get();
		assertEquals(Arrays.asList("foo", "bar", "baz"), list);

		// list

		list = list(String.class).add("foo").get();
		assertEquals(CollectionUtils.list("foo"), list);

		list = CollectionBuilder.<String> list().add("foo").get();
		assertEquals(CollectionUtils.list("foo"), list);

		// set

		Set<String> set = set(String.class).add("foo").get();
		assertEquals(CollectionUtils.set("foo"), set);

		list = CollectionBuilder.<String> list().add("foo").get();
		assertEquals(CollectionUtils.set("foo"), set);

		// sorted set

		Set<String> sortedSet = sortedSet(String.class).add("foo").get();
		assertEquals(CollectionUtils.sortedSet("foo"), sortedSet);

		sortedSet = CollectionBuilder.<String> sortedSet().add("foo").get();
		assertEquals(CollectionUtils.sortedSet("foo"), sortedSet);

		// generic elements
		List<Map<String, Object>> mapList = builder(new LinkedList<Map<String, Object>>()).add(
				new HashMap<String, Object>()).get();
		assertEquals(1, mapList.size());

	}
}
