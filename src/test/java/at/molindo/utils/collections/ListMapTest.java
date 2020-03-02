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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import at.molindo.utils.data.Function;

public class ListMapTest {

	@Test
	public void build() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
		ListMap<String, Integer> map = ListMap.build(list, new Function<Integer, String>() {

			@Override
			public String apply(Integer input) {
				return input % 2 == 0 ? "even" : "odd";
			}
		});

		assertEquals(Arrays.asList(1, 3, 5, 7), map.get("odd"));
		assertEquals(Arrays.asList(2, 4, 6, 8), map.get("even"));
	}
}
