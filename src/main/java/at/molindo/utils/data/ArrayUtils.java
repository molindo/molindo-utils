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

public class ArrayUtils {
	public static boolean equals(byte[] a, byte[] a2, int off, int len) {
		if (off < 0 || len < 0 || len > a.length - off || len > a2.length - off) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return true;
		}

		if (a == a2) {
			return true;
		}
		if (a == null || a2 == null) {
			return false;
		}

		for (int i = off; i < off + len; i++) {
			if (a[i] != a2[i]) {
				return false;
			}
		}

		return true;
	}
}
