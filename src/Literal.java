class Literal<T> {

	private T value;
	private LiteralType type;

	Literal(T value, LiteralType type) {
		this.value = value;
		this.type = type;
	}

	public String toString() {
		return value.toString();
	}

	public LiteralType getType() {
		return this.type;
	}

	public T getValue() {
		return this.value;
	}

	public static Boolean isLiteral(String str) {

		if ( Utils.isNumeric(str) || str.toUpperCase().equals("TRUE") || str.toUpperCase().equals("FALSE") )
			return true;
		else
			return false;
	}

	public static Boolean isInteger(String str) {

		if ( Utils.isNumeric(str) ) {

			float tmp = Float.parseFloat(str);

			if ( tmp % 1 == 0 )
				return true;
			else
				return false;
		} else
			return false;
	}

	public static Boolean isFloat(String str) {

		if ( Utils.isNumeric(str) ) {

			float tmp = Float.parseFloat(str);

			if ( tmp % 1 == 0 )
				return false;
			else
				return true;
		} else
			return false;
	}

	public static Boolean isBoolean(String str) {

		if ( str.toUpperCase().equals("TRUE") || str.toUpperCase().equals("FALSE") )
			return true;
		else
			return false;
	}
}