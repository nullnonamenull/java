class Main {
	
	public static void main(String[] args) {
		var text = "aA man, a plan, a canal: Panamaa";
		// var text = "0P";
		// var text = "AGA";

		var isPalindrome = isPalindrome(text);

		System.out.println(isPalindrome);
	}


	static boolean isPalindrome(String text) {
		var leftPointer = 0;
		var rightPointer = text.length() - 1;
		// var newText = text.toLowerCase();

		while(leftPointer < rightPointer) {
			if (!Character.isLetterOrDigit(text.charAt(leftPointer))) {
				leftPointer++;
				continue;
			}
			if (!Character.isLetterOrDigit(text.charAt(rightPointer))) {
				rightPointer--;
				continue;
			}

			if (Character.toLowerCase(text.charAt(leftPointer)) != Character.toLowerCase(text.charAt(rightPointer))) {
				return false;
			}

			rightPointer--;
			leftPointer++;
		}

		return true;
	}
}