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

public class NumberUtils {

	private NumberUtils() {
	}

	public static short checkNull(Short n) {
		return n == null ? 0 : n;
	}

	public static int checkNull(Integer n) {
		return n == null ? 0 : n;
	}

	public static long checkNull(Long n) {
		return n == null ? 0 : n;
	}

	public static float checkNull(Float n) {
		return n == null ? 0 : n;
	}

	public static double checkNull(Double n) {
		return n == null ? 0 : n;
	}

	public static short toShort(long n) {
		if (n < Short.MIN_VALUE || Short.MAX_VALUE < n) {
			throw new IllegalArgumentException("value out of range: " + n);
		}
		return (short) n;
	}

	public static short toShort(int n) {
		if (n < Short.MIN_VALUE || Short.MAX_VALUE < n) {
			throw new IllegalArgumentException("value out of range: " + n);
		}
		return (short) n;
	}

	public static int toInteger(long n) {
		if (n < Integer.MIN_VALUE || Integer.MAX_VALUE < n) {
			throw new IllegalArgumentException("value out of range: " + n);
		}
		return (int) n;
	}

	public static Short toShort(Long n) {
		return n == null ? null : toShort(n.longValue());
	}

	public static Short toShort(Integer n) {
		return n == null ? null : toShort(n.intValue());
	}

	public static Integer toInteger(Long n) {
		return n == null ? null : toInteger(n.longValue());
	}

	public static Integer toInteger(Short n) {
		return n == null ? null : n.intValue();
	}

	public static Long toLong(Short n) {
		return n == null ? null : n.longValue();
	}

	public static Long toLong(Integer n) {
		return n == null ? null : n.longValue();
	}
}
