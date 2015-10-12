package jdepend.framework.exceptions;

@SuppressWarnings("serial")
public class ForbidenEfferentException extends Exception{
	
	String efferent;
	String jpackage;
	
	
	
	public ForbidenEfferentException(String efferent, String jpackage) {
		super();
		this.efferent = efferent;
		this.jpackage = jpackage;
	}



	@Override
	public String getMessage() {
		String msg = new StringBuilder().append("Package ").append(jpackage).append(" can't depends on ").append(efferent).toString();
		return msg;
	}
}
