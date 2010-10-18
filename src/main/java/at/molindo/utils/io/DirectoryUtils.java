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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DirectoryUtils {
	private DirectoryUtils() {
		
	}
	
	public static Map<File, InputStream> open(File directory, boolean recursive) throws FileNotFoundException, IOException {
		return open(directory, Compression.NONE, recursive);
	}
	
	public static Map<File, InputStream> open(File directory, Compression compression, boolean recursive) throws FileNotFoundException, IOException {
		Map<File, InputStream> map = new HashMap<File, InputStream>();
		
		if (!directory.isDirectory()) {
			map.put(directory, FileUtils.newInputStream(directory, compression));
		} else {
			for (File f : directory.listFiles()) {
				if (!f.isDirectory()) {
					map.put(f, FileUtils.newInputStream(directory, compression));
				} else if (recursive) {
					open(f, compression, true);
				}
			}
		}
		
		return map;
	}
}
