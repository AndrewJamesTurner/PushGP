class IntegerGreater implements Instruction {

	public String toString() {
		return "INTEGER.>";
	}

	public Boolean execute(PushInterpreter interpreter) {

		if ( interpreter.getIntegerStack().size() < 2)
			return false;

		else if ( interpreter.getIntegerStack().peek() == 0)
			return false;
		else {

			int a = interpreter.getIntegerStack().pop();
			int b = interpreter.getIntegerStack().pop();
			Boolean rel = b > a;

			interpreter.getBooleanStack().push(rel);

			return true;
		}
	}
}