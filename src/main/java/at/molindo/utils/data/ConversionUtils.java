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

public class ConversionUtils {
	private ConversionUtils() {

	}

	public static byte[] bytes(final int... ints) {
		byte[] b = new byte[ints.length * 4];
		for (int i = 0; i < ints.length; i++) {
			int offset = i * 4;
			b[offset] = (byte) (ints[i] >> 24);
			b[offset + 1] = (byte) (ints[i] >> 16);
			b[offset + 2] = (byte) (ints[i] >> 8);
			b[offset + 3] = (byte) (ints[i]);
		}
		return b;
	}

	public static final int[] ints(final byte... b) {
		if (b.length % 4 != 0) {
			throw new IllegalArgumentException("array length must be a multiple of 4, was " + b.length);
		}

		int[] ints = new int[b.length / 4];

		for (int i = 0; i < ints.length; i++) {
			int offset = i * 4;
			ints[i] = (b[offset + 0] << 24) + ((b[offset + 1] & 0xFF) << 16) + ((b[offset + 2] & 0xFF) << 8)
					+ (b[offset + 3] & 0xFF);
		}

		return ints;
	}
}
