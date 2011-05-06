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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClassUtilsTest {

	@Test
	public void getTypeArgument() {
		assertEquals(String.class, ClassUtils.getTypeArgument(Bar.class, Doable.class));
		assertEquals(String.class, ClassUtils.getTypeArgument(Baz.class, Doable.class));
		assertEquals(Integer.class, ClassUtils.getTypeArgument(Baz.class, Foo.class));
	}

	public interface Doable<T> {
		T doSomething();
	}

	public interface VeryDoable extends Doable<String> {
	}

	public abstract static class Foo<T> {
		abstract T foo();
	}

	public static class Bar extends Foo<Integer> implements VeryDoable {

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
