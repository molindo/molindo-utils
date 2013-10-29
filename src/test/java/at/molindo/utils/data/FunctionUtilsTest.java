package at.molindo.utils.data;

import static at.molindo.utils.data.FunctionUtils.chain;
import static at.molindo.utils.data.FunctionUtils.parseIntFunction;
import static at.molindo.utils.data.FunctionUtils.toStringFunction;
import static at.molindo.utils.data.FunctionUtils.trimFunction;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FunctionUtilsTest {

	@Test
	public void testToStringFunction() {
		assertEquals("42", toStringFunction().apply(42));
	}

	@Test
	public void testTrimFunction() {
		assertEquals("42", trimFunction().apply(" 42 "));
	}

	@Test
	public void testParseIntFunction() {
		assertEquals(42, parseIntFunction().apply("42").intValue());
	}

	@Test
	public void testChain() {
		assertEquals(42, chain(trimFunction(), parseIntFunction()).apply(" 42 ").intValue());
	}

}
