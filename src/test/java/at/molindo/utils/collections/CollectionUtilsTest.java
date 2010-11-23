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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CollectionUtilsTest {

	@Test
	public void testSubList() {
		List<String> list = Arrays.asList("foo", "bar", "baz", "qux");

		assertEquals(list, CollectionUtils.subList(list, 0, 100));
		assertEquals(list.subList(2, 4), CollectionUtils.subList(list, 2, 100));
		assertEquals(list.subList(0, 2), CollectionUtils.subList(list, 0, 2));
		assertEquals(list.subList(2, 3), CollectionUtils.subList(list, 2, 1));
		assertEquals(Collections.emptyList(), CollectionUtils.subList(list, 50, 100));
	}

}
