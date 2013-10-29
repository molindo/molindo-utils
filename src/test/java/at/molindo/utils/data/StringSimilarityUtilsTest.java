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

import static at.molindo.utils.data.StringSimilarityUtils.similarity;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringSimilarityUtilsTest {

	@Test
	public void test() {
		float s = similarity("foo", "bar", 0.8f);
		assertEquals(0.0f, s, 0.005f);

		s = similarity("foobar", "foobaa", 0.9f);
		assertEquals(0.9167f, s, 0.005f);

	}

}
