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
package at.molindo.utils.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConversionUtilsTest {

	private static final int[] INTS = { 42, -2345, Integer.MAX_VALUE, Integer.MIN_VALUE, 0 };
	private static final byte[] BYTES = HexUtils.bytes("0000002afffff6d77fffffff8000000000000000");

	@Test
	public void testBytes() {
		assertArrayEquals(BYTES, ConversionUtils.bytes(INTS));
	}

	@Test
	public void testInts() {
		assertArrayEquals(INTS, ConversionUtils.ints(BYTES));
	}

	@Test
	public void testSimpleInt() {
		int value = 0x2348f03e;
		byte[] bytes = ConversionUtils.bytes(value);
		assertEquals(4, bytes.length);

		assertEquals(HexUtils.string(bytes), Integer.toHexString(value));

		int[] values = ConversionUtils.ints(bytes);
		assertEquals(1, values.length);

		assertEquals(value, values[0]);
	}
}
