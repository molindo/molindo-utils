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

import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * add class implementing {@link IProxyStripper} to
 * META-INF/services/at.molindo.utils.reflect.IProxyStripper and let
 * implementations detect if a class is proxied
 *
 */
public class ProxyUtil {

	private static final Logger log = LoggerFactory.getLogger(ProxyUtil.class);

	private ProxyUtil() {
	}

	public static Class<?> getClassWithoutProxy(final Object obj) {
		return getClassWithoutProxy(Object.class, obj, null);
	}

	public static Class<?> getClassWithoutProxy(final Object obj, ClassLoader cl) {
		return getClassWithoutProxy(Object.class, obj, cl);
	}

	/**
	 *
	 * @param cls
	 *            optional
	 * @param obj
	 * @return
	 */
	public static <T> Class<? extends T> getClassWithoutProxy(final Class<T> cls, final T obj) {
		return getClassWithoutProxy(cls, obj, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> getClassWithoutProxy(final Class<T> cls, final T obj, ClassLoader cl) {
		if (cls == null) {
			throw new NullPointerException("cls");
		}
		if (obj == null) {
			return null;
		}
		if (cl == null) {
			cl = ClassUtils.getClassLoader();
		}

		if (!cls.isInstance(obj)) {
			throw new IllegalArgumentException("object must be an instance of class " + cls.getName());
		}

		for (final IProxyStripper stripper : ServiceLoader.load(IProxyStripper.class, cl)) {
			final Class<?> stripped = stripper.stripProxyClass(obj);
			if (stripped != null) {
				if (!stripped.isInstance(obj)) {
					log.error("object of type {} isn't an intance of stripped class {}", obj.getClass().getName(),
							stripped.getClass().getName());
				} else if (!cls.isAssignableFrom(stripped)) {
					log.error("class {} isn't assignable from stripped class {}", cls.getName(), stripped.getClass()
							.getName());
				} else {
					return (Class<? extends T>) stripped;
				}
			}
		}

		return (Class<? extends T>) obj.getClass();
	}
}
