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
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;

public class MapMapTest {

	@Test
	public void test() {
		MapMap<String, String, String> map = MapMap.newMapMap();

		map.put("foo", "bar", "baz");
		map.putAll("foo", MapBuilder.map(String.class, String.class).put("bar", "qux").put("baz", "qux").get());

		Map<String, String> m = map.get("foo");
		assertNotNull(m);

		assertEquals(2, m.size()); // bar-qux, baz-qux
		assertEquals(m.get("bar"), "qux");
		assertEquals(m.get("baz"), "qux");
	}
}
