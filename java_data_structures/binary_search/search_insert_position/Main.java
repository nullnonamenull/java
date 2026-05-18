class Main {
	
	public static void main(String[] args) {
		var nums = new int[]{1, 2, 3, 4, 7, 8, 20, 21, 40, 100, 400};
		var idx = searchInsert(nums, 2);

		System.out.println(idx);
	}

	static int searchInsert(int[] nums, int target) {
		var right = nums.length - 1;
		var left = 0;

		while (left <= right) {
			var mid = (left + right) / 2;

			if (nums[mid] == target) {
				return mid;
			} else if (nums[mid] > target) {
				System.out.println("Target is: " + target + " so right: " + (mid - 1));
				right = mid - 1;
			} else if(nums[mid] < target) {
				System.out.println("Target is: " + target + " so left: " + (mid + 1));
				left = mid + 1;
			}
		}

		return left;
	}

}