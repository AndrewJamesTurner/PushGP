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

}