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

		LinkedList<String> programStringSplit = new LinkedList<String>( Arrays.asList(programString.split("\\s+")) );

		String next = programStringSplit.pop();

		// if the next begins a new program
		if (next.equals("(")) {
			programType = ProgramType.SUBPROGRAM;
			subProgram = new ArrayDeque<Program>();
			parseSubProgram(programStringSplit);
		}

		// if the next is a literal
		else if (isNumeric(next)) {
			programType = ProgramType.LITERAL;

			float tmp = Float.parseFloat(next);

			if ( tmp % 1 == 0 ) {
				//this.dataType = DataType.INTEGER;
				literal = new Literal<Integer>(Integer.parseInt(next), LiteralType.INTEGER);
			} else {
				literal = new Literal<Float>(Float.parseFloat(next), LiteralType.FLOAT);
				//this.dataType = DataType.FLOAT;
			}


		}

		// else the next is a instruction
		else {
			programType = ProgramType.INSTRUCTION;
			instruction = new Instruction(next);
		}
	}


	private void parseSubProgram(LinkedList<String> programStringSplit) throws ParseException {

		// remove the last brace
		programStringSplit.removeLast();


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
			throw new ParseException();
		}

	}

	public ProgramType getProgramType() {
		return programType;
	}

	public Literal getLiteral() {
		return this.literal;
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

	private static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}


	public static void main(String[] args) {

		String str = "( 5 5 INTEGER.+ ( ( 2 INTEGER.* ) 5 INTEGER.- ) )";

		try {
			Program program = new Program(str);
			System.out.println(program.toString());
		} catch (ParseException e) {

		}




	}

}