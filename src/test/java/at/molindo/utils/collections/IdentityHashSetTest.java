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

import java.util.Arrays;

import org.junit.Test;

import at.molindo.utils.data.SerializationUtils;

public class IdentityHashSetTest {

	@Test
	public void test() throws Exception {

		String[] s = new String[] { "foo", "bar", "baz" };

		String foo = "fooo".substring(0, 3); // another foo instance

		IdentityHashSet<String> set = new IdentityHashSet<String>();

		assertEquals(0, set.size());

		assertTrue(set.add(s[0]));
		assertFalse(set.add(s[0]));
		assertEquals(1, set.size());

		assertFalse(set.contains(foo));
		assertTrue(set.contains(s[0]));

		set.addAll(Arrays.asList(s));
		assertEquals(3, set.size());

		assertTrue(set.remove(s[1]));
		assertFalse(set.remove(foo));
		assertTrue(set.remove(s[0]));

		set.addAll(Arrays.asList(s));
		assertEquals(3, set.size());

		IdentityHashSet<?> copy = (IdentityHashSet<?>) SerializationUtils.copy(set);
		assertEquals(3, copy.size());

		// can't use equals as identity is different
		assertTrue(IteratorUtils.equals(set, copy));
	}
}
