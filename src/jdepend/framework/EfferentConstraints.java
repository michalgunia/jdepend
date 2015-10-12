package jdepend.framework;

import java.util.*;

import jdepend.framework.JavaPackage;
import jdepend.framework.exceptions.ForbidenEfferentException;

/**
 * 
 * @author michal
 *
 */

public class EfferentConstraints{

	protected HashMap<String, JavaPackageWithEfferentRestriction> packages;
	
    public EfferentConstraints() {
    	super();
        packages = new HashMap<String, JavaPackageWithEfferentRestriction>();
    }

 
    /**
     * 
     * 
     * @param jPackageToCheck
     * @return true if non forbidden dependencies are found
     * @throws ForbidenEfferentException is forbidden efferent is found
     */
    public boolean areEfferentsValid(JavaPackage jPackageToCheck) throws ForbidenEfferentException {
    	
    	JavaPackageWithEfferentRestriction rulePackage = findPackageWithRestriction(jPackageToCheck
	            .getName());
	
	    if (rulePackage != null) {
	        jPackageToCheck.isAnyForbidenEfferentPresent(rulePackage.getNotSupportedEfferents());
	    }
	
	    return true;
	}
    
    public JavaPackageWithEfferentRestriction addPackage(String packageName) {
    	JavaPackageWithEfferentRestriction jPackage = (JavaPackageWithEfferentRestriction) packages.get(packageName);
	    if (jPackage == null) {
	        jPackage = new JavaPackageWithEfferentRestriction(packageName);
	        addPackage(jPackage);
	    }
	    return jPackage;
	}
    
    private void addPackage(JavaPackageWithEfferentRestriction jPackage) {
	    if (!packages.containsValue(jPackage)) {
	        packages.put(jPackage.getName(), jPackage);
	    }
	}

	JavaPackageWithEfferentRestriction findPackageWithRestriction(String name) {
		
		for (String packageName : packages.keySet()) {
			if(packageName.contains(name)){
				return packages.get(packageName);
			}
		}
    	
		return null;
		
	}



 
}