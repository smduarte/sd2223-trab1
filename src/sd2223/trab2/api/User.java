package sd2223.trab2.api;

/**
 * Represents a user in the system. Note: the password of a user should not be
 * returned in any method.
 */
public class User {
	private String name;
	private String pwd;
	private String displayName;
	private String domain;

	public User() {
		this.pwd = null;
		this.name = null;
		this.domain = null;
		this.displayName = null;
	}

	public User(String name, String pwd, String domain, String displayName) {
		this.pwd = pwd;
		this.name = name;
		this.domain = domain;
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", pwd=" + pwd + ", displayName=" + displayName + ", domain=" + domain + "]";
	}
	
	/*
	 * Extended User Operations
	 */
	
	public void updateUser( User other )  {
		if( other.displayName != null )
			setDisplayName(other.displayName);
		if( other.pwd != null )
			setPwd( other.pwd );
	}
	
	public boolean hasNullFields() {
		return name == null || pwd == null || domain == null || displayName == null;		
	}
	
	public User secureCopy() {
		return new User( name, "", domain, displayName);			
	}

	static public boolean badData(String name, User data) {
		return data == null || data.getName() == null || ! name.equals(data.getName());
	}
}
