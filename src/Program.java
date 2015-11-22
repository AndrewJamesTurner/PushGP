import java.util.List;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Arrays;


public class Program {

	private ProgramType programType;
	private Instruction instruction;
	private Literal literal;
	private ArrayDeque<Program> subProgram;

	Program(String programString) throws ParseException {

		InstructionFactory instructionFactory = new InstructionFactory();
		LinkedList<String> programStringSplit = new LinkedList<String>( Arrays.asList(programString.split("\\s+")) );
		String next = programStringSplit.pop();

		// if the next begins a new program
		if (next.equals("(")) {

			programType = ProgramType.SUBPROGRAM;
			subProgram = new ArrayDeque<Program>();
			parseSubProgram(programStringSplit);
		}

		// if the next is a literal
		else if (Literal.isLiteral(next)) {

			programType = ProgramType.LITERAL;

			if (Literal.isInteger(next))
				literal = new Literal<Integer>(Integer.parseInt(next), LiteralType.INTEGER);
			else if (Literal.isFloat(next))
				literal = new Literal<Float>(Float.parseFloat(next), LiteralType.FLOAT);
			else
				literal = new Literal<Boolean>(Boolean.parseBoolean(next), LiteralType.BOOLEAN);
		}

		// else the next is a instruction
		else {
			programType = ProgramType.INSTRUCTION;
			instruction = instructionFactory.getInstruction(next);
		}
	}


	private void parseSubProgram(LinkedList<String> programStringSplit) throws ParseException {

		if ( programStringSplit.size() < 1)
			throw new ParseException("Unmatched braces");

		// remove the last brace
		String end = programStringSplit.removeLast();

		if ( !end.equals(")") )
			throw new ParseException("Unmatched braces");

		int bracesCount = 0;
		String subProgramString = "";

		for (String s : programStringSplit) {

			if (s.equals("(")) {
				bracesCount++;
				subProgramString += (s + " ");
			}

			else if (s.equals(")")) {
				bracesCount--;
				subProgramString += (s + " ");

				if (bracesCount == 0) {
					this.subProgram.add(new Program(subProgramString));
					subProgramString = "";
				}
			}

			else if (bracesCount != 0) {
				subProgramString += (s + " ");
			}

			else  {
				this.subProgram.add(new Program(s));
			}
		}

		if ( bracesCount != 0 ) {
			throw new ParseException("Unmatched braces");
		}

	}

	public ProgramType getProgramType() {
		return programType;
	}

	public Literal getLiteral() {
		return this.literal;
	}

	public Instruction getInstruction() {
		return this.instruction;
	}

	public ArrayDeque<Program> getSubProgram() {
		return this.subProgram;
	}

	public String toString() {

		String str = "";

		switch (programType) {

		case LITERAL:
			str += literal.toString();
			break;

		case INSTRUCTION:
			str += instruction.toString();
			break;

		case SUBPROGRAM:

			str += "( ";

			for (Program p : subProgram)
				str += p.toString();

			str += ")";

			break;
		}

		return str + " ";
	}

	public static void main(String[] args) {

		String str = "( 5 5 INTEGER.+ ( ( 2 INTEGER.* ) 5 INTEGER.- ) )";

		try {
			Program program = new Program(str);
			System.out.println(program.toString());
		} catch (ParseException e) {
			System.out.println("Error in given Push program: " + e.getMessage() + " aborting.");
			System.exit(0);
		}
	}
}