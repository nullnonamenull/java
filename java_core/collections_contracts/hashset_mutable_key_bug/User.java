import java.util.Objects;

class User {

	String email;

	public User(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.email);
	}

	@Override
	public boolean equals(Object u) {
		if (u ==  null) {
			return false;
		}

		if (!(u instanceof User user)) {
			return false;
		}

		return this.email == user.email;
	}
}