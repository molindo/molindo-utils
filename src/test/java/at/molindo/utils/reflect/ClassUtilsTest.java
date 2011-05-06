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

	public abstract static class Foo<T> {
		abstract T foo();
	}

	public static class Bar extends Foo<Integer> implements Doable<String> {

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
