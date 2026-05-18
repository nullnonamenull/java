import java.util.Arrays;

class Main {

	public static void main(String[] args) {
		var sortedTab = new int[]{-1, 0, 3, 5, 9, 11, 12};
		var target = 9;

		var targetIndex = binarySearch(target, sortedTab);
		System.out.println(targetIndex);
	}

	static int binarySearch(int target, int[] sortedTab) {
		var left = 0;
		var right = sortedTab.length - 1;
		var mid = right / 2;

		while(left <= right) {
			mid = (right + left) / 2;

			if (sortedTab[mid] == target) {
				return mid;
			}
			if (sortedTab[mid] < target) {
				left = mid + 1;
			}
			if (sortedTab[mid] > target) {
				right = mid - 1;
			}
		}

		return -1;
	}
}