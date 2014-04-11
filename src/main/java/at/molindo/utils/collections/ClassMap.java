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
package at.molindo.utils.collections;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A specialized {@link WeakHashMap} that ads the {@link #find(Class)} method to
 * find mappings for superclasses.
 * 
 * @param <V>
 *            value type
 * 
 * @author stf
 */
public class ClassMap<V> extends WeakHashMap<Class<?>, V> {

	/**
	 * @see #ClassMap()
	 */
	public static <V> ClassMap<V> create() {
		return new ClassMap<V>();
	}

	/**
	 * @see #ClassMap(int, float)
	 */
	public static <V> ClassMap<V> create(int initialCapacity, float loadFactor) {
		return new ClassMap<V>(initialCapacity, loadFactor);
	}

	/**
	 * @see #ClassMap(int)
	 */
	public static <V> ClassMap<V> create(int initialCapacity) {
		return new ClassMap<V>(initialCapacity);
	}

	/**
	 * @see #ClassMap(Map)
	 */
	public static <V> ClassMap<V> create(Map<? extends Class<?>, ? extends V> m) {
		return new ClassMap<V>(m);
	}

	/**
	 * @see WeakHashMap#WeakHashMap()
	 */
	public ClassMap() {
		super();
	}

	/**
	 * @see WeakHashMap#WeakHashMap(int, float)
	 */
	public ClassMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * @see WeakHashMap#WeakHashMap(int)
	 */
	public ClassMap(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * @see WeakHashMap#WeakHashMap(Map)
	 */
	public ClassMap(Map<? extends Class<?>, ? extends V> m) {
		super(m);
	}

	/**
	 * @return first mapping for <code>cls</code> or one of its superclasses.
	 */
	public V find(Class<?> cls) {
		if (cls == null) {
			throw new NullPointerException("cls");
		}

		V v;
		do {
			v = get(cls);
			cls = cls.getSuperclass();
		} while (v == null && cls != null);
		return v;
	}

	/**
	 * @return a new map containing all keys that are assignabel from cls
	 */
	public Map<Class<?>, V> findAssignable(Class<?> cls) {
		if (cls == null) {
			throw new NullPointerException("cls");
		}

		Map<Class<?>, V> map = new WeakHashMap<Class<?>, V>();
		for (Map.Entry<Class<?>, V> e : entrySet()) {
			if (cls.isAssignableFrom(e.getKey())) {
				map.put(e.getKey(), e.getValue());
			}
		}
		return map;
	}
}
