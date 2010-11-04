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

import static org.junit.Assert.*;

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
}
