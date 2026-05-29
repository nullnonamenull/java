import java.util.Objects;

class Person {

	private String name;
	private Integer age;

	public Person(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Person p)) {
			return false;
		} 

		return Objects.equals(this.name, p.name)
				&& Objects.equals(this.age, p.age);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.age);
	}

}