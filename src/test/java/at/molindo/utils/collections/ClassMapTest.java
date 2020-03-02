/**
 * Copyright 2016 Molindo GmbH
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

import java.util.Map;

import org.junit.Test;

public class ClassMapTest {

	@Test
	public void find() {
		ClassMap<String> map = new ClassMap<String>();
		map.put(Object.class, "foo");
		map.put(Number.class, "bar");

		assertEquals("foo", map.find(String.class));
		assertEquals("bar", map.find(Number.class));
		assertEquals("bar", map.find(Double.class));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void findAssignable() {
		ClassMap<String> map = new ClassMap<String>();
		map.put(Object.class, "foo");
		map.put(Integer.class, "bar");
		map.put(Float.class, "baz");
		map.put(String.class, "qux");

		Map<Class<?>, String> assignable = map.findAssignable(Number.class);
		assertEquals(CollectionUtils.set(Integer.class, Float.class), assignable.keySet());
		assertEquals(CollectionUtils.set("bar", "baz"), CollectionUtils.set(assignable.values()));
	}
}
