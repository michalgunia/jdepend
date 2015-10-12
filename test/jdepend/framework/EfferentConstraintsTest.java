package jdepend.framework;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import jdepend.framework.exceptions.ForbidenEfferentException;

/**
 * This class is useful for creating rules which point the forbidden efferents
 * e.g jdepend.framework should not depend upon jdepeng.swingui
 * 
 * 
 * @author michal
 *
 */
public class EfferentConstraintsTest extends JDependTestCase {

	private JDepend jdepend;

	
	@Before
	public void setUp() {
		super.setUp();
		jdepend = new JDepend();
	}
	

	@Test
	public void testShouldFindConstreints(){
		EfferentConstraints constraints = new EfferentConstraints();
		constraints.addPackage("jdepend.xx").shouldNotDependUpon("jdepend.yy");
		
		assertNull(constraints.findPackageWithRestriction("jdepend.zz"));
		assertNull(constraints.findPackageWithRestriction("jdepend.yy"));
		assertNotNull(constraints.findPackageWithRestriction("jdepend.xx"));
	} 
	
	@Test
	public void testShouldFindOneGoodConstreints(){
		EfferentConstraints constraints = new EfferentConstraints();
		constraints.addPackage("jdepend.xx").shouldNotDependUpon("jdepend.yy");
		constraints.addPackage("jdepend.xx").shouldNotDependUpon("jdepend.yz");

		assertEquals(2,constraints.findPackageWithRestriction("jdepend.xx").getNotSupportedEfferents().size());
		assertEquals("jdepend.yy", constraints.findPackageWithRestriction("jdepend.xx").getNotSupportedEfferents().get(0).getName());
		assertEquals("jdepend.yz", constraints.findPackageWithRestriction("jdepend.xx").getNotSupportedEfferents().get(1).getName());
	}
	
	@Test
	public void testShouldFindOneGoodConstreintsWithStarts(){
		EfferentConstraints constraints = new EfferentConstraints();
		constraints.addPackage("jdepend.xx").shouldNotDependUpon("jdepend.bb*");
		constraints.addPackage("jdepend.xx").shouldNotDependUpon("jdepend.bc*");

		assertEquals(2,constraints.findPackageWithRestriction("jdepend.xx").getNotSupportedEfferents().size());
		assertEquals("jdepend.bb*", constraints.findPackageWithRestriction("jdepend.xx").getNotSupportedEfferents().get(0).getName());
		assertEquals("jdepend.bc*", constraints.findPackageWithRestriction("jdepend.xx").getNotSupportedEfferents().get(1).getName());
	}
	
	@Test(expected=ForbidenEfferentException.class)
	public void testShouldThrowExcetion() throws IOException, ForbidenEfferentException {

		jdepend.addDirectory(getBuildDir());
		jdepend.analyze();

		EfferentConstraints constraints = new EfferentConstraints();
		constraints.addPackage("jdepend.textui").shouldNotDependUpon("jdepend.framework");

		assertEquals(false, jdepend.areProhibitionRulesFulfilled(constraints));
	}
	
	@Test
	public void testShouldThrowExcetionRuleWithStar() throws IOException, ForbidenEfferentException{

		jdepend.addDirectory(getBuildDir());
		jdepend.analyze();

		EfferentConstraints constraints = new EfferentConstraints();
		constraints.addPackage("jdepend.framework").shouldNotDependUpon("java.io*");
		constraints.addPackage("jdepend.framework").shouldNotDependUpon("jdepend.textui*");
		
		try{
			jdepend.areProhibitionRulesFulfilled(constraints);
		} catch (ForbidenEfferentException ex){
			assertTrue(ex.getMessage().contains("jdepend.framework") && ex.getMessage().contains("java.io"));
		}
	}

	public void testShouldPass() throws IOException, ForbidenEfferentException {

		jdepend.addDirectory(getBuildDir());
		jdepend.analyze();
		EfferentConstraints constraints = new EfferentConstraints();
		constraints.addPackage("jdepend.framework").shouldNotDependUpon("jdepend.textui*");
		constraints.addPackage("jdepend.framework").shouldNotDependUpon("jdepend.swingui*");
		
		assertEquals(true, jdepend.areProhibitionRulesFulfilled(constraints));
	}
}