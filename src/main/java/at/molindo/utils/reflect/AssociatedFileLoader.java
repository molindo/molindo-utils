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
package at.molindo.utils.reflect;

import java.io.InputStream;
import java.util.Iterator;
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

	public static AssociatedFile load(final Class<?> clazz, final String fileExtension, final Locale locale) {
		return load(clazz, null, suffix(fileExtension, locale));
	}

	public static AssociatedFile load(final Class<?> clazz, final Class<?> baseClass, final String fileExtension,
			final Locale locale) {
		return load(clazz, baseClass, suffix(fileExtension, locale));
	}

	public static AssociatedFile load(final Class<?> clazz, final String suffix) {
		return load(clazz, null, suffix);
	}

	public static AssociatedFile load(final Class<?> clazz, Class<?> baseClass, String suffix) {
		if (baseClass == null) {
			baseClass = Object.class;
		}

		Iterator<Class<?>> iter = ClassUtils.hierarchy(clazz);

		Class<?> c;
		do {
			c = iter.next();

			String template = c.getSimpleName() + suffix;
			if (ClassUtils.getClasspathResource(c, template) != null) {
				return new AssociatedFile(c, template);
			}

		} while (c != baseClass);

		return null;
	}

	private static String suffix(final String fileExtension, final Locale locale) {
		final StringBuilder buf = new StringBuilder();
		if (locale != null) {
			buf.append("_").append(locale.getLanguage());
		}
		if (fileExtension != null) {
			if (!fileExtension.startsWith(".")) {
				buf.append(".");
			}
			buf.append(fileExtension);
		}
		String suffix = buf.toString();
		return suffix;
	}

	public static final class AssociatedFile {

		private final Class<?> _owner;
		private final String _name;

		private AssociatedFile(Class<?> owner, String name) {
			if (owner == null) {
				throw new NullPointerException("owner");
			}
			if (name == null) {
				throw new NullPointerException("name");
			}
			_owner = owner;
			_name = name;
		}

		public Class<?> getOwner() {
			return _owner;
		}

		public String getName() {
			return _name;
		}

		public String getPath() {
			return ClassUtils.getPackageResourcePath(_owner, _name);
		}

		public InputStream open() {
			InputStream in = ClassUtils.getClasspathResourceAsStream(_owner, _name);
			if (in == null) {
				throw new IllegalStateException("AssociatedFile not available: " + getPath());
			}
			return in;
		}

	}
}
