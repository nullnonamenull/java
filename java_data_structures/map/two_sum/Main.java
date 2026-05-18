import java.util.List;
import java.util.HashMap;


public class Main {

	public static void main(String[] args) {
		var nums = new int[] { 2, 11, 8, 15, 7 };
		var target = 9;

		var idx = twoSum(nums, target); 

		for (var id : idx) {
			System.out.println(id);
		}
	}

	public static int[] twoSum(int[] nums, int target) {
		var map = new HashMap<Integer, Integer>();

		for (int i = 0; i < nums.length; i++) {
			var num = nums[i];
			var wanted = target - num;

			if (map.containsKey(wanted)) {
				return new int[] {map.get(wanted), i};
			}

			map.put(num, i);
		}

		return new int[]{};
	}

}