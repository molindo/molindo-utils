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

public abstract class HexUtils {
	private HexUtils() {
	}

	/**
	 * transforms a byte array into a hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String string(final byte[] bytes) {
		final StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (final byte b : bytes) {
			final String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				buf.append("0");
			}
			buf.append(hex);
		}
		return buf.toString();
	}

	/**
	 * transform an array of integers into a hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public static String string(final int... bytes) {
		return string(ConversionUtils.bytes(bytes));
	}

	/**
	 * transform a hex string into a byte array
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] bytes(final String hex) {
		final byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
		}
		return bytes;
	}

	public static String pad(String hex, int bytes) {
		return StringUtils.padLeft(hex, "0", bytes * 2);
	}

}
