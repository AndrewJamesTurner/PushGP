class IntegerAdd implements Instruction {

	public String toString() {
		return "INTEGER.+";
	}

	public Boolean execute(PushInterpreter interpreter) {

		if ( interpreter.getIntegerStack().size() < 2) {
			return false;
		} else {

			int a = interpreter.getIntegerStack().pop();
			int b = interpreter.getIntegerStack().pop();
			int rel = b + a;

			interpreter.getIntegerStack().push(rel);

			return true;
		}
	}
}