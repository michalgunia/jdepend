package jdepend.framework;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class ClassFileParserTest extends JDependTestCase {

    private ClassFileParser parser;

 
    @Before
    public void setUp() {
        super.setUp();
        PackageFilter filter = PackageFilter.all();
        parser = new ClassFileParser(filter);
    }

    @After
    public void tearDown() {
        super.tearDown();
    }
    
    @Test
    public void testInvalidClassFile() {
        File f = new File(getTestDir() + getPackageSubDir() + "ExampleTest.java");

        try {
            parser.parse(f);
            fail("Invalid class file: Should raise IOException");
        } catch (IOException expected) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testInterfaceClass() throws IOException {
        File f = new File(getTestBuildDir() + getPackageSubDir() + "ExampleInterface.class");

        JavaClass clazz = parser.parse(f);

        assertTrue(clazz.isAbstract());

        assertEquals("jdepend.framework.ExampleInterface", clazz.getName());

        assertEquals("ExampleInterface.java", clazz.getSourceFile());

        Collection imports = clazz.getImportedPackages();
        assertEquals(6, imports.size());

        assertTrue(imports.contains(new JavaPackage("java.math")));
        assertTrue(imports.contains(new JavaPackage("java.text")));
        assertTrue(imports.contains(new JavaPackage("java.lang")));
        assertTrue(imports.contains(new JavaPackage("java.io")));
        assertTrue(imports.contains(new JavaPackage("java.rmi")));
        assertTrue(imports.contains(new JavaPackage("java.util")));
    }
    
    @Test
    public void testAbstractClass() throws IOException {

        File f = new File(getTestBuildDir() + getPackageSubDir() + "ExampleAbstractClass.class");

        JavaClass clazz = parser.parse(f);

        assertTrue(clazz.isAbstract());

        assertEquals("jdepend.framework.ExampleAbstractClass", clazz.getName());

        assertEquals("ExampleAbstractClass.java", clazz.getSourceFile());

        Collection imports = clazz.getImportedPackages();
        assertEquals(7, imports.size());

        assertTrue(imports.contains(new JavaPackage("java.math")));
        assertTrue(imports.contains(new JavaPackage("java.text")));
        assertTrue(imports.contains(new JavaPackage("java.lang")));
        assertTrue(imports.contains(new JavaPackage("java.lang.reflect")));
        assertTrue(imports.contains(new JavaPackage("java.io")));
        assertTrue(imports.contains(new JavaPackage("java.rmi")));
        assertTrue(imports.contains(new JavaPackage("java.util")));
    }
    
    @Test
    public void testConcreteClass() throws IOException {

        File f = new File(getTestBuildDir() + getPackageSubDir() + "ExampleConcreteClass.class");

        JavaClass clazz = parser.parse(f);

        assertFalse(clazz.isAbstract());

        assertEquals("jdepend.framework.ExampleConcreteClass", clazz.getName());

        assertEquals("ExampleConcreteClass.java", clazz.getSourceFile());

        Collection imports = clazz.getImportedPackages();
        assertEquals(19, imports.size());

        assertTrue(imports.contains(new JavaPackage("java.net")));
        assertTrue(imports.contains(new JavaPackage("java.text")));
        assertTrue(imports.contains(new JavaPackage("java.sql")));
        assertTrue(imports.contains(new JavaPackage("java.lang")));
        assertTrue(imports.contains(new JavaPackage("java.io")));
        assertTrue(imports.contains(new JavaPackage("java.rmi")));
        assertTrue(imports.contains(new JavaPackage("java.util")));
        assertTrue(imports.contains(new JavaPackage("java.util.jar")));
        assertTrue(imports.contains(new JavaPackage("java.math")));

        // annotations
        assertTrue(imports.contains(new JavaPackage("org.junit.runners")));
        assertTrue(imports.contains(new JavaPackage("java.applet")));
        assertTrue(imports.contains(new JavaPackage("org.junit")));
        assertTrue(imports.contains(new JavaPackage("javax.crypto")));
        assertTrue(imports.contains(new JavaPackage("java.awt.geom")));
        assertTrue(imports.contains(new JavaPackage("java.awt.image.renderable")));
        assertTrue(imports.contains(new JavaPackage("jdepend.framework.p1")));
        assertTrue(imports.contains(new JavaPackage("jdepend.framework.p2")));
        assertTrue(imports.contains(new JavaPackage("java.awt.im")));
        assertTrue(imports.contains(new JavaPackage("java.awt.dnd.peer")));
    }
    
    @Test
    public void testInnerClass() throws IOException {

        File f = new File(getTestBuildDir() + getPackageSubDir() +
                "ExampleConcreteClass$ExampleInnerClass.class");

        JavaClass clazz = parser.parse(f);

        assertFalse(clazz.isAbstract());

        assertEquals("jdepend.framework.ExampleConcreteClass$ExampleInnerClass",
                clazz.getName());

        assertEquals("ExampleConcreteClass.java", clazz.getSourceFile());

        Collection imports = clazz.getImportedPackages();
        assertEquals(1, imports.size());

        assertTrue(imports.contains(new JavaPackage("java.lang")));

    }
    
    @Test
    public void testPackageClass() throws IOException {

        File f = new File(getTestBuildDir() + getPackageSubDir() +
                "ExamplePackageClass.class");

        JavaClass clazz = parser.parse(f);

        assertFalse(clazz.isAbstract());

        assertEquals("jdepend.framework.ExamplePackageClass", clazz.getName());

        assertEquals("ExampleConcreteClass.java", clazz.getSourceFile());

        Collection imports = clazz.getImportedPackages();
        assertEquals(1, imports.size());

        assertTrue(imports.contains(new JavaPackage("java.lang")));

    }
    
    @Test
    public void testExampleClassFileFromTimDrury() throws IOException {
        // see http://github.com/clarkware/jdepend/issues#issue/1
        parser.parse(ClassFileParser.class.getResourceAsStream("/data/example_class1.bin"));
    }
    
    @Test
    public void testExampleClassFile2() throws IOException {
        parser.parse(ClassFileParser.class.getResourceAsStream("/data/example_class2.bin"));
    }
}

