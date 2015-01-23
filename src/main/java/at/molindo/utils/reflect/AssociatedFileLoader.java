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

import java.io.InputStream;
import java.util.Locale;

/**
 * looks for files next to a certain class, e.g. at/molindo/Foo for
 * at.molindo.Foo
 * 
 * file extension and language (locale) are optional, e.g. at/molindo/Foo_de.txt
 */
public class AssociatedFileLoader {

	private AssociatedFileLoader() {
		// no hay objetos
	}

	private static String find(final Class<?> clazz, Class<?> baseClass, final String fileExtension, final Locale locale) {

		if (baseClass == null) {
			baseClass = Object.class;
		}

		String template;

		Class<?> c = clazz;
		final StringBuilder buf = new StringBuilder();
		boolean exists = false;
		do {
			buf.append(c.getSimpleName().replace(".", "/"));
			if (locale != null) {
				buf.append("_").append(locale.getLanguage());
			}
			if (fileExtension != null) {
				if (!fileExtension.startsWith(".")) {
					buf.append(".");
				}
				buf.append(fileExtension);
			}
			template = buf.toString();
			exists = ClassUtils.getClasspathResource(c, template) != null;

			// if (!exists) {
			buf.setLength(0);
			c = c.getSuperclass();

		} while (!exists && c != baseClass);

		if (exists) {
			return template;
		}
		return null;
	}

	public static InputStream load(final Class<?> clazz, final String fileExtension) {
		return load(clazz, null, fileExtension, null);
	}

	public static InputStream load(final Class<?> clazz, final String fileExtension, final Locale locale) {
		return load(clazz, null, fileExtension, locale);
	}

	public static InputStream load(final Class<?> clazz, final Class<?> baseClass, final String fileExtension,
			final Locale locale) {
		final String name = find(clazz, baseClass, fileExtension, locale);
		if (name == null) {
			return null;
		}
		return ClassUtils.getClasspathResourceAsStream(clazz, name);
	}

	public static boolean exists(final Class<?> clazz, final String fileExtension) {
		return exists(clazz, null, fileExtension, null);
	}

	public static boolean exists(final Class<?> clazz, final String fileExtension, final Locale locale) {
		return exists(clazz, null, fileExtension, locale);
	}

	public static boolean exists(final Class<?> clazz, final Class<?> baseClass, final String fileExtension,
			final Locale locale) {
		return find(clazz, baseClass, fileExtension, locale) != null;
	}
}
