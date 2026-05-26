import java.util.HashSet;

class Main {

	public static void main(String[] args) {
		var s = "abcabcabcdabcdefghijka";

		var longestSubstringCountHashSet = HashSetSolution.longestSubstringWithoutRepeating(s);
		var longestSubstringCountHashMap = HashMapSolution.solve(s);

		System.out.println(longestSubstringCountHashSet);
		System.out.println(longestSubstringCountHashMap);
	}

	

}