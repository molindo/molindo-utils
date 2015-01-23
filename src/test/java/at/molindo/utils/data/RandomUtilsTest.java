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
package at.molindo.utils.data;

import static at.molindo.utils.collections.CollectionUtils.list;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class RandomUtilsTest {

	@Test
	public void testRandom() {
		assertNotNull(RandomUtils.random());
	}

	@Test
	public void testGet() {
		assertEquals("foo", RandomUtils.get(list("foo")));
		assertNotNull(RandomUtils.get(list("foo", "bar")));

		try {
			RandomUtils.get(asList());
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}

	@Test
	public void testRemove() {
		assertEquals("foo", RandomUtils.remove(list("foo")));
		assertNotNull(RandomUtils.remove(list("foo", "bar")));

		try {
			RandomUtils.remove(list());
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {
			// expected
		}
	}

}
