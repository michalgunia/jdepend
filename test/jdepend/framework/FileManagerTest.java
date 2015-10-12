package jdepend.framework;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class FileManagerTest extends JDependTestCase {

	private FileManager fileManager;
	
	@Before
	public void setUp() {
		super.setUp();
		fileManager = new FileManager();
		fileManager.acceptInnerClasses(false);
	}
	
	@After
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testEmptyFileManager() {
		assertEquals(0, fileManager.extractFiles().size());
	}

	@Test
	public void testBuildDirectory() throws IOException {
		fileManager.addDirectory(getBuildDir());
		fileManager.addDirectory(getTestBuildDir());
		assertEquals(51, fileManager.extractFiles().size());
	}

	@Test(expected = IOException.class)
	public void testNonExistentDirectory() throws IOException {
		fileManager.addDirectory(getBuildDir() + "junk");
		fail("Non-existent directory: Should raise IOException");

	}

	@Test
	public void testInvalidDirectory() {

		String file = getTestDir() + getPackageSubDir() + "ExampleTest.java";

		try {

			fileManager.addDirectory(file);
			fail("Invalid directory: Should raise IOException");

		} catch (IOException expected) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testClassFile() throws IOException {

		File f = new File(getBuildDir() + getPackageSubDir() + "JDepend.class");

		assertEquals(true, new FileManager().acceptClassFile(f));
	}
	
	@Test
	public void testNonExistentClassFile() {
		File f = new File(getBuildDir() + "JDepend.class");
		assertEquals(false, new FileManager().acceptClassFile(f));
	}
	
	@Test
	public void testInvalidClassFile() {
		File f = new File(getHomeDir() + "build.xml");
		assertEquals(false, new FileManager().acceptClassFile(f));
	}
	
	@Test
	public void testJar() throws IOException {
		File f = File.createTempFile("bogus", ".jar", new File(getTestDataDir()));
		fileManager.addDirectory(f.getPath());
		f.deleteOnExit();
	}
	
	@Test
	public void testZip() throws IOException {
		File f = File.createTempFile("bogus", ".zip", new File(getTestDataDir()));
		fileManager.addDirectory(f.getPath());
		f.deleteOnExit();
	}
	
	@Test
	public void testWar() throws IOException {
		File f = File.createTempFile("bogus", ".war", new File(getTestDataDir()));
		fileManager.addDirectory(f.getPath());
		f.deleteOnExit();
	}
}