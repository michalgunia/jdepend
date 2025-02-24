package jdepend.framework;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.NumberFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class MetricTest extends JDependTestCase {

    private JDepend jdepend;
    private static NumberFormat formatter;

    static {
        formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
    }

    @Before
    public void setUp() {
        super.setUp();

        PackageFilter filter = PackageFilter.all().excludingProperties().excluding("java.*", "javax.*");

        jdepend = new JDepend(filter);
        jdepend.analyzeInnerClasses(false);
    }
    
    @After
    public void tearDown() {
        super.tearDown();
    }
    
    @Test
    public void testAnalyzeClassFiles() throws IOException {
        jdepend.addDirectory(getBuildDir());
        jdepend.addDirectory(getTestBuildDir());
        assertAnalyzePackages();
    }

    private void assertAnalyzePackages() {
        assertEquals(57, jdepend.countClasses());

        PackageFilter filter = jdepend.getFilter().excluding("junit.*");

        jdepend.analyze();

        assertFrameworkPackage();
        assertTextUIPackage();
        assertSwingUIPackage();
        assertXmlUIPackage();
    }

    private void assertFrameworkPackage() {
        JavaPackage p = jdepend.getPackage("jdepend.framework");
        assertNotNull(p);

        assertEquals(28, p.getConcreteClassCount());
        assertEquals(7, p.getAbstractClassCount());
        assertEquals(3, p.afferentCoupling());
        assertEquals(7, p.efferentCoupling());
        assertEquals(format(0.2f), format(p.abstractness()));
        assertEquals(format(0.7f), format(p.instability()));
        assertEquals(format(0.1f), format(p.distance()));
        assertEquals(1, p.getVolatility());
    }

    private void assertTextUIPackage() {
        JavaPackage p = jdepend.getPackage("jdepend.textui");
        assertNotNull(p);

        assertEquals(1, p.getConcreteClassCount());
        assertEquals(0, p.getAbstractClassCount());
        assertEquals(1, p.efferentCoupling());
        assertEquals("0", format(p.abstractness()));
        assertEquals(1, p.afferentCoupling());
        assertEquals(format(0.5f), format(p.instability()));
        assertEquals(format(0.5f), format(p.distance()));
        assertEquals(1, p.getVolatility());
    }

    private void assertSwingUIPackage() {

        JavaPackage p = jdepend.getPackage("jdepend.swingui");
        assertNotNull(p);

        assertEquals(7, p.getConcreteClassCount());
        assertEquals(1, p.getAbstractClassCount());
        assertEquals(0, p.afferentCoupling());
        assertEquals(1, p.efferentCoupling());
        assertEquals(format(0.12f), format(p.abstractness()));
        assertEquals("1", format(p.instability()));
        assertEquals(format(0.12f), format(p.distance()));
        assertEquals(1, p.getVolatility());
    }

    private void assertXmlUIPackage() {

        JavaPackage p = jdepend.getPackage("jdepend.xmlui");
        assertNotNull(p);

        assertEquals(1, p.getConcreteClassCount());
        assertEquals(0, p.getAbstractClassCount());
        assertEquals(0, p.afferentCoupling());
        assertEquals(2, p.efferentCoupling());
        assertEquals(format(0.0f), format(p.abstractness()));
        assertEquals("1", format(p.instability()));
        assertEquals(format(0.0f), format(p.distance()));
        assertEquals(1, p.getVolatility());
    }
    
    @Test
    public void testConfiguredVolatility() throws IOException {

        jdepend.addDirectory(getBuildDir());

        JavaPackage pkg = new JavaPackage("jdepend.swingui");
        pkg.setVolatility(0);

        jdepend.addPackage(pkg);

        jdepend.analyze();

        JavaPackage analyzedPkg = jdepend.getPackage(pkg.getName());
        assertEquals(0, analyzedPkg.getVolatility());
        assertEquals(format(0.0f), format(analyzedPkg.distance()));
        assertEquals(7, analyzedPkg.getConcreteClassCount());
    }

    private String format(float f) {
        return formatter.format(f);
    }
}