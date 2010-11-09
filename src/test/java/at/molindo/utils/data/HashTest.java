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

import static org.junit.Assert.*;

import org.junit.Test;

import at.molindo.utils.io.CharsetUtils;

public class HashTest {

	public static Hash h() {
		return h("foo");
	}

	public static Hash h(String content) {
		return Hash.sha256(content);
	}

	@Test
	public void testHashCode() {
		assertEquals(h().hashCode(), h().hashCode());
	}

	@Test
	public void testHashStringIHashAlgorithm() {
		Hash h1 = h();
		Hash h2 = new Hash(h1.toHex(), h1.getAlgorithm());

		assertEquals(h1, h2);
	}

	@Test
	public void testHashByteArrayIHashAlgorithm() {
		Hash h1 = h();
		Hash h2 = new Hash(h1.getBytes(), h1.getAlgorithm());

		assertEquals(h1, h2);
	}

	@Test
	public void testToHex() {
		Hash h1 = h();
		assertEquals(HexUtils.string(h1.getBytes()), h1.toHex());
	}

	@Test
	public void testGetAlgorithm() {
		assertEquals(Hash.Algorithm.SHA_256, h().getAlgorithm());
	}

	@Test
	public void testToString() {
		Hash h1 = h();
		assertEquals(HexUtils.string(h1.getBytes()), h1.toString());
	}

	@Test
	public void testValidateString() {
		String foo = "foo";
		Hash h = h(foo);

		assertTrue(h.validate(foo));
		assertFalse(h.validate("bar"));
	}

	@Test
	public void testValidateStringCharset() {
		String foo = "foo\u00FC";
		Hash h = h(foo);

		assertTrue(h.validate(foo, CharsetUtils.UTF_8));
		assertFalse(h.validate(foo, CharsetUtils.US_ASCII));
	}

	@Test
	public void testValidateByteArray() {
		byte[] foo = ConversionUtils.bytes(42);
		Hash h = Hash.md5(foo);

		assertTrue(h.validate(foo));
		assertFalse(h.validate(ConversionUtils.bytes(43)));
	}

	@Test
	public void testEqualsObject() {
		assertEquals(h(), h());
		assertFalse(h().equals(h("bar")));
	}

}
