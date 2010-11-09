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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileUtils {

	private FileUtils() {
		throw new IllegalStateException("bad boy!");
	}

	/**
	 * @return normalized (trimmed, lower case) extension of file including last
	 *         '.' or empty string (e.g. "foo.tar.gz" => ".gz")
	 */
	public static String extension(final File file) {
		return extension(file.getName());
	}

	public static String extension(final String fileName) {
		int idx = fileName.lastIndexOf(".");

		return idx == -1 ? "" : fileName.substring(idx, fileName.length()).trim().toLowerCase();
	}

	/**
	 * @return normalized (trimmed, lower case) full extension of file including
	 *         first '.' or empty string (e.g. "foo.tar.gz" => ".tar.gz")
	 */
	public static String extensionFull(final File file) {
		return extensionFull(file.getName());
	}

	public static String extensionFull(final String fileName) {
		int idx = fileName.indexOf(".");

		return idx == -1 ? "" : fileName.substring(idx, fileName.length()).trim().toLowerCase();
	}

	public static InputStream in(final File file, Compression compression) throws FileNotFoundException, IOException {
		if (Compression.AUTO == compression) {
			compression = compression(file.getName());
		}
		return StreamUtils.decompress(new FileInputStream(file), compression);
	}

	public static OutputStream out(final File file, Compression compression) throws FileNotFoundException, IOException {
		if (Compression.AUTO == compression) {
			compression = compression(file.getName());
		}
		return StreamUtils.compress(new FileOutputStream(file), compression);
	}

	public static BufferedReader read(File file, Compression compression) throws FileNotFoundException, IOException {
		return new BufferedReader(new InputStreamReader(in(file, compression), CharsetUtils.UTF_8));
	}

	public static BufferedWriter write(File file, Compression compression) throws FileNotFoundException, IOException {
		return new BufferedWriter(new OutputStreamWriter(out(file, compression), CharsetUtils.UTF_8));
	}

	/**
	 * @param name
	 *            name of a file
	 * @return never <code>null</code>, never {@link Compression#AUTO}
	 */
	public static Compression compression(String name) {
		if (name.endsWith(".gz"))
			return Compression.GZIP;
		if (name.endsWith(".bz2"))
			return Compression.BZIP2;
		return Compression.NONE;
	}

}
