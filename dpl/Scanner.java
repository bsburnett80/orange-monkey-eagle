import java.io.IOException;

public class Scanner extends Types
{
	String fName;
	Recognizer rec;
	Environment env = new Environment();
	Lexeme lex;
	
	public Scanner(String fileName) throws IOException
	{
		fName = fileName;
	    rec = new Recognizer(fName);	
	}

	public static void main(String[] args) throws IOException
	{
        Scanner sc = new Scanner("functionTest.txt");
		sc.run();
	}
	
	public void run() throws IOException
	{		
		lex = rec.runRecognizer();
		if (lex != null) System.out.println("LEGAL");
		pp(lex);
		@SuppressWarnings("unused")
		Evaluator e = new Evaluator(lex, env.env);
	}
	
	public void pp(Lexeme pt)
	{
		@SuppressWarnings("unused")
		int a = 0;
		if (pt.type == "INTEGER") System.out.print(pt.ival);
		else if (pt.type == "STRING") System.out.print(pt.sval);
		else if (pt.type == "REAL") System.out.print(pt.rval);
		else if (pt.type == "ID") System.out.print(pt.sval);
		else if (pt.type == "FUNCTIONDEF") printFuncDef(pt);
		else if (pt.type == "FUNCTIONCALL") printFuncCall(pt);
		else if (pt.type == "CLASS") printClass(pt);
		else if (pt.type == "IF") printIf(pt);
		else if (pt.type == "IF-ELSE") printIfElse(pt);
		else if (pt.type == "WHILESTATEMENT") printWhile(pt);
		else if (pt.type == "PRINT") printPrint(pt);
		else if (pt.type == "PRINTLN") printPrintLn(pt);
		else if (pt.type == "ASSIGN" || pt.type == "EQUAL") printAssign(pt);
		else if (pt.type == "PLUS") printPlus(pt);
		else if (pt.type == "MINUS") printMinus(pt);
		else if (pt.type == "STAR") printStar(pt);
		else if (pt.type == "MODULO") printMod(pt);
		else if (pt.type == "SLASH") printSlash(pt);
		else if (pt.type == "GTHAN") printGthan(pt);
		else if (pt.type == "LTHAN") printLthan(pt);
		else if (pt.type == "NOT") printNot(pt);
		else if (pt.type == "AND") printAnd(pt);
		else if (pt.type == "OR") printOr(pt);
		else if (pt.type == "EXP") printExp(pt);
		else if (pt.type == "AT") printAt(pt);
		else if (pt.type == "DOT") printDot(pt);
		else if (pt.type == "SET") printSet(pt);
		else if (pt.type == "DOTFUNCCALL") printDotFuncCall(pt);
		else if (pt.type == "DOTVARCALL") printDotVarCall(pt);
		else if (pt.type == "PARAMS" || pt.type == "ARGS" || pt.type == "ARRAYARGS") printParams(pt, ", ");
		else if (pt.type == "ARRAY") printArray(pt);
		else if (pt.type == "SETARRAY") printSetArray(pt);
		else if (pt.type == "BODY") printBody(pt, "\n");
		else if (pt.type == "RETURN") printReturn(pt);
		else if (pt.type == "NULL") printNull(pt);
	}
	
	private void printFuncDef(Lexeme pt)
	{
		System.out.print("define function ");
		pp(pt.left);
		System.out.print("(");
		if (pt.right.left != null)
		{
			pp(pt.right.left);
		}
		System.out.print(")\n");
		System.out.print("\r{\n");
		if (pt.right.right.left != null)
		{
			pp(pt.right.right.left);
		}
		System.out.print("\r}\n");
	}
	
	private void printParams(Lexeme pt, String delimiter)
	{
		while (pt != null)
		{
			pp(pt.left);
			
			if (pt.right != null)
			{
				System.out.print(delimiter);
			}
			
			pt = pt.right;
		}
	}
	
