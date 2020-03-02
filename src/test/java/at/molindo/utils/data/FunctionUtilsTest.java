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

import static at.molindo.utils.data.FunctionUtils.chain;
import static at.molindo.utils.data.FunctionUtils.parseIntFunction;
import static at.molindo.utils.data.FunctionUtils.toLowerCaseFunction;
import static at.molindo.utils.data.FunctionUtils.toStringFunction;
import static at.molindo.utils.data.FunctionUtils.toUpperCaseFunction;
import static at.molindo.utils.data.FunctionUtils.trimFunction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
	public void testUpperFunction() {
		assertEquals("FOO", toUpperCaseFunction().apply("foo"));
		assertNull(toUpperCaseFunction().apply(null));
	}

	@Test
	public void testLowerFunction() {
		assertEquals("foo", toLowerCaseFunction().apply("FOO"));
		assertNull(toLowerCaseFunction().apply(null));
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
