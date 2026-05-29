import java.util.HashSet;
import java.util.Set;

class Main {


	public static void main(String[] args) {
		Set<Person> people = new HashSet<>();

		var p1 = new Person("Jasmine", 45);
		var p2 = new Person("Jasmine", 45);
		people.add(p1);
		people.add(p2);

		System.out.println(people.size());
		System.out.println(people);

		System.out.println("Reference check - reference equality: " + (p1 == p2));
		System.out.println("Logical equality (equals): " + p1.equals(p2));
	}

}