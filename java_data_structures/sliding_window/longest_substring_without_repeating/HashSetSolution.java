import java.util.HashSet;


class HashSetSolution {
	
	static int longestSubstringWithoutRepeating(String s) {
		var seen = new HashSet<Character>();
		var left = 0;
		var right = 0;
		var longest = 0;

		while (right < s.length()) {
			var character = s.charAt(right);

			if (seen.add(character)) {
				longest = Math.max(longest, right - left + 1);
				right++;
			} else {
				seen.remove(s.charAt(left));
				left++;
			}
		}

		return longest;
	}

}