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
			compression = compression(file);
		}
		return StreamUtils.decompress(new FileInputStream(file), compression);
	}

	public static OutputStream out(final File file, Compression compression) throws FileNotFoundException, IOException {
		if (Compression.AUTO == compression) {
			compression = compression(file);
		}
		return StreamUtils.compress(new FileOutputStream(file), compression);
	}

	public static BufferedReader read(File file, Compression compression) throws FileNotFoundException, IOException {
		return new BufferedReader(new InputStreamReader(in(file, compression), CharsetUtils.UTF_8));
	}

	public static BufferedWriter write(File file, Compression compression) throws FileNotFoundException, IOException {
		return new BufferedWriter(new OutputStreamWriter(out(file, compression), CharsetUtils.UTF_8));
	}

	public static void write(File file, Compression compression, String string) throws FileNotFoundException,
			IOException {
		BufferedWriter w = write(file, compression);
		w.write(string);
		StreamUtils.close(w);
	}

	public static byte[] bytes(File file) throws FileNotFoundException, IOException {
		return bytes(file, compression(file));
	}

	public static byte[] bytes(File file, Compression compression) throws FileNotFoundException, IOException {
		InputStream in = FileUtils.in(file, compression);
		try {
			return StreamUtils.bytes(in);
		} finally {
			StreamUtils.close(in);
		}
	}

	public static Compression compression(File file) {
		return compression(file.getName());
	}

	/**
	 * @param name
	 *            name of a file
	 * @return never <code>null</code>, never {@link Compression#AUTO}
	 */
	public static Compression compression(String name) {
		if (name.endsWith(".gz")) {
			return Compression.GZIP;
		}
		if (name.endsWith(".bz2")) {
			return Compression.BZIP2;
		}
		return Compression.NONE;
	}

	/**
	 * deletes files and directories
	 * 
	 * @return true if file does not exist (anymore)
	 */
	public static boolean delete(File file) {
		if (file != null && file.exists()) {
			if (file.isDirectory()) {
				for (File child : file.listFiles()) {
					delete(child);
				}
			}
			return file.delete();
		} else {
			return true;
		}
	}

	/**
	 * deletes files and directories inside a directory
	 * 
	 * @return true if directory exists and is empty
	 */
	public static boolean deleteContents(File file) {
		if (file != null && file.isDirectory()) {
			boolean empty = true;
			for (File child : file.listFiles()) {
				empty &= delete(child);
			}
			return empty;
		} else {
			return false;
		}
	}

	/**
	 * creates directory if it does not exist and returns true if exists now
	 * 
	 * @param assetDirectory
	 * @return true if d is a directory
	 */
	public static boolean mkdir(File d) {
		d.mkdir();
		return d.isDirectory();
	}

	/**
	 * creates directory if it does not exist and returns true if exists now
	 * 
	 * @param assetDirectory
	 * @return true if d is a directory
	 */
	public static boolean mkdirs(File d) {
		d.mkdirs();
		return d.isDirectory();
	}
}
