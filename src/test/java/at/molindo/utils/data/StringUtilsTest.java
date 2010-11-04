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

}
