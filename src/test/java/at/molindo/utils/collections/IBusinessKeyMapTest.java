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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class IBusinessKeyMapTest {

	@Test
	public void testHashMap() {
		IBusinessKeyMap<String, Foo> map = BusinessKeyHashMap.newMap();
		test(map);
	}

	@Test
	public void testTreeMap() {
		IBusinessKeyMap<String, Foo> map = BusinessKeyTreeMap.newMap();
		test(map);
	}

	private void test(final IBusinessKeyMap<String, Foo> map) {
		IBusinessKeySet<String, Foo> set = map.values();

		assertNull(map.put(new Foo("k1", "foo")));
		assertTrue(set.add(new Foo("k2", "bar")));
		map.putAll(Arrays.asList(new Foo("k3", "baz"), new Foo("k4", "qux")));
		assertEquals(4, set.size());
		assertEquals(4, map.size());

		assertTrue(map.containsKey("k1"));
		assertTrue(set.containsKey("k1"));
		assertTrue(set.contains(new Foo("k1", "foo")));
		assertFalse(set.contains(new Foo("k1", "foobar")));

		assertTrue(set.removeKey("k2"));
		assertFalse(set.remove(new Foo("k3", "foobar")));

	}

	/**
	 * 
	 */
	private static class Foo implements IBusinessKey<String> {

		private final String _key;
		private final String _foo;

		private Foo(String key, String foo) {
			if (key == null) {
				throw new NullPointerException("key");
			}
			if (foo == null) {
				throw new NullPointerException("foo");
			}
			_key = key;
			_foo = foo;
		}

		@Override
		public String getBusinessKey() {
			return _key;
		}

		public String getFoo() {
			return _foo;
		}

		@Override
		public int hashCode() {
			return getBusinessKey().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Foo)) {
				return false;
			}
			Foo other = (Foo) obj;
			return getBusinessKey().equals(other.getBusinessKey()) && getFoo().equals(other.getFoo());
		}

	}
}
