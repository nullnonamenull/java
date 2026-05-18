import java.util.HashMap;

public class Main {


	public static void main(String[] args) {
		var s = "anagram";
		var t = "nagaram";

		var anagramValid = isAnagramValid(s, t);
		System.out.println(anagramValid);
	}

	private static boolean isAnagramValid(String a, String b) {
		if (a.length() != b.length()) {
			return false;			
		}

		var charCounterMap = new HashMap<Character, Integer>();

		for (var character : a.toCharArray()) {
			if (charCounterMap.containsKey(character)) {
				charCounterMap.put(character, charCounterMap.get(character) + 1);
			} else {
				charCounterMap.put(character, 1);
			}
		}

		for (var character : b.toCharArray()) {
			if (!charCounterMap.containsKey(character)) {
				return false;
			}

			var count = charCounterMap.get(character) - 1;

			if (count < 0) {
				return false;
			}

			charCounterMap.put(character, count);
		}

		return true;
	}

}