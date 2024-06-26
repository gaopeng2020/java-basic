package basic.string;

public class equalsClass extends Object {
		int age;
		String name;

		public equalsClass(String name,int age) {
			super();
			this.age = age;
			this.name = name;
		}



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
			equalsClass other = (equalsClass) obj;
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
			return "equalsClass [age=" + age + ", name=" + name + "]";
		}
}
