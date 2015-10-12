package jdepend.framework;

import java.util.*;
/**
 *   The <code>JavaPackage</code> with restrictions to efferents(imports)
 *   Should be used co create junit which check if Your project hava correcdependencis
 * 
 * 
 * @author michal
 *
 */
public class JavaPackageWithEfferentRestriction extends JavaPackage {

    private List<JavaPackage> notSupportedEfferents;

	public JavaPackageWithEfferentRestriction(String name) {
        this(name, 1);
    }

    public JavaPackageWithEfferentRestriction(String name, int volatility) {
        super(name, volatility);        
        notSupportedEfferents = new ArrayList<JavaPackage>();
    }

    List<JavaPackage> getNotSupportedEfferents() {
		return notSupportedEfferents;
	}
       
    void addNotSupportedEfferent(JavaPackage jPackage) {
        if (!jPackage.getName().equals(getName())) {
            if (!notSupportedEfferents.contains(jPackage)) {
                notSupportedEfferents.add(jPackage);
            }
        }
    }
  
	public void shouldNotDependUpon(String jpackage) {
		addNotSupportedEfferent(new JavaPackage(jpackage));
	}
	
}
