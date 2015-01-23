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

package at.molindo.utils.data;

import static at.molindo.utils.collections.IteratorUtils.transform;
import static at.molindo.utils.data.FunctionUtils.toStringFunction;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testEmpty() {
		assertTrue(StringUtils.empty(null));
		assertTrue(StringUtils.empty(""));
		assertFalse(StringUtils.empty(" "));
		assertFalse(StringUtils.empty("foo"));
	}

	@Test
	public void testTrim() {
		assertEquals(null, StringUtils.trim(null));
		assertEquals("", StringUtils.trim(""));
		assertEquals("", StringUtils.trim(" "));
		assertEquals("foo", StringUtils.trim("foo "));
	}

	@Test
	public void testTrimLeading() {
		assertEquals(null, StringUtils.trimLeading(null));
		assertEquals("", StringUtils.trimLeading(""));
		assertEquals("", StringUtils.trimLeading(" "));
		assertEquals("", StringUtils.trimLeading("  "));
		assertEquals("", StringUtils.trimLeading("   "));
		assertEquals("foo", StringUtils.trimLeading(" foo"));
		assertEquals("foo", StringUtils.trimLeading("  foo"));
		assertEquals("foo", StringUtils.trimLeading("   foo"));
		assertEquals("foo ", StringUtils.trimLeading("foo "));
	}

	@Test
	public void testTrimTrailing() {
		assertEquals(null, StringUtils.trimTrailing(null));
		assertEquals("", StringUtils.trimTrailing(""));
		assertEquals("", StringUtils.trimTrailing(" "));
		assertEquals("", StringUtils.trimTrailing("  "));
		assertEquals("", StringUtils.trimTrailing("   "));
		assertEquals("foo", StringUtils.trimTrailing("foo "));
		assertEquals("foo", StringUtils.trimTrailing("foo  "));
		assertEquals("foo", StringUtils.trimTrailing("foo   "));
		assertEquals(" foo", StringUtils.trimTrailing(" foo"));
	}

	@Test
	public void testSplit() {
		Iterator<String> iter = StringUtils.split("foo\nbar\nbaz", "\n").iterator();

		assertTrue(iter.hasNext());
		assertEquals("foo", iter.next());
		assertTrue(iter.hasNext());
		assertEquals("bar", iter.next());
		assertTrue(iter.hasNext());
		assertEquals("baz", iter.next());
		assertFalse(iter.hasNext());

		iter = StringUtils.split("foo\nbar\n", "\n").iterator();

		assertTrue(iter.hasNext());
		assertEquals("foo", iter.next());
		assertTrue(iter.hasNext());
		assertEquals("bar", iter.next());
		assertTrue(iter.hasNext());
		assertEquals("", iter.next());
		assertFalse(iter.hasNext());

		iter = StringUtils.split("foo;bar;baz;qux;", ";", 3).iterator();

		assertTrue(iter.hasNext());
		assertEquals("foo", iter.next());
		assertTrue(iter.hasNext());
		assertEquals("bar", iter.next());
		assertTrue(iter.hasNext());
		assertEquals("baz;qux;", iter.next());
		assertFalse(iter.hasNext());

		String[] split = new String[5];
		assertEquals(3, StringUtils.split("foo,bar,baz", ",", split));
		assertEquals("foo", split[0]);
		assertEquals("bar", split[1]);
		assertEquals("baz", split[2]);
		assertNull(split[3]);
		assertNull(split[4]);
	}

	@Test
	public void testSub() {
		assertEquals("foobar", StringUtils.sub("foobar", "...", 6));
		assertEquals("fo...", StringUtils.sub("foobar", "...", 5));
		try {
			assertEquals("", StringUtils.sub("foobar", "...", 2));
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testSubPre() {
		assertEquals("foobar", StringUtils.subPre("foobar", "...", 6));
		assertEquals("...ar", StringUtils.subPre("foobar", "...", 5));
		try {
			assertEquals("", StringUtils.subPre("foobar", "...", 2));
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testBeforeLast() {
		assertEquals("fooXYbar", StringUtils.beforeLast("fooXYbarXYbaz", "XY"));
		assertEquals("", StringUtils.beforeLast("fooXbarYbaz", "XY"));
		assertNull(StringUtils.beforeLast(null, "."));
	}

	@Test
	public void testAfterLast() {
		assertEquals("baz", StringUtils.afterLast("fooXYbarXYbaz", "XY"));
		assertEquals("", StringUtils.afterLast("fooXbarYbaz", "XY"));
		assertNull(StringUtils.afterLast(null, "."));
	}

	@Test
	public void testBeforeFirst() {
		assertEquals("foo", StringUtils.beforeFirst("fooXYbarXYbaz", "XY"));
		assertEquals("", StringUtils.beforeFirst("fooXbarYbaz", "XY"));
		assertNull(StringUtils.beforeFirst(null, "."));
	}

	@Test
	public void testAfterFirst() {
		assertEquals("barXYbaz", StringUtils.afterFirst("fooXYbarXYbaz", "XY"));
		assertEquals("", StringUtils.afterFirst("fooXbarYbaz", "XY"));
		assertNull(StringUtils.afterFirst(null, "."));
	}

	@Test
	public void testStartWith() {
		assertEquals("/foo/", StringUtils.startWith("foo/", "/"));
		assertEquals("/foo/", StringUtils.startWith("/foo/", "/"));
		assertEquals("/", StringUtils.startWith("", "/"));
		assertEquals("/", StringUtils.startWith(null, "/"));
	}

	@Test
	public void testEndWith() {
		assertEquals("/foo/", StringUtils.endWith("/foo", "/"));
		assertEquals("/foo/", StringUtils.endWith("/foo/", "/"));
		assertEquals("/", StringUtils.endWith("", "/"));
		assertEquals("/", StringUtils.endWith(null, "/"));
	}

	@Test
	public void testEquals() {
		assertTrue(StringUtils.equals(null, null));
		assertFalse(StringUtils.equals(null, "foo"));
		assertFalse(StringUtils.equals("foo", null));
		assertFalse(StringUtils.equals("foo", "bar"));
		assertTrue(StringUtils.equals("foo", "foo"));
	}

	@Test
	public void testLeading() {
		assertNull(StringUtils.leading(null, "foo"));
		assertEquals("foo", StringUtils.leading("foo", null));
		assertEquals("foobar", StringUtils.leading("bar", "foo"));
		assertEquals("foo", StringUtils.leading("foo", "foo"));
	}

	@Test
	public void testTrailing() {
		assertNull(StringUtils.trailing(null, "foo"));
		assertEquals("foo", StringUtils.trailing("foo", null));
		assertEquals("foobar", StringUtils.trailing("foo", "bar"));
		assertEquals("foo", StringUtils.trailing("foo", "foo"));
	}

	@Test
	public void testStripLeading() {
		assertNull(StringUtils.stripLeading(null, "foo"));
		assertEquals("foo", StringUtils.stripLeading("foo", null));
		assertEquals("bar", StringUtils.stripLeading("foobar", "foo"));
		assertEquals("bar", StringUtils.stripLeading("bar", "foo"));
	}

	@Test
	public void testStripTrailing() {
		assertNull(StringUtils.stripTrailing(null, "foo"));
		assertEquals("foo", StringUtils.stripTrailing("foo", null));
		assertEquals("foo", StringUtils.stripTrailing("foobar", "bar"));
		assertEquals("bar", StringUtils.stripTrailing("bar", "foo"));
	}

	@Test
	public void testPair() {
		assertEquals(Pair.pair("", ""), StringUtils.pair(null, ";"));
		assertEquals(Pair.pair("foo", "bar"), StringUtils.pair("foo:bar", ":"));
		assertEquals(Pair.pair("foobar", ""), StringUtils.pair("foobar", ":"));
		assertEquals(Pair.pair("", "foobar"), StringUtils.pair(":foobar", ":"));
	}

	@Test
	public void testUpper() {
		assertNull(StringUtils.upper(null));
		assertEquals("", StringUtils.upper(""));
		assertEquals("A", StringUtils.upper("a"));
		assertEquals("A", StringUtils.upper("A"));
		assertEquals("FOO", StringUtils.upper("foo"));
		assertEquals("FOO", StringUtils.upper("Foo"));
	}

	@Test
	public void testUpperFirst() {
		assertNull(StringUtils.upperFirst(null));
		assertEquals("", StringUtils.upperFirst(""));
		assertEquals("A", StringUtils.upperFirst("a"));
		assertEquals("A", StringUtils.upperFirst("A"));
		assertEquals("Foo", StringUtils.upperFirst("foo"));
		assertEquals("Foo", StringUtils.upperFirst("Foo"));
	}

	@Test
	public void testLower() {
		assertNull(StringUtils.lower(null));
		assertEquals("", StringUtils.lower(""));
		assertEquals("a", StringUtils.lower("a"));
		assertEquals("a", StringUtils.lower("A"));
		assertEquals("foo", StringUtils.lower("foo"));
		assertEquals("foo", StringUtils.lower("FOO"));
	}

	@Test
	public void testLowerFirst() {
		assertNull(StringUtils.lowerFirst(null));
		assertEquals("", StringUtils.lowerFirst(""));
		assertEquals("a", StringUtils.lowerFirst("a"));
		assertEquals("a", StringUtils.lowerFirst("A"));
		assertEquals("foo", StringUtils.lowerFirst("foo"));
		assertEquals("foo", StringUtils.lowerFirst("Foo"));
	}

	@Test
	public void testJoin() {
		assertEquals("", StringUtils.join(", ", (Collection<String>) null));
		assertEquals("", StringUtils.join(", "));
		assertEquals("foo", StringUtils.join(", ", "foo"));
		assertEquals("foo, bar", StringUtils.join(", ", "foo", "bar"));
		assertEquals("foo, bar", StringUtils.join(", ", "foo", null, "bar"));
		assertEquals("foo, bar", StringUtils.join(", ", "foo", "bar", ""));
		assertEquals("foo- -bar", StringUtils.join("-", "foo", " ", "bar"));
		assertEquals("1-2-3", StringUtils.join("-", transform(asList(1, 2, 3), toStringFunction()))); // compiles?
	}
}
