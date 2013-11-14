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
package at.molindo.utils.crypto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DigestAlgorithmTest {

	public DigestAlgorithmTest() {
	}

	@Test
	public void testMd5() {
		String md5 = DigestAlgorithm.MD5.newDigest().add("foobar").digestHex();
		assertEquals("3858f62230ac3c915f300c664312c63f", md5);

		md5 = DigestAlgorithm.MD5.newDigest().add("foo").add("bar").digestHex();
		assertEquals("3858f62230ac3c915f300c664312c63f", md5);

	}

}
