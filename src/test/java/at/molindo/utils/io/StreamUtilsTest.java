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

package at.molindo.utils.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import at.molindo.utils.collections.ArrayUtils;
import at.molindo.utils.data.HexUtils;

public class StreamUtilsTest {

	@Test
	public void testString() throws IOException {
		String test = "this is a test";
		Charset charset = CharsetUtils.US_ASCII;

		ByteArrayInputStream in = new ByteArrayInputStream(test.getBytes(charset));
		StreamUtils.close(in);

		assertEquals(test, StreamUtils.string(in, charset, 4));
	}

	@Test
	public void testCopy() throws IOException {
		String test = "this is a test";
		Charset charset = CharsetUtils.US_ASCII;
		byte[] bytes = test.getBytes(charset);

		ByteArrayInputStream in = new ByteArrayInputStream(test.getBytes(charset));
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		assertEquals(bytes.length, StreamUtils.copy(in, out, 4));
		StreamUtils.close(in, out);

		assertArrayEquals(bytes, out.toByteArray());
	}

	@Test
	public void testReadFully() throws IOException {
		byte[] bytes = HexUtils.bytes("1234567890abcdef1234567890abcdef");
		assertEquals(16, bytes.length);

		byte[] buf = new byte[10];

		InputStream in = new ByteArrayInputStream(bytes);

		StreamUtils.readFully(in, buf);

		assertTrue(ArrayUtils.equals(bytes, buf, 0, buf.length));

		StreamUtils.close(in);
	}

	@Test(expected = EOFException.class)
	public void testReadFullyEOF() throws IOException {
		byte[] bytes = HexUtils.bytes("1234567890abcdef1234567890abcdef");
		assertEquals(16, bytes.length);

		byte[] buf = new byte[20];

		InputStream in = new ByteArrayInputStream(bytes);

		StreamUtils.readFully(in, buf);
		StreamUtils.close(in);
	}

	public void testEquals() throws IOException {
		assertTrue(StreamUtils.equals(stream("abcd"), stream("abcd")));
		assertTrue(!StreamUtils.equals(stream("abcd"), stream("ab01")));
		assertTrue(!StreamUtils.equals(stream("abcd"), stream("ab")));
		assertTrue(!StreamUtils.equals(stream("ab"), stream("abcd")));
	}

	private static ByteArrayInputStream stream(String hex) {
		return new ByteArrayInputStream(HexUtils.bytes(hex));
	}
}
