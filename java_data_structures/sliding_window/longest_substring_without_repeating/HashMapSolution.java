import java.util.HashMap;


class HashMapSolution {

	static int solve(String s) {
		var seen = new HashMap<Character, Integer>();
		var longest = 0;
		var right = 0;
		var left = 0;

		while (right < s.length()) {
			var character = s.charAt(right);

			if (seen.containsKey(character) && seen.get(character) >= left) {
				left = seen.get(character) + 1;
			}

			seen.put(character, right);
			longest = Math.max(longest, right - left + 1);
			right++;
		}

		return longest;
	}

}