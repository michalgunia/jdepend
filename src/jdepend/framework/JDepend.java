package jdepend.framework;

import java.io.IOException;
import java.util.*;

import jdepend.framework.exceptions.ForbidenEfferentException;

/**
 * The <code>JDepend</code> class analyzes directories of Java class files and
 * generates the following metrics for each Java package.
 * <ul>
 * <li>Afferent Coupling (Ca)
 * <p>
 * The number of packages that depend upon the classes within the analyzed
 * package.
 * </p>
 * </li>
 * <li>Efferent Coupling (Ce)
 * <p>
 * The number of packages that the classes in the analyzed package depend upon.
 * </p>
 * </li>
 * <li>Abstractness (A)
 * <p>
 * The ratio of the number of abstract classes (and interfaces) in the analyzed
 * package to the total number of classes in the analyzed package.
 * </p>
 * <p>
 * The range for this metric is 0 to 1, with A=0 indicating a completely
 * concrete package and A=1 indicating a completely abstract package.
 * </p>
 * </li>
 * <li>Instability (I)
 * <p>
 * The ratio of efferent coupling (Ce) to total coupling (Ce + Ca) such that I =
 * Ce / (Ce + Ca).
 * </p>
 * <p>
 * The range for this metric is 0 to 1, with I=0 indicating a completely stable
 * package and I=1 indicating a completely instable package.
 * </p>
 * </li>
 * <li>Distance from the Main Sequence (D)
 * <p>
 * The perpendicular distance of a package from the idealized line A + I = 1. A
 * package coincident with the main sequence is optimally balanced with respect
 * to its abstractness and stability. Ideal packages are either completely
 * abstract and stable (x=0, y=1) or completely concrete and instable (x=1,
 * y=0).
 * </p>
 * <p>
 * The range for this metric is 0 to 1, with D=0 indicating a package that is
 * coincident with the main sequence and D=1 indicating a package that is as far
 * from the main sequence as possible.
 * </p>
 * </li>
 * <li>Package Dependency Cycle
 * <p>
 * Package dependency cycles are reported along with the paths of packages
 * participating in package dependency cycles.
 * </p>
 * </li>
 * </ul>
 * <p>
 * These metrics are hereafter referred to as the "Martin Metrics", as they are
 * credited to Robert Martin (Object Mentor Inc.) and referenced in the book
 * "Designing Object Oriented C++ Applications using the Booch Method", by
 * Robert C. Martin, Prentice Hall, 1995.
 * </p>
 * <p>
 * Example API use:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * JDepend jdepend = new JDepend();
 * jdepend.addDirectory(&quot;/path/to/classes&quot;);
 * Collection packages = jdepend.analyze();
 *
 * Iterator i = packages.iterator();
 * while (i.hasNext()) {
 * 	JavaPackage jPackage = (JavaPackage) i.next();
 * 	String name = jPackage.getName();
 * 	int Ca = jPackage.afferentCoupling();
 * 	int Ce = jPackage.efferentCoupling();
 * 	float A = jPackage.abstractness();
 * 	float I = jPackage.instability();
 * 	float D = jPackage.distance();
 * 	boolean b = jPackage.containsCycle();
 * }
 * </pre>
 * 
 * </blockquote>
 * <p>
 * This class is the data model used by the <code>jdepend.textui.JDepend</code>
 * and <code>jdepend.swingui.JDepend</code> views.
 * </p>
 *
 * @author <b>Mike Clark</b>
 * @author Clarkware Consulting, Inc.
 */

public class JDepend {

	private Map<String, JavaPackage> packages;
	private FileManager fileManager;
	private PackageFilter filter;
	private ClassFileParser parser;
	private JavaClassBuilder builder;
	private Collection<String> components;

	public JDepend() {
		this(PackageFilter.all().excludingProperties());
	}

	public JDepend(PackageFilter filter) {

		setFilter(filter);

		this.packages = new HashMap<String, JavaPackage>();
		this.fileManager = new FileManager();

		this.parser = new ClassFileParser(filter);
		this.builder = new JavaClassBuilder(parser, fileManager);

		PropertyConfigurator config = new PropertyConfigurator();
		addPackages(config.getConfiguredPackages());
		analyzeInnerClasses(config.getAnalyzeInnerClasses());
	}

	/**
	 * Analyzes the registered directories and returns the collection of
	 * analyzed packages.
	 *
	 * @return Collection of analyzed packages.
	 */
	public Collection<JavaPackage> analyze() {

		Collection<JavaClass> classes = builder.build();

		for (JavaClass aClass : classes) {
			analyzeClass(aClass);
		}

		return getPackages();
	}

	/**
	 * Adds the specified directory name to the collection of directories to be
	 * analyzed.
	 *
	 * @param name
	 *            Directory name.
	 * @throws IOException
	 *             If the directory is invalid.
	 */
	public void addDirectory(String name) throws IOException {
		fileManager.addDirectory(name);
	}

