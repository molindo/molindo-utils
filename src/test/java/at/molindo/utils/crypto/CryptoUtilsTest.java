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

public class CryptoUtilsTest {

	@Test
	public void testGetMd5Digest() {
		// expected results computed with MySQL's MD5 implementation
		assertEquals("5f4dcc3b5aa765d61d8327deb882cf99", CryptoUtils.hexDigest("password", DigestAlgorithm.MD5));
		assertEquals("08f8e0260c64418510cefb2b06eee5cd", CryptoUtils.hexDigest("bbb", DigestAlgorithm.MD5));
	}

	@Test
	public void testGetSha256Digest() {
		assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8",
				CryptoUtils.hexDigest("password", DigestAlgorithm.SHA_256));
		assertEquals("3e744b9dc39389baf0c5a0660589b8402f3dbb49b89b3e75f2c9355852a3c677",
				CryptoUtils.hexDigest("bbb", DigestAlgorithm.SHA_256));
	}

	@Test
	public void testGetSha512Digest() {
		assertEquals(
				"b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86",
				CryptoUtils.hexDigest("password", DigestAlgorithm.SHA_512));
		assertEquals(
				"5edc1c6a4390075a3ca27f4d4161c46b374b1c3b2d63f846db6fff0c513203c3ac3b14a24a6f09d8bf21407a4842113b5d9aa359d266299c3d6cf9e92db66dbe",
				CryptoUtils.hexDigest("bbb", DigestAlgorithm.SHA_512));
	}
}
