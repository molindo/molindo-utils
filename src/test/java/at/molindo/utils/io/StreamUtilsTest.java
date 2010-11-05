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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

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

}