	/**
	 * Sets the list of components.
	 *
	 * @param components
	 *            Comma-separated list of components.
	 */
	public void setComponents(String components) {
		this.components = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(components, ",");
		while (st.hasMoreTokens()) {
			String component = st.nextToken();
			this.components.add(component);
		}
	}

	/**
	 * Determines whether inner classes are analyzed.
	 *
	 * @param b
	 *            <code>true</code> to analyze inner classes; <code>false</code>
	 *            otherwise.
	 */
	public void analyzeInnerClasses(boolean b) {
		fileManager.acceptInnerClasses(b);
	}

	/**
	 * Returns the collection of analyzed packages.
	 *
	 * @return Collection of analyzed packages.
	 */
	public Collection<JavaPackage> getPackages() {
		return packages.values();
	}

	/**
	 * Returns the analyzed package of the specified name.
	 *
	 * @param name
	 *            Package name.
	 * @return Package, or <code>null</code> if the package was not analyzed.
	 */
	public JavaPackage getPackage(String name) {
		return packages.get(name);
	}

	/**
	 * Returns the number of analyzed Java packages.
	 *
	 * @return Number of Java packages.
	 */
	public int countPackages() {
		return getPackages().size();
	}

	/**
	 * Returns the number of registered Java classes to be analyzed.
	 *
	 * @return Number of classes.
	 */
	public int countClasses() {
		return builder.countClasses();
	}

	/**
	 * Indicates whether the packages contain one or more dependency cycles.
	 *
	 * @return <code>true</code> if one or more dependency cycles exist.
	 */
	public boolean containsCycles() {
		for (JavaPackage jPackage : getPackages()) {
			if (jPackage.containsCycle()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Indicates whether the analyzed packages match the specified dependency
	 * constraint.
	 *
	 * @return <code>true</code> if the packages match the dependency constraint
	 */
	public boolean dependencyMatch(DependencyConstraint constraint) {
		return constraint.match(getPackages()).matches();
	}

	public DependencyConstraint.MatchResult analyzeDependencies(DependencyConstraint constraint) {
		return constraint.match(getPackages());
	}

	/**
	 * Registers the specified parser listener.
	 *
	 * @param listener
	 *            Parser listener.
	 */
	public void addParseListener(ParserListener listener) {
		parser.addParseListener(listener);
	}

	/**
	 * Adds the specified Java package name to the collection of analyzed
	 * packages.
	 *
	 * @param name
	 *            Java package name.
	 * @return Added Java package.
	 */
	public JavaPackage addPackage(String name) {
		name = toComponent(name);
		JavaPackage pkg = packages.get(name);
		if (pkg == null) {
			pkg = new JavaPackage(name);
			addPackage(pkg);
		}

		return pkg;
	}

	private String toComponent(String packageName) {
		if (components != null) {
			for (String component : components) {
				if (packageName.startsWith(component + ".")) {
					return component;
				}
			}
		}
		return packageName;
	}

	/**
	 * Adds the specified collection of packages to the collection of analyzed
	 * packages.
	 *
	 * @param packages
	 *            Collection of packages.
	 */
	public void addPackages(Collection<JavaPackage> packages) {
		for (JavaPackage pkg : packages) {
			addPackage(pkg);
		}
	}

	/**
	 * Adds the specified Java package to the collection of analyzed packages.
	 *
	 * @param pkg
	 *            Java package.
	 */
	public void addPackage(JavaPackage pkg) {
		if (!packages.containsValue(pkg)) {
			packages.put(pkg.getName(), pkg);
		}
	}

	public PackageFilter getFilter() {
		if (filter == null) {
			filter = PackageFilter.all().excludingProperties();
		}

		return filter;
	}

	public void setFilter(PackageFilter filter) {
		if (parser != null) {
			parser.setFilter(filter);
		}
		this.filter = filter;
	}

	private void analyzeClass(JavaClass clazz) {
		String packageName = clazz.getPackageName();

		if (!getFilter().accept(packageName)) {
			return;
		}

		JavaPackage clazzPackage = addPackage(packageName);
		clazzPackage.addClass(clazz);

		for (JavaPackage importedPackage : clazz.getImportedPackages()) {
			importedPackage = addPackage(importedPackage.getName());
			clazzPackage.dependsUpon(importedPackage);
		}
	}
	
	/**
     * 
     * 
     * @param jPackageToCheck
     * @return true any forbiden dependencis are found
     * @throws ForbidenEfferentException is forbiden efferent is found
     */
	public boolean areProhibitionRulesFulfilled(EfferentConstraints rules) throws ForbidenEfferentException {
		for (Iterator<JavaPackage> i = packages.values().iterator(); i.hasNext();) {
			JavaPackage pkg = (JavaPackage) i.next();
			if (!rules.areEfferentsValid(pkg)) {
				return false;
			}
		}
		return true;
	}
}
