import java.util.HashSet;


public class Main {

	public static void main(String[] args) {
		var tabOne = new int[]{1, 2, 3, 4};
		var tabTwo = new int[]{1, 1, 2, 3 ,4};

		boolean containsDuplicate = containsDuplicate(tabTwo);

		System.out.println(containsDuplicate);
	}

	public static boolean containsDuplicate(int[] tab) {
		var map = new HashSet<Integer>();
		for (var num : tab) {
			if (map.contains(num)) {
				System.out.println("Try add duplicate: " + map.add(num));
				return true;
			}

			map.add(num);
		}

		return false;
	}

}