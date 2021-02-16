package basic.reflect;

public class person {
	public String name;
	private int age;
	private String addresse;
	
	public person() {
		super();
	}
	
	private person(int age, String addresse) {
		super();
		this.age = age;
		this.addresse = addresse;
	}

	public person(String name, int age, String addresse) {
		super();
		this.name = name;
		this.age = age;
		this.addresse = addresse;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddresse() {
		return addresse;
	}
	public void setAddresse(String addresse) {
		this.addresse = addresse;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addresse == null) ? 0 : addresse.hashCode());
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		person other = (person) obj;
		if (addresse == null) {
			if (other.addresse != null)
				return false;
		} else if (!addresse.equals(other.addresse))
			return false;
		if (age != other.age)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "person [" + (name != null ? "name=" + name + ", " : "") + "age=" + age + ", "
				+ (addresse != null ? "addresse=" + addresse : "") + "]";
	}

}
