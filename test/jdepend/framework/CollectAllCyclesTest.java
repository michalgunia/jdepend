package jdepend.framework;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class CollectAllCyclesTest extends JDependTestCase {

	@Test
    public void testNoCycles() {

        JavaPackage a = new JavaPackage("A");
        JavaPackage b = new JavaPackage("B");

        a.dependsUpon(b);

        List<JavaPackage> aCycles = new ArrayList<JavaPackage>();
        assertEquals(false, a.containsCycle());
        assertEquals(false, a.collectAllCycles(aCycles));
        assertListEquals(aCycles, new String[]{});

        List<JavaPackage> bCycles = new ArrayList<JavaPackage>();
        assertEquals(false, b.containsCycle());
        assertEquals(false, b.collectAllCycles(bCycles));
        assertListEquals(bCycles, new String[]{});
    }
    
    @Test
    public void test2Node1BranchCycle() {

        JavaPackage a = new JavaPackage("A");
        JavaPackage b = new JavaPackage("B");

        a.dependsUpon(b);
        b.dependsUpon(a);

        List<JavaPackage> aCycles = new ArrayList<JavaPackage>();
        assertEquals(true, a.containsCycle());
        assertEquals(true, a.collectAllCycles(aCycles));
        assertListEquals(aCycles, new String[]{"A", "B", "A" });

        List<JavaPackage> bCycles = new ArrayList<JavaPackage>();
        assertEquals(true, b.containsCycle());
        assertEquals(true, b.collectAllCycles(bCycles));
        assertListEquals(bCycles, new String[]{"B", "A", "B" });
    }
    
    @Test
    public void test3Node1BranchCycle() {

        JavaPackage a = new JavaPackage("A");
        JavaPackage b = new JavaPackage("B");
        JavaPackage c = new JavaPackage("C");

        a.dependsUpon(b);
        b.dependsUpon(c);
        c.dependsUpon(a);


        List<JavaPackage> aCycles = new ArrayList<JavaPackage>();
        assertEquals(true, a.containsCycle());
        assertEquals(true, a.collectAllCycles(aCycles));
        assertListEquals(aCycles, new String[]{"A", "B", "C", "A" });

        List<JavaPackage> bCycles = new ArrayList<JavaPackage>();
        assertEquals(true, b.containsCycle());
        assertEquals(true, b.collectAllCycles(bCycles));
        assertListEquals(bCycles, new String[]{"B", "C", "A", "B" });

        List<JavaPackage> cCycles = new ArrayList<JavaPackage>();
        assertEquals(true, c.containsCycle());
        assertEquals(true, c.collectAllCycles(cCycles));
        assertListEquals(cCycles, new String[]{"C", "A", "B", "C" });
    }
    
    @Test
    public void test3Node1BranchSubCycle() {

        JavaPackage a = new JavaPackage("A");
        JavaPackage b = new JavaPackage("B");
        JavaPackage c = new JavaPackage("C");

        a.dependsUpon(b);
        b.dependsUpon(c);
        c.dependsUpon(b);

        List<JavaPackage> aCycles = new ArrayList<JavaPackage>();
        assertEquals(true, a.containsCycle());
        assertEquals(true, a.collectAllCycles(aCycles));
        assertListEquals(aCycles, new String[]{"A", "B", "C", "B" });

        List<JavaPackage> bCycles = new ArrayList<JavaPackage>();
        assertEquals(true, b.containsCycle());
        assertEquals(true, b.collectAllCycles(bCycles));
        assertListEquals(bCycles, new String[]{"B", "C", "B" });

        List<JavaPackage> cCycles = new ArrayList<JavaPackage>();
        assertEquals(true, c.containsCycle());
        assertEquals(true, c.collectAllCycles(cCycles));
        assertListEquals(cCycles, new String[]{"C", "B", "C" });
    }
    
    @Test
    public void test3Node2BranchCycle() {

        JavaPackage a = new JavaPackage("A");
        JavaPackage b = new JavaPackage("B");
        JavaPackage c = new JavaPackage("C");

        a.dependsUpon(b);
        b.dependsUpon(a);

        a.dependsUpon(c);
        c.dependsUpon(a);

        List<JavaPackage> aCycles = new ArrayList<JavaPackage>();
        assertEquals(true, a.containsCycle());
        assertEquals(true, a.collectAllCycles(aCycles));
        assertListEquals(aCycles, new String[]{"A", "B", "A", "C", "A" });

        List<JavaPackage> bCycles = new ArrayList<JavaPackage>();
        assertEquals(true, b.containsCycle());
        assertEquals(true, b.collectAllCycles(bCycles));
        assertListEquals(bCycles, new String[]{"B", "A", "B", "C", "A" });

        List<JavaPackage> cCycles = new ArrayList<JavaPackage>();
        assertEquals(true, c.containsCycle());
        assertEquals(true, c.collectAllCycles(cCycles));
        assertListEquals(cCycles, new String[]{"C", "A", "B", "A", "C" });
    }
    
    @Test
    public void test5Node2BranchCycle() {

        JavaPackage a = new JavaPackage("A");
        JavaPackage b = new JavaPackage("B");
        JavaPackage c = new JavaPackage("C");
        JavaPackage d = new JavaPackage("D");
        JavaPackage e = new JavaPackage("E");

        a.dependsUpon(b);
        b.dependsUpon(c);
        c.dependsUpon(a);

        a.dependsUpon(d);
        d.dependsUpon(e);
        e.dependsUpon(a);

        List<JavaPackage> aCycles = new ArrayList<JavaPackage>();
        assertEquals(true, a.containsCycle());
        assertEquals(true, a.collectAllCycles(aCycles));
        assertListEquals(aCycles, new String[]{"A", "B", "C", "A", "D", "E", "A" });

        List<JavaPackage> bCycles = new ArrayList<JavaPackage>();
        assertEquals(true, b.containsCycle());
        assertEquals(true, b.collectAllCycles(bCycles));
        assertListEquals(bCycles, new String[]{"B", "C", "A", "B", "D", "E", "A" });

        List<JavaPackage> cCycles = new ArrayList<JavaPackage>();
        assertEquals(true, c.containsCycle());
        assertEquals(true, c.collectAllCycles(cCycles));
        assertListEquals(cCycles, new String[]{"C", "A", "B", "C", "D", "E", "A" });

        List<JavaPackage> dCycles = new ArrayList<JavaPackage>();
        assertEquals(true, d.containsCycle());
        assertEquals(true, d.collectAllCycles(dCycles));
        assertListEquals(dCycles, new String[]{"D", "E", "A", "B", "C", "A", "D" });

        List<JavaPackage> eCycles = new ArrayList<JavaPackage>();
        assertEquals(true, e.containsCycle());
        assertEquals(true, e.collectAllCycles(eCycles));
        assertListEquals(eCycles, new String[]{"E", "A", "B", "C", "A", "D", "E" });
    }

    protected void assertListEquals(List<JavaPackage> list, String names[]) {

        assertEquals(names.length, list.size());

        for (int i = 0; i < names.length; i++) {
            assertEquals(names[i], (list.get(i)).getName());
        }
    }
}
