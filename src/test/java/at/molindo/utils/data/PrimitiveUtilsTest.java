package at.molindo.utils.data;

import static at.molindo.utils.data.PrimitiveUtils.primitive;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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
	}

}
