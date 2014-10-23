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

package at.molindo.utils.concurrent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.Nonnull;

public abstract class ObjectPool<T> {

	/**
	 * use stack to prefer "hot" objects
	 */
	private final LinkedBlockingDeque<T> _stack;

	/**
	 * create an ObjectPool that uses the default constructor of the passed
	 * class
	 */
	public static <T> ObjectPool<T> create(Class<T> cls, int capacity) {
		try {
			final Constructor<T> constructor = cls.getConstructor();
			return new ObjectPool<T>(capacity) {

				@Override
				protected T create() {
					try {
						return constructor.newInstance();
					} catch (IllegalArgumentException e) {
						throw new RuntimeException("failed to create new instance of type "
								+ constructor.getDeclaringClass().getName(), e);
					} catch (InstantiationException e) {
						throw new RuntimeException("failed to create new instance of type "
								+ constructor.getDeclaringClass().getName(), e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("failed to create new instance of type "
								+ constructor.getDeclaringClass().getName(), e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException("failed to create new instance of type "
								+ constructor.getDeclaringClass().getName(), e);
					}
				}

			};
		} catch (SecurityException e) {
			throw new IllegalArgumentException(cls.getName() + " does not have an accessible default constructor", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(cls.getName() + " does not have a default constructor", e);
		}

	}

	public ObjectPool(int capacity) {
		_stack = new LinkedBlockingDeque<T>(capacity);
	}

	/**
	 * @return object from stack or newly created one
	 */
	public T get() {
		T o = _stack.poll();
		return o == null ? create() : o;
	}

	/**
	 * @param put
	 *            object back on stack
	 */
	public void put(T o) {
		if (o != null && reset(o)) {
			_stack.offerFirst(o);
		}
	}

	/**
	 * @return a newly created object
	 */
	protected abstract @Nonnull
	T create();

	/**
	 * reset an object, e.g. call {@link Collection#clear()}
	 * 
	 * @param object
	 * @return <code>false</code> if object shouldn't be reused
	 */
	protected boolean reset(T object) {
		return true;
	}

	public T refresh(T o) {
		if (o != null && reset(o)) {
			return o;
		} else {
			throw new IllegalStateException("failed to refresh object");
		}
	}
}
