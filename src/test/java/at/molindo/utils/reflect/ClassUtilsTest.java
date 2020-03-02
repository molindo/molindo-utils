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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

import at.molindo.utils.collections.CollectionUtils;
import at.molindo.utils.data.StringUtils;
import at.molindo.utils.io.StreamUtils;

public class ClassUtilsTest {

	private static final String TEST_RESOURCE = "ClassUtilsTest.txt";

	@Test
	public void getTypeArgument() {
		assertEquals(String.class, ClassUtils.getTypeArgument(Bar.class, Doable.class));
		assertEquals(String.class, ClassUtils.getTypeArgument(Baz.class, Doable.class));
		assertEquals(Integer.class, ClassUtils.getTypeArgument(Baz.class, Foo.class));
	}

	@Test
	public void isAssignable() {
		assertTrue(ClassUtils.isAssignable(String.class, set(Comparable.class, Integer.class)));
		assertFalse(ClassUtils.isAssignable(null, set(Comparable.class, Integer.class)));
		assertFalse(ClassUtils.isAssignable(String.class, set(Long.class, Integer.class)));
		assertFalse(ClassUtils.isAssignable(String.class, set()));

		assertFalse(ClassUtils.isAssignableToAll(String.class, set(Comparable.class, Integer.class)));
		assertTrue(ClassUtils.isAssignableToAll(String.class, set(Comparable.class, Serializable.class)));
		assertFalse(ClassUtils.isAssignableToAll(null, set(Comparable.class, Integer.class)));
		assertTrue(ClassUtils.isAssignableToAll(String.class, set()));
	}

	@Test
	public void getPackageResourcePath() {
		assertEquals("at/molindo/utils/reflect/ClassUtilsTest.txt",
				ClassUtils.getPackageResourcePath(this.getClass(), TEST_RESOURCE));
	}

	@Test
	public void getClasspathResource() {
		URL url = ClassUtils.getClasspathResource(this.getClass(), TEST_RESOURCE);
		assertNotNull("resource not found", url);
	}

	@Test
	public void getClasspathResources() throws IOException {
		Enumeration<URL> urls = ClassUtils.getClasspathResources(this.getClass(), TEST_RESOURCE);
		assertNotNull("resource not found", urls);
		assertTrue(urls.hasMoreElements());

		URL url = urls.nextElement();
		assertNotNull(url);

		assertFalse(urls.hasMoreElements());

	}

	@Test
	public void getClasspathResourceAsStream() throws IOException {
		InputStream in = ClassUtils.getClasspathResourceAsStream(this.getClass(), TEST_RESOURCE);
		assertNotNull("resource not found", in);
		assertTrue(!StringUtils.empty(StreamUtils.string(in)));
	}

	protected Set<Class<?>> set(Class<?>... classes) {
		return CollectionUtils.set(classes);
	}

	@Test
	public void testHierarchy() {
		Iterator<Class<?>> iter = ClassUtils.hierarchy(Integer.class);
		assertNotNull(iter);
		assertTrue(iter.hasNext());

		assertSame(Integer.class, iter.next());
		assertTrue(iter.hasNext());

		try {
			iter.remove();
			fail();
		} catch (UnsupportedOperationException e) {
			// expected
		}

		assertSame(Number.class, iter.next());
		assertTrue(iter.hasNext());
		assertSame(Object.class, iter.next());
		assertFalse(iter.hasNext());

		try {
			iter.next();
			fail();
		} catch (NoSuchElementException e) {
			// expected
		}
	}

	public interface Doable<T> {
		T doSomething();
	}

	public interface VeryDoable<V> extends Doable<V> {
	}

	public abstract static class Foo<T> {
		abstract T foo();
	}

	public static class Bar extends Foo<Integer> implements VeryDoable<String> {

		@Override
		Integer foo() {
			return 42;
		}

		@Override
		public String doSomething() {
			return "bar";
		}

	}

	public static class Baz extends Bar {
	}

}
