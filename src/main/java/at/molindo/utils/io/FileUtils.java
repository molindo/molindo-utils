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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	private FileUtils() {
		throw new IllegalStateException("bad boy!");
	}

	/**
	 * @return extension of file including last '.' or empty string (e.g.
	 *         "foo.tar.gz" => ".gz")
	 */
	public static String extension(final File file) {
		final String fileName = file.getName();
		return fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
	}

	public static InputStream newInputStream(final File file, Compression compression) throws FileNotFoundException, IOException {
		if (Compression.AUTO == compression) {
			compression = compression(file.getName());
		}

		return StreamUtils.compress(new FileInputStream(file), compression) ;	
	}

	/**
	 * @param name name of a file
	 * @return never <code>null</code>, never {@link Compression#AUTO}
	 */
	public static Compression compression(String name) {
		if (name.endsWith(".gz")) return Compression.GZIP;
		if (name.endsWith(".bz2")) return Compression.BZIP2;
		return Compression.NONE;
	}

}
