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
	private Stack<Boolean> booleanStack;

	private int steps;
	private Boolean noop = false;

	PushInterpreter(String programString) {

		Program program = loadProgram(programString);

		execStack = new Stack<Program>();
		integerStack = new Stack<Integer>();
		floatStack = new Stack<Float>();
		booleanStack = new Stack<Boolean>();

		execStack.push(program);
	}


	private Program loadProgram(String str) {

		Program program = null;
		steps = 0;

		try {
			program = new Program(str);
		} catch (ParseException e) {
			System.out.println("Error in given Push program: " + e.getMessage() + " aborting.");
			System.exit(0);
		}

		return program;
	}


	public void run() {

		steps = 0;

		while (!execStack.isEmpty()) {
			step();
		}
	}


	public void runInteractive() {

		steps = 0;

		while (!execStack.isEmpty()) {
			clearDisplay();
			display();
			Utils.waitForKeyPress();
			step();
		}

		clearDisplay();
		display();
		System.out.println();
	}

	public void step() {

		Program next = execStack.pop();
		noop = false;
		steps++;

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
			case BOOLEAN:
				booleanStack.push((Boolean)literal.getValue());
				break;
			}

			break;

		case INSTRUCTION:
			Instruction instruction = next.getInstruction();
			noop = !instruction.execute(this);
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
		Stack<?> booleanTmp = (Stack<?>) booleanStack.clone();

		int stackWidth = 10;
		int spacing = 5;

		while (!execTmp.isEmpty() || !integerTmp.isEmpty() || !floatTmp.isEmpty()  || !booleanTmp.isEmpty() ) {

			int largestStackSize = getLargestStackSize(execTmp, integerTmp, floatTmp, booleanTmp);
			String line = "";

			if (execTmp.size() == largestStackSize) {

				Program program = (Program)execTmp.pop();

				switch (program.getProgramType()) {

				case INSTRUCTION :
					line += Utils.padString(program.toString(), stackWidth );
					break;

				case LITERAL :
					line += Utils.padString(program.toString(), stackWidth );
					break;

				case SUBPROGRAM :
					line += Utils.padString("P(...)", stackWidth );
					break;
				}
			} else {
				line += Utils.padString("", stackWidth );
			}

			line += Utils.padString("", spacing );

			if (integerTmp.size() == largestStackSize) {
				line += Utils.padString(integerTmp.pop().toString(), stackWidth);
			} else {
				line += Utils.padString("", stackWidth );
			}

			line += Utils.padString("", spacing );

			if (floatTmp.size() == largestStackSize) {
				line += Utils.padString(floatTmp.pop().toString(), stackWidth);
			} else {
				line += Utils.padString("", stackWidth );
			}

			line += Utils.padString("", spacing );

			if (booleanTmp.size() == largestStackSize) {
				line += Utils.padString(booleanTmp.pop().toString(), stackWidth);
			} else {
				line += Utils.padString("", stackWidth );
			}

			System.out.println(line);
		}

		System.out.println( Utils.padString("EXEC", stackWidth) + Utils.padString("", spacing) +
		                    Utils.padString("INTEGER", stackWidth) + Utils.padString("", spacing) +
		                    Utils.padString("FLOAT", stackWidth) + Utils.padString("", spacing) +
		                    Utils.padString("BOOLEAN", stackWidth));


		for (int i = 0; i < 80; i++)
			System.out.print("-");

		System.out.println();

		System.out.print("Steps: " + steps + Utils.padString("", spacing) + (noop ? "NO-OP" : " ") );
	}




	private int getLargestStackSize(Stack<?>... args) {

		ArrayList<Integer> stackSizes = new ArrayList<Integer>();

		for (Stack<?> arg : args)
			stackSizes.add(arg.size());

		return Collections.max(stackSizes);
	}

	public Stack<Program> getExecStack() {
		return execStack;
	}

	public Stack<Integer> getIntegerStack() {
		return integerStack;
	}

	public Stack<Float> getFloatStack() {
		return floatStack;
	}

	public Stack<Boolean> getBooleanStack() {
		return booleanStack;
	}


	public static void main(String[] args) {

		String str;
		PushInterpreter interpreter;

		if (args.length == 0 ) {
			str = "( TRUE 0.7 5 5 INTEGER.+ ( ( 2 INTEGER.* ) 5 INTEGER.- ) FALSE 2 INTEGER./ INTEGER.% 5 INTEGER.> 3 3 INTEGER.= )";
		} else {
			str = args[0];
		}

		interpreter = new PushInterpreter(str);

		interpreter.runInteractive();

		//interpreter.step();
		//interpreter.display();
	}
}