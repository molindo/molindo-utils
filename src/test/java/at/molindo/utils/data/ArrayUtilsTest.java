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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import at.molindo.utils.collections.ArrayUtils;

public class ArrayUtilsTest {

	@Test
	public void testEqualsBytes() {
		byte[] b1 = HexUtils.bytes("0123456789abcdef");
		byte[] b2 = HexUtils.bytes("0023456789abcd00");

		assertTrue(ArrayUtils.equals(b1, b2, 1, 6));
		assertTrue(!ArrayUtils.equals(b1, b2, 0, 6));
		assertTrue(!ArrayUtils.equals(b1, b2, 1, 7));
	}
}
