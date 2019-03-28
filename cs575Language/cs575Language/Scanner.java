package cs575Language;

import java.io.IOException;

public class Scanner extends Types
{
	String fname;
	Lexeme lex, lex1;
	Parser p;
	int depth = 0;
	
	public Scanner(String fileName) throws IOException
	{
		fname = fileName;
	}
	
	public void runParser() throws IOException
	{
		p = new Parser(fname);
		lex = p.parse();
		@SuppressWarnings("unused")
		int a = 0;
	}
	
	public void runLexer() throws IOException
	{
		Lexer lexer = new Lexer(fname);
		lex1 = lexer.Lex();
		while (lex1.T != Type.ENDofINPUT)
		{
			System.out.println(lex1.type + " " + lex1.sval);
			lex1 = lexer.Lex();
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		Scanner sc = new Scanner("classes.cpp");
		//sc.runLexer();
		sc.runParser();
		//sc.pp(sc.lex);
	}
	
	public void pp(Lexeme pt)
	{
		depth++;
		if ("PROGRAM".equals(pt.type)) {
			printProgram(pt);
		} else if ("CLASS".equals(pt.type)) {
			printClass(pt);
		} else if ("METHOD_DECLARATIONS".equals(pt.type)) {
			printMethodDec(pt);
		} else if ("FIELD_LIST".equals(pt.type)) {
			printFieldList(pt);
		} else if ("FIELD_DECLARATION".equals(pt.type)) {
			printFieldDec(pt);
		} else if ("VARIABLE_DECLARATION".equals(pt.type)) {
			printVarDec(pt);
		} else if ("MORE_VARIABLES".equals(pt.type)) {
			printMoreVars(pt);
		} else if ("PRIMITIVE_DECLARATION".equals(pt.type)) {
			printPrimDec(pt);
		} else if ("VARIABLE".equals(pt.type)) {
			printVar(pt);
		} else if ("DIMENSIONS".equals(pt.type)) {
			printDims(pt);
		} else if ("METHOD_LIST".equals(pt.type)) {
			printMethodList(pt);
		} else if ("FUNCTION_DECLARATION".equals(pt.type)) {
			printFuncDec(pt);
		} else if ("PARAM_LIST".equals(pt.type)) {
			printParamList(pt);
		} else if ("PARAM".equals(pt.type)) {
			printParam(pt);
		} else if ("BLOCK".equals(pt.type)) {
			printBlock(pt);
		} else if ("DECLARATION_OR_STATEMENT".equals(pt.type)) {
			printDecOrState(pt);
		} else if ("IF".equals(pt.type)) {
			printIf(pt);
		} else if ("WHILE".equals(pt.type)) {
			printWhile(pt);
		} else if ("DO_WHILE".equals(pt.type)) {
			printDoWhile(pt);
		} else if ("JUMP".equals(pt.type)) {
			printJump(pt);
		} else if ("INPUT".equals(pt.type)) {
			printInput(pt);
		} else if ("OUTPUT".equals(pt.type)) {
			printOutput(pt);
		} else if ("EXPRESSION_STATEMENT".equals(pt.type)) {
			printExprState(pt);
		} else if ("ASSIGNMENT".equals(pt.type)) {
			printAssignment(pt);
		} else if ("REST_OF_EXPRESSION_STATEMENT".equals(pt.type)) {
			printRestOfExprState(pt);
		} else if ("EXPRESSION".equals(pt.type)) {
			printExpr(pt);
		} else if ("CONDITIONAL".equals(pt.type)) {
			printConditional(pt);
		} else if ("DISJUNCTION".equals(pt.type)) {
			printDisjunction(pt);
		} else if ("CONJUNCTION".equals(pt.type)) {
			printConjunction(pt);
		} else if ("EQUALITY".equals(pt.type)) {
			printEquality(pt);
		} else if ("INEQUALITY".equals(pt.type)) {
			printInequality(pt);
		} else if ("SUM".equals(pt.type)) {
			printSum(pt);
		} else if ("PRODUCT".equals(pt.type)) {
			printProduct(pt);
		} else if ("PRIMARY".equals(pt.type)) {
			printPrimary(pt);
		} else if ("INTEGER".equals(pt.type)) {
			printInteger(pt);
		} else if ("STRING".equals(pt.type)) {
			printString(pt);
		} else if ("ID".equals(pt.type)) {
			printId(pt);
		} else if ("INT".equals(pt.type)) {
			printInt(pt);
		} else if ("BOOL".equals(pt.type)) {
			printBool(pt);
		} else if ("VOID".equals(pt.type)) {
			printVoid(pt);
		} else if ("PLUS".equals(pt.type)) {
			printPlus(pt);
		} else if ("MINUS".equals(pt.type)) {
			printMinus(pt);
		} else if ("STAR".equals(pt.type)) {
			printStar(pt);
		} else if ("SLASH".equals(pt.type)) {
			printSlash(pt);
		} else if ("MODULO".equals(pt.type)) {
			printModulo(pt);
		} else if ("EQUALS".equals(pt.type)) {
			printEqual(pt);
		} else if ("NOTEQUAL".equals(pt.type)) {
			printNotEqual(pt);
		} else if ("GTHAN".equals(pt.type)) {
			printGthan(pt);
		} else if ("GTHANEQUAL".equals(pt.type)) {
			printGthanEqual(pt);
		} else if ("LTHAN".equals(pt.type)) {
			printLthan(pt);
		} else if ("LTHANEQUAL".equals(pt.type)) {
			printLthanEqual(pt);
		} else if ("DOT_CALL".equals(pt.type)) {
			printDotCall(pt);
		} else if ("ENDL".equals(pt.type)) {
			printEndl(pt);
		} else if ("ARG_LIST".equals(pt.type)) {
			printArgsList(pt);
		} else if ("ARG".equals(pt.type)) {
			printArg(pt);
		} else if ("MORE_ARGS".equals(pt.type)) {
			printMoreArgs(pt);
		} else if ("AND_STATEMENT".equals(pt.type)) {
			printAndState(pt);
		} else if ("OR_STATEMENT".equals(pt.type)) {
			printOrState(pt);
		} else if ("AND".equals(pt.type)) {
			printAnd(pt);
		} else if ("OR".equals(pt.type)) {
			printOr(pt);
		} else if ("ARRAY_DIMENSIONS".equals(pt.type)) {
			printArrayDims(pt);
		} else if ("DECLARATION_LIST".equals(pt.type)) {
			printDecList(pt);
		} else if ("DECLARATION".equals(pt.type)) {
			printDec(pt);
		}
		depth--;
	}
	
	private void printProgram(Lexeme pt) {
		if (pt.left != null) pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}
	
	private void printDecList(Lexeme pt) {
		pp(pt.left);
		pp(pt.right);
	}
	
	private void printDec(Lexeme pt) {
		pp(pt.left);
		pp(pt.right);
	}

	private void printClass(Lexeme pt) {
		System.out.print("class ");
		pp(pt.left);
		if (pt.right.left != null)
		{
			System.out.print(" : public ");
			pp(pt.right.left.left);
		}
		System.out.println();
		System.out.println("{");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
		pp(pt.right.right.left);
		System.out.print("\n}");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
	}

	private void printMethodDec(Lexeme pt) {
		pp(pt.left);
		System.out.print("\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
		System.out.print("public: \n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
		if (pt.right != null) pp(pt.right);
	}

	private void printFieldList(Lexeme pt) {
		pp(pt.left);
		System.out.print(" ");
		if (pt.right != null) pp(pt.right);
	}

	private void printFieldDec(Lexeme pt) {
		pp(pt.left);
		System.out.print(" ");
		pp(pt.right);
	}

	private void printVarDec(Lexeme pt) {
		System.out.print(" ");
		pp(pt.left);
		if (pt.right != null)
		{
			//System.out.print(", ");
			pp(pt.right);
		}
		System.out.print(";\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
	}
	
	private void printMoreVars(Lexeme pt) {
		System.out.print(", ");
		pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}
	
	private void printPrimDec(Lexeme pt) {
		pp(pt.left);
		System.out.print(" ");
		if (pt.right != null)
		{
			pp(pt.right);
		}
	}

	private void printVar(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}

	private void printDims(Lexeme pt) {
		System.out.print("[");
		pp(pt.left);
		System.out.print("]");
		if (pt.right != null) pp(pt.right);
	}

	private void printMethodList(Lexeme pt) {
		pp(pt.left);
		System.out.println();
		if (pt.right.left != null) pp(pt.right);
	}

	private void printFuncDec(Lexeme pt) {
		pp(pt.left);
		System.out.print(" ");
		pp(pt.right.left);
		System.out.print("(");
		if (pt.right.right.left != null) pp(pt.right.right.left);
		System.out.print(")\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
		System.out.print("{\n");
		for (int i = 0; i < depth + 4; i+=2) System.out.print(" ");
		if (pt.right.right.right.left != null) pp(pt.right.right.right.left);
		System.out.print("\n}");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
	}

	private void printParamList(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}

	private void printParam(Lexeme pt) {
		pp(pt.left);
		System.out.print(" ");
		pp(pt.right);
	}

	private void printBlock(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}

	private void printDecOrState(Lexeme pt) {
		if (pt.left != null) pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}

	private void printIf(Lexeme pt) {
		System.out.print("if (");
		pp(pt.left);
		System.out.print(")\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
		pp(pt.right.left);
		if (pt.right.right.left != null)
		{
			System.out.print("else\n");
			for (int i = 0; i < depth; i+=2) System.out.print(" ");
			pp(pt.right.right.left);
		}
	}

	private void printWhile(Lexeme pt) {
		System.out.print("while (");
		pp(pt.left);
		System.out.print(")\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
		pp(pt.right.left);
	}

	private void printDoWhile(Lexeme pt) {
		System.out.print("do\n");
		pp(pt.left);
		System.out.print("while (");
		pp(pt.right.left);
		System.out.print(");\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
	}

	private void printJump(Lexeme pt) {
		System.out.print("return ");
		pp(pt.left);
		System.out.print(";\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
	}

	private void printInput(Lexeme pt) {
		System.out.print("cin >> ");
		pp(pt.left);
		if (pt.right.left != null) pp(pt.right);
		System.out.print(";\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
	}

	private void printOutput(Lexeme pt) {
		System.out.print("cout << ");
		pp(pt.left);
		System.out.print(";\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
	}

	private void printExprState(Lexeme pt) {
		pp(pt.left);
		pp(pt.right);
	}
	
	private void printRestOfExprState(Lexeme pt) {
		if (pt.left != null) pp(pt.left);
		if (pt.right != null) pp(pt.right); 
		System.out.print(";\n");
		for (int i = 0; i < depth; i+=2) System.out.print(" ");
	}

	private void printAssignment(Lexeme pt) {
		System.out.print(" = ");
		pp(pt.left);
	}

	private void printExpr(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}

	private void printConditional(Lexeme pt) {
		System.out.print(" ? ");
		pp(pt.left);
		System.out.print(" : ");
		pp(pt.right.left);
	}

	private void printDisjunction(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null)
		{
			System.out.print(" || ");
			pp(pt.right);
		}
	}

	private void printConjunction(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null)
		{
			pp(pt.right);
		}
	}

	private void printEquality(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null)
		{
			//System.out.print(" PLACEHOLDER CAUSE I KNOW IT'S WRONG ");
			pp(pt.right);
		}
	}

	private void printInequality(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null)
		{
			//System.out.print(" IT'S STILL WRONG ");
			pp(pt.right);
		}
	}

	private void printSum(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null)
		{
			//System.out.print(" STILL WRONG ");
			pp(pt.right);
		}
	}

	private void printProduct(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null)
		{
			//System.out.print(" WRONGO ");
			pp(pt.right);
		}
	}

	private void printPrimary(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null) 
		{
			//System.out.print(" I DON'T KNOW WHAT TO DO HERE ");
			pp(pt.right);
		}
	}
	
	private void printPlus(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(" + ");
		pp(pt.right);
	}
	
	private void printMinus(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(" - ");
		pp(pt.right);
	}
	
	private void printStar(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(" * ");
		pp(pt.right);
	}
	
	private void printSlash(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(" / ");
		pp(pt.right);
	}
	
	private void printModulo(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(" % ");
		pp(pt.right);
	}
	
	private void printEqual(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;	
		System.out.print(" == ");
		pp(pt.right);
	}
	
	private void printNotEqual(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;	
		System.out.print(" != ");
		pp(pt.right);
	}
	
	private void printGthan(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(" > ");
		pp(pt.right);
	}
	
	private void printGthanEqual(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(" >= ");
		pp(pt.right);
	}
	
	private void printLthan(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(" < ");
		pp(pt.right);
	}
	
	private void printLthanEqual(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(" <= ");
		pp(pt.right);
	}
	
	private void printDotCall(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print(".");
		pp(pt.right);
	}
	
	private void printArgsList(Lexeme pt) {
		@SuppressWarnings("unused")
		int a=0;
		System.out.print("(");
		if (pt.left != null) pp(pt.left);
		if (pt.right != null) pp(pt.right);
		System.out.print(")");
	}
	
	private void printArg(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}
	
	private void printMoreArgs(Lexeme pt) {
		System.out.print(", ");
		pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}
	
	private void printAndState(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}
	
	private void printOrState(Lexeme pt) {
		pp(pt.left);
		if (pt.right != null) pp(pt.right);
	}
	
	private void printAnd(Lexeme pt) {
		System.out.print(" && ");
	}
	
	private void printOr(Lexeme pt) {
		System.out.print(" || ");
	}
	
	private void printArrayDims(Lexeme pt) {
		System.out.print("[");
		pp(pt.left);
		System.out.print("]");
	}

	private void printInteger(Lexeme pt) {
		System.out.print(pt.ival);
	}

	private void printString(Lexeme pt) {
		System.out.print("\"" + pt.sval + "\"");
	}

	private void printId(Lexeme pt) {
		System.out.print(pt.sval);
	}
	
	private void printInt(Lexeme pt) {
		System.out.print(pt.sval);
	}
	
	private void printBool(Lexeme pt) {
		System.out.print(pt.sval);
	}
	
	private void printVoid(Lexeme pt) {
		System.out.print(pt.sval);
	}
	
	private void printEndl(Lexeme pt) {
		System.out.print(pt.sval);
	}
}
