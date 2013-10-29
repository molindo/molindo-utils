package at.molindo.utils.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FunctionUtilsTest {

	@Test
	public void testToStringFunction() {
		assertEquals("42", FunctionUtils.toStringFunction().apply(42));
	}

	@Test
	public void testTrimFunction() {
		assertEquals("42", FunctionUtils.trimFunction().apply(" 42 "));
	}

	@Test
	public void testParseInt() {
		assertEquals(42, FunctionUtils.parseInt().apply("42").intValue());
	}

}
