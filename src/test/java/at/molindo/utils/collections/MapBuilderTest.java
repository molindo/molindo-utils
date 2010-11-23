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

import static at.molindo.utils.collections.MapBuilder.builder;
import static at.molindo.utils.collections.MapBuilder.map;
import static at.molindo.utils.collections.MapBuilder.sortedMap;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class MapBuilderTest {
	@Test
	public void testBuilder() {

		Map<String, String> expected = new TreeMap<String, String>();
		expected.put("k1", "v1");
		expected.put("k2", "v2");

		// custom collection
		Map<String, String> map = builder(new HashMap<String, String>()).put("k1", "v1").put("k2", "v2").get();

		assertEquals(expected, map);

		// map
		map = map(String.class, String.class).put("k1", "v1").put("k2", "v2").get();
		assertEquals(expected, map);

		map = MapBuilder.<String, String> map().put("k1", "v1").put("k2", "v2").get();
		assertEquals(expected, map);

		// sorted map
		map = sortedMap(String.class, String.class).put("k1", "v1").put("k2", "v2").get();
		assertEquals(expected, map);

		map = MapBuilder.<String, String> sortedMap().put("k1", "v1").put("k2", "v2").get();
		assertEquals(expected, map);

		// generic elements
		TreeMap<List<String>, Map<String, Object>> genericsMap = builder(
				new TreeMap<List<String>, Map<String, Object>>()).put(Arrays.asList("foo"),
				new HashMap<String, Object>()).get();
		assertEquals(1, genericsMap.size());
	}
}
