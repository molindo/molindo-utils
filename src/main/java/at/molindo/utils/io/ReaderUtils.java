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
package at.molindo.utils.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

public class ReaderUtils {

	private static final int BUFFER_SIZE = 1024;

	private ReaderUtils() {
	}

	public static String text(Reader r) throws IOException {

		try {
			int c;
			char[] chars = new char[BUFFER_SIZE];
			StringBuilder buf = new StringBuilder();

			while ((c = r.read(chars)) > 0) {
				buf.append(chars, 0, c);
			}
			return buf.toString();
		} finally {
			close(r);
		}
	}

	public static void close(Closeable... in) {
		StreamUtils.close(in);
	}

	public static void close(Iterable<Closeable> iterable) {
		StreamUtils.close(iterable);
	}

	public static void close(Closeable in) {
		StreamUtils.close(in);
	}
}
