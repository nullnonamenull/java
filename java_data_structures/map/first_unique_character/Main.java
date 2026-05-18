import java.util.Optional;
import java.util.HashMap;

class Main {

	public static void main(String[] args) {
		var str = "eeletcode";

		var firstUniqueCharacter = firstUniqueCharacter(str);
		var response = firstUniqueCharacter != -1  ? 
			"String contain uniqe character! The first one is at index: " + firstUniqueCharacter
			: "Nope :(";

		System.out.println(response);
	}

	// TWO-PASS HashMap Pattern
	public static int firstUniqueCharacter(String str) {
		var map = new HashMap<Character, Integer>();

		for (var character : str.toCharArray()) {	
			map.merge(character, 1, Integer::sum);
		}

		for (int i = 0; i < str.length(); i++) {
			var character = str.charAt(i);
			if (map.get(character) == 1) {
				return i;
			}
		}

		return -1;
	}

}