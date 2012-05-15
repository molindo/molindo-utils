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

public class PrimitiveUtils {

	public static short primitive(Short value) {
		return primitive(value, (short) 0);
	}

	public static short primitive(Short value, int nullValue) {
		return primitive(value, (short) nullValue);
	}

	public static short primitive(Short value, short nullValue) {
		return value == null ? nullValue : value;
	}

	public static int primitive(Integer value) {
		return primitive(value, 0);
	}

	public static int primitive(Integer value, int nullValue) {
		return value == null ? nullValue : value;
	}

	public static long primitive(Long value) {
		return primitive(value, 0L);
	}

	public static long primitive(Long value, long nullValue) {
		return value == null ? nullValue : value;
	}

	public static float primitive(Float value) {
		return primitive(value, 0F);
	}

	public static float primitive(Float value, float nullValue) {
		return value == null ? nullValue : value;
	}

	public static double primitive(Double value) {
		return primitive(value, 0.0);
	}

	public static double primitive(Double value, double nullValue) {
		return value == null ? nullValue : value;
	}

	public static boolean primitive(Boolean value) {
		return primitive(value, false);
	}

	public static boolean primitive(Boolean value, boolean nullValue) {
		return value == null ? nullValue : value;
	}

	public static char primitive(Character value) {
		return primitive(value, '\0');
	}

	public static char primitive(Character value, char nullValue) {
		return value == null ? nullValue : value;
	}

	public static byte primitive(Byte value) {
		return primitive(value, (byte) 0x00);
	}

	public static byte primitive(Byte value, int nullValue) {
		return primitive(value, (byte) nullValue);
	}

	public static byte primitive(Byte value, byte nullValue) {
		return value == null ? nullValue : value;
	}

}