import java.util.HashSet;

class Main {
	
	public static void main(String[] args) {
		var users = new HashSet<User>();

		var user = new User("cos@cos.pl");

		// Generated hash with user.hasCode() for cos@cos.pl email 
		users.add(user);

		System.out.println(users.contains(user));

		user.email = "test@test.pl";

		// Old hash doesnt fit to new hash test@test.pl email
		System.out.println(users.contains(user));

		// Same as up
		System.out.println(users.remove(user));
		
		// There is still user :D
		System.out.println(users);
	}

}