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
package at.molindo.utils.reflect;

import static at.molindo.utils.io.StreamUtils.close;
import static at.molindo.utils.reflect.AssociatedFileLoader.load;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;

import org.junit.Test;

import at.molindo.utils.reflect.AssociatedFileLoader.AssociatedFile;

public class AssociatedFileLoaderTest {

	@Test
	public void testLoad() {
		AssociatedFile file = load(ClassUtilsTest.class, ".txt");
		assertNotNull(file);

		assertEquals(ClassUtilsTest.class, file.getOwner());
		assertEquals("ClassUtilsTest.txt", file.getName());
		assertEquals("at/molindo/utils/reflect/ClassUtilsTest.txt", file.getPath());
		assertNull(load(ClassUtilsTest.class, ".xml"));
	}

	@Test
	public void testOpen() {
		AssociatedFile file = load(ClassUtilsTest.class, ".txt");
		assertNotNull(file);

		InputStream txt = file.open();
		try {
			assertNotNull(txt);
		} finally {
			close(txt);
		}
	}
}
