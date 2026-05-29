

class Main {

	public static void main(String[] args) {
		// Pass by value
		int num = 1;
		System.out.println(num);
		changePrimitive(num);
		System.out.println(num);

		// Pass by reference
		var person = new Person("Marianek");
		System.out.println(person.name);
		mutatePerson(person);
		System.out.println(person.name);

		reassignPerson(person);
		System.out.println(person.name);
	}

	static void changePrimitive(int num) {
		num++;
	}

	static void mutatePerson(Person person) {
		person.name = "Changed";
	}

	static void reassignPerson(Person person) {
		person = new Person("Czesław");
	}

}