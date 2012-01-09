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

import static java.lang.Integer.signum;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import at.molindo.utils.collections.CollectionUtils;

public class ComparatorUtilsTest {

	@Test
	public void testNullHighCompareTo() {
		assertEquals(0, ComparatorUtils.nullHighCompareTo("foo", "foo"));
		assertEquals(0, ComparatorUtils.nullHighCompareTo(null, null));
		assertEquals(-1, signum(ComparatorUtils.nullHighCompareTo("foo", null)));
		assertEquals(1, signum(ComparatorUtils.nullHighCompareTo(null, "foo")));
	}

	@Test
	public void testNullHighComparator() {
		List<String> list = CollectionUtils.list("foo", "bar", null, "baz", null);
		Collections.sort(list, ComparatorUtils.nullHighComparable());
		assertEquals(CollectionUtils.list("bar", "baz", "foo", null, null), list);
	}

	@Test
	public void testNullLowCompareTo() {
		assertEquals(0, ComparatorUtils.nullLowCompareTo("foo", "foo"));
		assertEquals(0, ComparatorUtils.nullLowCompareTo(null, null));
		assertEquals(1, signum(ComparatorUtils.nullLowCompareTo("foo", null)));
		assertEquals(-1, signum(ComparatorUtils.nullLowCompareTo(null, "foo")));
	}

	@Test
	public void testNullLowComparator() {
		List<String> list = CollectionUtils.list("foo", "bar", null, "baz", null);
		Collections.sort(list, ComparatorUtils.nullLowComparable());
		assertEquals(CollectionUtils.list(null, null, "bar", "baz", "foo"), list);
	}
}
