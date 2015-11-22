class InstructionFactory {

	public Instruction getInstruction(String str) throws ParseException {

		if ( str.equals("INTEGER.+") )
			return new IntegerAdd();
		else if ( str.equals("INTEGER.-") )
			return new IntegerSub();
		else if ( str.equals("INTEGER.*") )
			return new IntegerMul();
		else if ( str.equals("INTEGER./") )
			return new IntegerDiv();
		else if ( str.equals("INTEGER.%") )
			return new IntegerMod();
		else if ( str.equals("INTEGER.<") )
			return new IntegerLess();
		else if ( str.equals("INTEGER.=") )
			return new IntegerEqual();
		else if ( str.equals("INTEGER.>") )
			return new IntegerGreater();
		else
			throw new ParseException("'" + str + "'" + " not known.");
	}
}