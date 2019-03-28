package TuringMachine;

import java.io.IOException;

public class Scanner {

	public static void main(String[] args) throws IOException {
		TuringMachine tm = new TuringMachine("binaryAddition.txt", "abba");
//		TuringMachine palindromeTest = new TuringMachine("palindromeTest.txt");
//		TuringMachine binaryAddition = new TuringMachine("binaryAddition.txt");
//		TuringMachine binaryMultiplication = new TuringMachine("binaryMultiplication.txt");
		tm.simulate();
//		palindromeTest.simulate();
//		binaryAddition.simulate();
//		binaryMultiplication.simulate();
	}
}
