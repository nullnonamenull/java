

class Main {

	public static void main(String[] args) {

		// a ───┐
		// b ───┤
		// e ───┘ ----> pooled "Hello"

		// c ----------> new "Hello"
		// d ----------> new "Hello"

		var a = "Hello";
		var b = "Hello";

		var c = new String("Hello");
		var d = new String("Hello");

		// pooled string
		var e = c.intern();

		System.out.println("Reference equality a==b: " + (a == b));
		System.out.println("Logical equality a.equals(b): " + a.equals(b));

		System.out.println("Reference equality a==c: " + (a == c));
		System.out.println("Logical equality a.equals(c): " + a.equals(c));

		System.out.println("Reference equality c==d: " + (c == d));
		System.out.println("Logical equality c.equals(d): " + c.equals(d));

		System.out.println("Reference equality a==e: " + (a == e));
		System.out.println("Reference equality c==e: " + (c == e));
	}

}