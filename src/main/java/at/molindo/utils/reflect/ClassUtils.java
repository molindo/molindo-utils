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

import java.lang.reflect.Type;
import java.util.Set;

import at.molindo.thirdparty.org.springframework.core.GenericTypeResolver;
import at.molindo.utils.collections.ArrayUtils;

public class ClassUtils {
	private ClassUtils() {
	}

	/**
	 * @param type
	 * @param genericCls
	 * @return first actual type arguments of gernicCls in type hierarchy of cls
	 */
	public static Class<?> getTypeArgument(Class<?> cls, Class<?> genericCls) {
		return ArrayUtils.first(getTypeArguments(cls, genericCls));
	}

	/**
	 * @param type
	 * @param genericCls
	 * @return actual type arguments of gernicCls in type hierarchy of cls
	 */
	public static Class<?>[] getTypeArguments(Class<?> cls, Class<?> genericCls) {
		return GenericTypeResolver.resolveTypeArguments(cls, genericCls);
	}

	public static Class<?> toClass(Class<?> declaringCls, Type type) {
		return GenericTypeResolver.extractClass(declaringCls, type);
	}

	/**
	 * @return <code>true</code> if <code>cls</code> is assignable to at least
	 *         one class in <code>classes</code>
	 */
	public static boolean isAssignable(Class<?> cls, Set<Class<?>> classes) {
		if (cls == null || classes.isEmpty()) {
			return false;
		}
		for (Class<?> c : classes) {
			if (c.isAssignableFrom(cls)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return <code>true</code> if <code>cls</code> is assignable to all
	 *         <code>classes</code>
	 */
	public static boolean isAssignableToAll(Class<?> cls, Set<Class<?>> classes) {
		if (cls == null) {
			return false;
		}
		for (Class<?> c : classes) {
			if (!c.isAssignableFrom(cls)) {
				return false;
			}
		}
		return true;
	}
}
