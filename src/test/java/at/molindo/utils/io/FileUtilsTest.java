package at.molindo.utils.io;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileUtilsTest {

	@Test
	public void extension() {
		assertEquals(".gz", FileUtils.extension("foo.TAR.GZ "));
	}
	
	@Test
	public void extensionFull() {
		assertEquals(".tar.gz", FileUtils.extensionFull("foo.TAR.GZ "));
	}
}
