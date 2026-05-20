import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

class Main {

	public static void main(String[] args) {
		var strings = new String[]{"eat","tea","tan","ate","nat","bat"};

		// for (var s : strings) {
		// 	System.out.println(s);
		// }

		var groups = groupAnagram(strings);

		System.out.println(groups);
	}
	

	static List<List<String>> groupAnagram(String[] strings) {
		var map = new HashMap<String, List<String>>();

		for (var s : strings) {
			var chars = s.toCharArray();
			Arrays.sort(chars);
			var sortedString = new String(chars);

			// if (map.containsKey(sortedString)) {
			// 	map.get(sortedString).add(s);
			// } else {
			// 	var list = new ArrayList<String>();
			// 	list.add(s);
			// 	map.put(sortedString, list);
			// }

			map.computeIfAbsent(sortedString, key -> new ArrayList<>()).add(s);
		}

		// var result = new ArrayList<List<String>>();
		// for (var anagrms : map.values()) {
		// 	result.add(anagrms);
		// }

		return new ArrayList<>(map.values());
	}

}