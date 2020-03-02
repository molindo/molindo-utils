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
package at.molindo.utils.crypto;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import at.molindo.utils.data.HexUtils;
import at.molindo.utils.io.CharsetUtils;

public abstract class CryptoUtils {

	private CryptoUtils() {
		// no instances please
	}

	public static byte[] digest(final byte[] input, final IDigestAlgorithm algorithm) {
		return algorithm.newDigest().add(input).digest();
	}

	public static byte[] digest(final ByteBuffer input, final IDigestAlgorithm algorithm) {
		return algorithm.newDigest().add(input).digest();
	}

	public static byte[] digest(final String input, Charset charset, final IDigestAlgorithm algorithm) {
		return digest(input.getBytes(charset), algorithm);
	}

	public static byte[] digest(final String input, final IDigestAlgorithm algorithm) {
		return digest(input.getBytes(CharsetUtils.UTF_8), algorithm);
	}

	public static String hexDigest(final ByteBuffer input, final IDigestAlgorithm algorithm) {
		return HexUtils.string(digest(input, algorithm));
	}

	public static String hexDigest(final byte[] input, final IDigestAlgorithm algorithm) {
		return HexUtils.string(digest(input, algorithm));
	}

	public static String hexDigest(final String input, Charset charset, final IDigestAlgorithm algorithm) {
		return HexUtils.string(digest(input, charset, algorithm));
	}

	public static String hexDigest(final String input, final IDigestAlgorithm algorithm) {
		return HexUtils.string(digest(input, algorithm));
	}

	public static boolean validate(final ByteBuffer input, byte[] digest, final IDigestAlgorithm algorithm) {
		return Arrays.equals(digest(input, algorithm), digest);
	}

	public static boolean validate(final byte[] input, byte[] digest, final IDigestAlgorithm algorithm) {
		return Arrays.equals(digest(input, algorithm), digest);
	}

	public static boolean validate(final String input, Charset charset, byte[] digest, final IDigestAlgorithm algorithm) {
		return Arrays.equals(digest(input, charset, algorithm), digest);
	}

	public static boolean validate(final String input, byte[] digest, final IDigestAlgorithm algorithm) {
		return Arrays.equals(digest(input, algorithm), digest);
	}

	public static boolean validate(final ByteBuffer input, String digest, final IDigestAlgorithm algorithm) {
		return validate(input, HexUtils.bytes(digest), algorithm);
	}

	public static boolean validate(final byte[] input, String digest, final IDigestAlgorithm algorithm) {
		return validate(input, HexUtils.bytes(digest), algorithm);
	}

	public static boolean validate(final String input, Charset charset, String digest, final IDigestAlgorithm algorithm) {
		return validate(input, charset, HexUtils.bytes(digest), algorithm);
	}

	public static boolean validate(final String input, String digest, final IDigestAlgorithm algorithm) {
		return validate(input, HexUtils.bytes(digest), algorithm);
	}

}
