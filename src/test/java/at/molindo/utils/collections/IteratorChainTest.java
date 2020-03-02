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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class IteratorChainTest {

	@Test
	public void test() {
		List<String> foo = Arrays.asList("foo");
		List<String> e1 = Arrays.asList();
		List<String> qux = Arrays.asList("qux", "quux");
		List<String> e2 = Arrays.asList();

		List<List<String>> lists = new ArrayList<List<String>>();
		lists.add(foo);
		lists.add(e1);
		lists.add(qux);
		lists.add(e2);

		List<String> expected = new ArrayList<String>();
		expected.addAll(foo);
		expected.addAll(qux);

		Iterator<String> iter = IteratorChain.chainIterables(lists);
		assertTrue(iter.hasNext());

		List<String> all = IteratorUtils.list(iter);

		assertEquals(expected, all);
	}
}
