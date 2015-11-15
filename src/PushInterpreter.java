import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.Stack;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;


class PushInterpreter {

	private Stack<Program> execStack;
	private Stack<Integer> integerStack;
	private Stack<Float> floatStack;

	PushInterpreter(String programString) {

		Program program = loadProgram(programString);

		execStack = new Stack<Program>();
		integerStack = new Stack<Integer>();
		floatStack = new Stack<Float>();

		execStack.push(program);
	}


	private Program loadProgram(String str) {

		Program program = null;

		try {
			program = new Program(str);
			//System.out.println(program.toString());

		} catch (ParseException e) {
			System.out.println("Error in given Push program... aborting.");
			System.exit(0);
		}

		return program;
	}


	public void run() {

		while (!execStack.isEmpty()) {
			step();
		}

	}


	public void runInteractive() {

		while (!execStack.isEmpty()) {
			clearDisplay();
			display();
			Utils.waitForKeyPress();
			step();
		}

		clearDisplay();
		display();
	}

	public void step() {

		Program next = execStack.pop();

		switch (next.getProgramType()) {

		case LITERAL:
			Literal literal = next.getLiteral();

			switch (literal.getType()) {

			case INTEGER :
				integerStack.push((int)literal.getValue());
				break;

			case FLOAT:
				floatStack.push((float)literal.getValue());
				break;
			}

			break;

		case INSTRUCTION:
			break;

		case SUBPROGRAM:

			ArrayDeque<Program> subProgram = next.getSubProgram();

			while (	!subProgram.isEmpty() ) {
				Program tmp = subProgram.removeLast();
				execStack.push(tmp);
			}

			break;
		}
	}

	private void clearDisplay() {
		for (int i = 0; i < 100; i++) {
			System.out.println();
		}
	}

	public void display() {

		Stack<?> execTmp = (Stack<?>) execStack.clone();
		Stack<?> integerTmp = (Stack<?>) integerStack.clone();
		Stack<?> floatTmp = (Stack<?>) floatStack.clone();

		int spacing = 10;

		while (!execTmp.isEmpty() || !integerTmp.isEmpty() || !floatTmp.isEmpty()) {

			int largestStackSize = getLargestStackSize(execTmp, integerTmp, floatTmp);
			String line = "";

			if (execTmp.size() == largestStackSize) {

				Program program = (Program)execTmp.pop();

				switch (program.getProgramType()) {

				case INSTRUCTION :
					line += Utils.padString(program.toString(), spacing );
					break;

				case LITERAL :
					line += Utils.padString(program.toString(), spacing );
					break;

				case SUBPROGRAM :
					line += Utils.padString("P(...)", spacing );
					break;
				}
			} else {
				line += Utils.padString("", spacing );
			}

			line += Utils.padString("", spacing );

			if (integerTmp.size() == largestStackSize) {
				line += Utils.padString(integerTmp.pop().toString(), spacing);
			} else {
				line += Utils.padString("", spacing );
			}

			line += Utils.padString("", spacing );

			if (floatTmp.size() == largestStackSize) {
				line += Utils.padString(floatTmp.pop().toString(), spacing);
			} else {
				line += Utils.padString("", spacing );
			}

			System.out.println(line);
		}

		System.out.println( Utils.padString("EXEC", spacing) + Utils.padString("", spacing) +
		                    Utils.padString("INTEGER", spacing) + Utils.padString("", spacing) +
		                    Utils.padString("FLOAT", spacing));
	}




	private int getLargestStackSize(Stack<?>... args) {

		ArrayList<Integer> stackSizes = new ArrayList<Integer>();

		for (Stack<?> arg : args) {
			stackSizes.add(arg.size());
		}

		return Collections.max(stackSizes);
	}


	public static void main(String[] args) {

		String str;
		PushInterpreter interpreter;

		if (args.length == 0 ) {
			str = "( 5 5 INTEGER. + ( ( 2 INTEGER.* ) 5 INTEGER. - ) )";
		} else {
			str = args[0];
		}

		interpreter = new PushInterpreter(str);

		interpreter.runInteractive();

		//interpreter.step();
		//interpreter.display();
	}
}