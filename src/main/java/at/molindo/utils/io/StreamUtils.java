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

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.molindo.thirdparty.org.apache.tools.bzip2.CBZip2InputStream;
import at.molindo.thirdparty.org.apache.tools.bzip2.CBZip2OutputStream;
import at.molindo.utils.collections.ArrayUtils;

public class StreamUtils {

	private static final int DEFAULT_BUFFER = 4096;
	private static Logger log = LoggerFactory.getLogger(StreamUtils.class);

	private StreamUtils() {
	}

	public static String string(InputStream in) throws IOException {
		return string(in, CharsetUtils.UTF_8);
	}

	public static String string(InputStream in, Charset charset) throws IOException {
		return string(in, charset, DEFAULT_BUFFER);
	}

	public static String string(InputStream in, Charset charset, int bufferSize) throws IOException {
		try {
			InputStreamReader reader = new InputStreamReader(in, charset);
			StringWriter writer = new StringWriter();

			int n;
			char[] buffer = new char[bufferSize];
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
			return writer.toString();
		} finally {
			close(in);
		}
	}

	public static int copy(final InputStream in, final OutputStream out) throws IOException {
		return copy(in, out, 4096);
	}

	public static int copy(final InputStream in, final OutputStream out, final int bufferSize) throws IOException {
		final byte[] buffer = new byte[bufferSize];
		int bytesCopied = 0;
		while (true) {
			final int byteCount = in.read(buffer, 0, buffer.length);
			if (byteCount <= 0) {
				break;
			}
			out.write(buffer, 0, byteCount);
			bytesCopied += byteCount;
		}
		return bytesCopied;
	}

	/**
	 * 
	 * @param in
	 * @param compression
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 *             if compression none of {@link Compression#BZIP2},
	 *             {@link Compression#GZIP}, {@link Compression#NONE}
	 */
	public static InputStream decompress(InputStream in, Compression compression) throws IOException {
		switch (compression) {
		case BZIP2:
			return newBz2InputStream(in);
		case GZIP:
			return new GZIPInputStream(in);
		case NONE:
			return in;
		case AUTO:
			throw new IllegalArgumentException("can't guess compression from InputStream");
		default:
			throw new IllegalArgumentException("compression not implemented: " + compression);
		}
	}

	public static OutputStream compress(OutputStream out, Compression compression) throws IOException {
		switch (compression) {
		case BZIP2:
			return newBz2OutputStream(out);
		case GZIP:
			return new GZIPOutputStream(out);
		case NONE:
			return out;
		case AUTO:
			throw new IllegalArgumentException("can't guess compression from InputStream");
		default:
			throw new IllegalArgumentException("compression not implemented: " + compression);
		}
	}

	public static void close(Closeable... in) {
		close(Arrays.asList(in));
	}

	public static void close(Iterable<Closeable> iterable) {
		for (Closeable in : iterable) {
			close(in);
		}
	}

	public static void close(Closeable in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				log.debug("stream close failed", e);
			}
		}
	}

	public static void readFully(InputStream in, byte[] b) throws EOFException, IOException {
		readFully(in, b, 0, b.length);
	}

	public static void readFully(InputStream in, byte[] b, int off, int len) throws EOFException, IOException {
		while (len > 0) {
			int read = in.read(b, off, len);
			if (read == -1) {
				throw new EOFException();
			}
			off += read;
			len -= read;
		}
	}

	public static boolean equals(InputStream i1, InputStream i2) throws IOException {
		return equals(i1, i2, DEFAULT_BUFFER);
	}

	public static boolean equals(InputStream i1, InputStream i2, int buf) throws IOException {
		try {
			// do the compare
			while (true) {
				byte[] b1 = new byte[buf];
				byte[] b2 = new byte[buf];

				int length = i1.read(b1);
				if (length == -1) {
					return i2.read(b2, 0, 1) == -1;
				}

				try {
					StreamUtils.readFully(i2, b2, 0, length);
				} catch (EOFException e) {
					// i2 is shorter than i1
					return false;
				}

				if (!ArrayUtils.equals(b1, b2, 0, length)) {
					return false;
				}
			}
		} finally {
			StreamUtils.close(i1, i2);
		}
	}

	private static InputStream newBz2InputStream(InputStream in) throws IOException {
		if (in.available() > 2) {
			in.mark(2);
			if (in.read() != 'B' || in.read() != 'Z') {
				// "BZ" is used by linux bzip2 utility to mark a bzip2
				// stream
				in.reset();
			}
		}

		in = new CBZip2InputStream(in);
		return in;
	}

	private static OutputStream newBz2OutputStream(OutputStream out) throws IOException {
		// TODO linux compatibility?
		return new CBZip2OutputStream(out);
	}

}