

class Main {


	public static void main(String[] args) {
		// Pass by value
		int num = 1;
		System.out.println(num);
		changePrimitive(num);
		System.out.println(num);

		// Still pass by value but the value is the reference to object 
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

	/* 
		main person ----┐
		method person --┘ ---> Changed
	*/
	static void mutatePerson(Person person) {
		person.name = "Changed";
	}

	/* 
		main person ----------> Changed

		method person --------> Czesław
	*/
	static void reassignPerson(Person person) {
		person = new Person("Czesław");
	}

}