import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Set; // ???
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		var parenthesesList = List.of("()[]{}", "([{}])", "(]", "([)]");
		// System.out.println(parentheses);

		for (var parentheses : parenthesesList) {
			var isValid = isParenthesesValid(parentheses);
			System.out.println(isValid);
		}
	}

	public static boolean isParenthesesValid(String parentheses) {
		Deque<Character> parenthesisDeque = new ArrayDeque<>();
		Map<Character, Character> pairs = Map.of(
			'(', ')',
			'[', ']',
			'{', '}'
		);

		for (int i = 0; i < parentheses.length(); i++) {
			var parenthesis = parentheses.charAt(i);

			if (parenthesis == '(' || parenthesis == '{' || parenthesis == '[') {
				parenthesisDeque.push(parenthesis);
			} else if (parenthesis == ')' || parenthesis == ']' || parenthesis == '}') {
				if (parenthesisDeque.isEmpty()) {
					return false;
				}

				var lastOpening = parenthesisDeque.pop();

				if (parenthesis != pairs.get(lastOpening)) {
					return false;
				}
			}
		} 

		if (!parenthesisDeque.isEmpty()) {
				return false;
		}

		return true;
	}
}