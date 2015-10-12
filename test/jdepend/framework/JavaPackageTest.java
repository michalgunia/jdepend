package jdepend.framework;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import jdepend.framework.exceptions.ForbidenEfferentException;

public class JavaPackageTest {

	@Test(expected=ForbidenEfferentException.class)
	public void testShouldFindForbidenEffrent() throws ForbidenEfferentException {
		JavaPackage comx = new JavaPackage("com.x");
		JavaPackage comy = new JavaPackage("com.y");
		comx.dependsUpon(comy);
		
		comx.isAnyForbidenEfferentPresent(Arrays.asList(comy));
	}
	
	@Test
	public void testShouldNotFindForbidenEffrent() throws ForbidenEfferentException {
		JavaPackage comx = new JavaPackage("com.x");
		JavaPackage comy = new JavaPackage("com.y");
		JavaPackage comz = new JavaPackage("com.z");
		comx.dependsUpon(comy);
		
		comx.isAnyForbidenEfferentPresent(Arrays.asList(comz));
	}
	
	@Test(expected=ForbidenEfferentException.class)
	public void testShouldFindForbidenEffrentWithStar() throws ForbidenEfferentException {
		JavaPackage comx = new JavaPackage("com.x");
		JavaPackage comy = new JavaPackage("com.y");
		JavaPackage comzu = new JavaPackage("com.z.u");
		JavaPackage comzuStar = new JavaPackage("com.z.*");
		
		comx.dependsUpon(comy);
		comx.dependsUpon(comzu);
		
		comx.isAnyForbidenEfferentPresent(Arrays.asList(comzuStar));
	}
	
	@Test(expected=ForbidenEfferentException.class)
	public void testShouldFindForbidenEffrentWithStarOtherFormat() throws ForbidenEfferentException {
		JavaPackage comx = new JavaPackage("com.x");
		JavaPackage comy = new JavaPackage("com.y");
		JavaPackage comzu = new JavaPackage("com.z.u");
		JavaPackage comzuStar = new JavaPackage("com.z*");
		
		comx.dependsUpon(comy);
		comx.dependsUpon(comzu);
		
		comx.isAnyForbidenEfferentPresent(Arrays.asList(comzuStar));
	}
	
	@Test
	public void testShouldFindForbidenEffrentWithoutStar() throws ForbidenEfferentException {
		JavaPackage comx = new JavaPackage("com.x");
		JavaPackage comy = new JavaPackage("com.y");
		JavaPackage comzu = new JavaPackage("com.z.u");
		JavaPackage comzuStar = new JavaPackage("com.z");
		
		comx.dependsUpon(comy);
		comx.dependsUpon(comzu);
		
		comx.isAnyForbidenEfferentPresent(Arrays.asList(comzuStar));
	}
	
}
