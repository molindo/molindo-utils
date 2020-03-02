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

import static at.molindo.utils.data.PrimitiveUtils.object;
import static at.molindo.utils.data.PrimitiveUtils.primitive;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class PrimitiveUtilsTest {

	@Test
	public void testPrimitive() {
		// null values
		assertEquals(0, primitive((Short) null));
		assertEquals(0, primitive((Integer) null));
		assertEquals(0, primitive((Long) null));
		assertEquals(0, primitive((Double) null), 0.01);
		assertEquals(0, primitive((Float) null), 0.01);
		assertEquals(0x00, primitive((Byte) null));
		assertEquals('\0', primitive((Character) null));
		assertEquals(false, primitive((Boolean) null));

		// arrays
		assertArrayEquals(new short[] { 1 }, primitive(new Short[] { 1 }));
		assertArrayEquals(new int[] { 1 }, primitive(new Integer[] { 1 }));
		assertArrayEquals(new long[] { 1L }, primitive(new Long[] { 1L }));
		assertArrayEquals(new double[] { 1.0 }, primitive(new Double[] { 1.0 }), 0.01);
		assertArrayEquals(new float[] { 1.0f }, primitive(new Float[] { 1.0f }), 0.01f);
		assertArrayEquals(new byte[] { 0x01 }, primitive(new Byte[] { 0x01 }));
		assertArrayEquals(new char[] { '1' }, primitive(new Character[] { '1' }));
		// assertArrayEquals(new boolean[] { false }, primitive(new Boolean[] {
		// false }));

		// collections
		// arrays
		assertArrayEquals(new short[] { 1 }, primitive(Arrays.asList((short) 1), (short) 0));
		assertArrayEquals(new int[] { 1 }, primitive(Arrays.asList(1), 0));
		assertArrayEquals(new long[] { 1L }, primitive(Arrays.asList(1L), 0));
		assertArrayEquals(new double[] { 1.0 }, primitive(Arrays.asList(1.0), 0.00), 0.01);
		assertArrayEquals(new float[] { 1.0f }, primitive(Arrays.asList(1.0f), 0.00f), 0.01f);
		assertArrayEquals(new byte[] { 0x01 }, primitive(Arrays.asList((byte) 0x01), (byte) 0x00));
		assertArrayEquals(new char[] { '1' }, primitive(Arrays.asList('1'), '\0'));

		// primitives to object
		assertArrayEquals(new Short[] { 1 }, object(new short[] { 1 }));
		assertArrayEquals(new Integer[] { 1 }, object(new int[] { 1 }));
		assertArrayEquals(new Long[] { 1L }, object(new long[] { 1L }));
		assertArrayEquals(new Double[] { 1.0 }, object(new double[] { 1.0 }));
		assertArrayEquals(new Float[] { 1.0f }, object(new float[] { 1.0f }));
		assertArrayEquals(new Byte[] { 0x01 }, object(new byte[] { 0x01 }));
		assertArrayEquals(new Character[] { '1' }, object(new char[] { '1' }));
	}
}