	private void printBody(Lexeme pt, String delimiter)
	{
		while (pt != null)
		{
			pp(pt.left);
			if (pt.right != null)
			{
				System.out.print(delimiter);
			}
			pt = pt.right;
		}
	}
	
	private void printFuncCall(Lexeme pt)
	{
		System.out.print("function ");
		pp(pt.left);
		System.out.print("(");
		if (pt.right.left != null)
		{
			pp(pt.right.left);
		}
		System.out.print(")\n");
	}
	
	private void printClass(Lexeme pt)
	{
		System.out.print("class ");
		pp(pt.left);
		System.out.print("\r{\n");
		if (pt.right.left != null)
		{
			pp(pt.right.left);
		}
		System.out.print("\r}\n\n");
	}
	
	private void printIf(Lexeme pt) 
	{
		System.out.print("if (");
		pp(pt.left);
		System.out.print(")\r{\n");
		if (pt.right.left != null)
		{
			pp(pt.right.left);
		}
		System.out.print("\r}\n");
	}
	
	private void printIfElse(Lexeme pt) 
	{
		System.out.print("if (");
		pp(pt.left);
		System.out.print(")\r{\n");
		if (pt.right.left != null)
		{
			pp(pt.right.left);
		}
		System.out.print("\r}\n");
		System.out.print("else\n");
		System.out.print("\r{\n");
		if (pt.right.right.left != null)
		{
			pp(pt.right.right.left);
		}
		System.out.print("\r}\n");
	}
	
	private void printWhile(Lexeme pt) 
	{
		System.out.print("while (");
		pp(pt.left);
		System.out.print(")\r{\n");
		if (pt.right.left != null)
		{
			pp(pt.right.left);
		}
		System.out.print("\r}\n");
	}
	
	private void printPrint(Lexeme pt)
	{
		System.out.print("print(");
		pp(pt.left);
		System.out.print(")");
	}
	
	private void printPrintLn(Lexeme pt)
	{
		System.out.print("println(");
		pp(pt.left);
		System.out.print(")");
	}
	
	private void printAssign(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("=");
		pp(pt.right);
	}
	
	private void printPlus(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("+");
		pp(pt.right);
	}
	
	private void printMinus(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("-");
		pp(pt.right);
	}
	
	private void printStar(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("*");
		pp(pt.right);
	}
	
	private void printMod(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("%");
		pp(pt.right);
	}
	
	private void printSlash(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("/");
		pp(pt.right);
	}
	
	private void printGthan(Lexeme pt)
	{
		pp(pt.left);
		System.out.print(">");
		pp(pt.right);
	}
	
	private void printLthan(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("<");
		pp(pt.right);
	}
	
	private void printNot(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("!");
		pp(pt.right);
	}
	
	private void printAnd(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("&");
		pp(pt.right);
	}
	
	private void printOr(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("|");
		pp(pt.right);
	}
	
	private void printExp(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("^");
		pp(pt.right);
	}
	
	private void printAt(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("@");
		pp(pt.right);
	}
	
	private void printDot(Lexeme pt)
	{
		pp(pt.left);
		System.out.print(".");
		pp(pt.right);
	}
	
	private void printSet(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("$");
		pp(pt.right);
	}
	
	private void printDotFuncCall(Lexeme pt)
	{
		pp(pt.left);
		System.out.print("(");
		if (pt.right.left != null)
		{
			pp(pt.right.left);
		}
		System.out.print(")");
	}
	
	private void printDotVarCall(Lexeme pt)
	{
		pp(pt.left);
	}
	
	private void printArray(Lexeme pt)
	{
		System.out.print("array ");
		pp(pt.left);
		System.out.print("= [");
		pp(pt.right);
		System.out.print("]");
	}
	
	private void printSetArray(Lexeme pt)
	{
		System.out.print("set ");
		pp(pt.left);
		System.out.print("= ");
		pp(pt.right);
	}
	
	private void printReturn(Lexeme pt)
	{
		System.out.print("return ");
		pp(pt.left);
	}
	
	private void printNull(Lexeme pt)
	{
		System.out.print("null");
	}
}
