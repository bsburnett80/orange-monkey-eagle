import java.io.IOException;
import java.util.ArrayList;

public class Recognizer extends Types{
	
	String fname = "";
	Lexeme currentLexeme;
	Lexer i;
	Environment e = new Environment();
	
	public Recognizer(String filename) throws IOException
	{
		fname = filename;
		i = new Lexer(fname);
	}
	
	public void run() throws IOException
	{
		currentLexeme = i.Lex();
		
		@SuppressWarnings("unused")
		Lexeme result = runRecognizer();
	}
	
	public Lexeme runRecognizer() throws IOException
	{
		currentLexeme = i.Lex();
		
		if (classPending())
		{
			return isClass();
		}
		else
		{
			System.out.println("ILLEGAL");
			System.out.println("Class definition missing");
			return null;
		}
	}
	
	private Lexeme isClass()
	{
		if (classPending())
		{
			match(Keywords.CLASS);
			Lexeme name = match(Keywords.ID);
			match(Punct.OBRACE);
			Lexeme body = block();
			body.type = "BODY";
			match(Punct.CBRACE);
			Lexeme result = e.cons(name, e.cons(body, null));
			result.type = "CLASS";
			return result;
		}
		else return null;
	}
	
	private boolean classPending()
	{
		return check(Keywords.CLASS);
	}
	
	private Lexeme block()
	{
		return optStatementSeq();
	}
	
	private Lexeme optStatementSeq()
	{
		if (statementSeqPending())
		{
			return statementSeq();
		}
		else return null;
	}
	
	private boolean statementSeqPending()
	{
		return embeddedStatementPending();
	}
	
	private Lexeme statementSeq()
	{
		return e.cons(statement(), optStatementSeq());
	}
	
	private Lexeme statement()
	{
		if (embeddedStatementPending())
		{
			return embeddedStatement();
		}
		else
		{
			return null;
		}
	}
	
	private boolean embeddedStatementPending()
	{
		return ifStatementPending() || iterationStatementPending() ||
				expressionPending() || functionDefPending() || returnPending();
	}
	
	private Lexeme embeddedStatement()
	{	
		if (ifStatementPending())
		{
			return ifStatement();
			
		}
		else if (iterationStatementPending())
		{
			return iterationStatement();
		}
        else if (functionDefPending())
        {
            return functionDef();
        }
        else if (returnPending())
        {
        	return returnStatement();
        }
		else
		{
			return expression();
		}
	}
	
	private boolean ifStatementPending()
	{
		return check(Keywords.IF);
	}
	
	private Lexeme ifStatement()
	{
		Lexeme result = new Lexeme();
		match(Keywords.IF);
		match(Punct.OPAREN);
		Lexeme boolExpr = boolExpression();
		if (check(Operator.AND) || check(Operator.OR))
		{
			if (check(Operator.AND))
			{
				Lexeme op = match(Operator.AND);
				op.type = "AND";
				op.left = boolExpr;
				Lexeme boolExpr2 = boolExpression();
				op.right = boolExpr2;
				match(Punct.CPAREN);
				match(Punct.OBRACE);
				Lexeme body1 = block();
				body1.type = "BODY";
				match(Punct.CBRACE);
				if (check(Keywords.ELSE))
				{
					match(Keywords.ELSE);
					match(Punct.OBRACE);
					Lexeme body2 = block();
					body2.type = "BODY";
					match(Punct.CBRACE);
					result = e.cons(op, e.cons(body1, e.cons(body2, null)));
					result.type = "IF-ELSE";
				}
				else
				{
					result = e.cons(op, e.cons(body1, null));
					result.type = "IF";
				}
			}
			else if (check(Operator.OR))
			{
				Lexeme op = match(Operator.OR);
				op.type = "OR";
				op.left = boolExpr;
				Lexeme boolExpr2 = boolExpression();
				op.right = boolExpr2;
				match(Punct.CPAREN);
				match(Punct.OBRACE);
				Lexeme body1 = block();
				body1.type = "BODY";
				match(Punct.CBRACE);
				if (check(Keywords.ELSE))
				{
					match(Keywords.ELSE);
					match(Punct.OBRACE);
					Lexeme body2 = block();
					body2.type = "BODY";
					match(Punct.CBRACE);
					result = e.cons(op, e.cons(body1, e.cons(body2, null)));
					result.type = "IF-ELSE";
				}
				else
				{
					result = e.cons(op, e.cons(body1, null));
					result.type = "IF";
				}
			}
		}
		else
		{
			match(Punct.CPAREN);
			match(Punct.OBRACE);
			Lexeme body1 = block();
			body1.type = "BODY";
			match(Punct.CBRACE);
			if (check(Keywords.ELSE))
			{
				match(Keywords.ELSE);
				match(Punct.OBRACE);
				Lexeme body2 = block();
				body2.type = "BODY";
				match(Punct.CBRACE);
				result = e.cons(boolExpr, e.cons(body1, e.cons(body2, null)));
				result.type = "IF-ELSE";
			}
			else
			{
				result = e.cons(boolExpr, e.cons(body1, null));
				result.type = "IF";
			}
		}
		
		return result;
	}
	
	private boolean iterationStatementPending()
	{
		return whileStatementPending();
	}
	
	private Lexeme iterationStatement()
	{
		if (whileStatementPending())
		{
			return whileStatement();
		}
		else
		{
			return null;
		}
	}
	
	private boolean whileStatementPending()
	{
		return check(Keywords.WHILE);
	}
	
	private Lexeme whileStatement()
	{
		match(Keywords.WHILE);
		match(Punct.OPAREN);
		Lexeme boolExpr = boolExpression();
		Lexeme result = new Lexeme("WHILESTATEMENT");
		if (check(Operator.AND) || check(Operator.OR))
		{
			if (check(Operator.AND))
			{
				Lexeme op = match(Operator.AND);
				op.type = "AND";
				op.left = boolExpr;
				Lexeme boolExpr2 = boolExpression();
				op.right = boolExpr2;
				match(Punct.CPAREN);
				match(Punct.OBRACE);
				Lexeme body = block();
				body.type = "BODY";
				match(Punct.CBRACE);
				result = e.cons(op, e.cons(body, null));
			}
			else if (check(Operator.OR))
			{
				Lexeme op = match(Operator.OR);
				op.type = "OR";
				op.left = boolExpr;
				Lexeme boolExpr2 = boolExpression();
				op.right = boolExpr2;
				match(Punct.CPAREN);
				match(Punct.OBRACE);
				Lexeme body = block();
				body.type = "BODY";
				match(Punct.CBRACE);
				result = e.cons(op, e.cons(body, null));
			}
		}
		else
		{
			match(Punct.CPAREN);
			match(Punct.OBRACE);
			Lexeme body = block();
			body.type = "BODY";
			match(Punct.CBRACE);
			result = e.cons(boolExpr, e.cons(body, null));
			result.type = "WHILESTATEMENT";
		}
		
		result.type = "WHILESTATEMENT";
		return result;
		
	}

    private Lexeme boolExpression()
    {
        return boolTest();
    }
	
	private Lexeme boolTest()
	{
		Lexeme name = expression();
		if (name.type == "ASSIGN") name.type = "EQUAL";
		return name;
	}
	
	private boolean operatorPending()
	{
		return check(Operator.PLUS)|| check(Operator.MINUS) || check(Operator.SLASH) ||
				check(Operator.STAR) || check(Operator.ASSIGN) || check(Operator.MODULO) ||
				check(Operator.AT) || check(Operator.DOT) || check(Operator.EXP) ||
				check(Operator.SET) || check(Operator.GTHAN)|| check(Operator.LTHAN) ||
				check(Operator.NOT);
	}
	
	private Lexeme operator()
	{
		Lexeme op = new Lexeme();
		
		if (check(Operator.PLUS))
		{
			op = match(Operator.PLUS);
		}
		else if (check(Operator.MINUS))
		{
			op = match(Operator.MINUS);
		}
		else if (check(Operator.SLASH))
		{
			op = match(Operator.SLASH);
		}
		else if (check(Operator.STAR))
		{
			op = match(Operator.STAR);
		}
		else if (check(Operator.MODULO))
		{
			op = match(Operator.MODULO);
		}
		else if (check(Operator.ASSIGN))
		{
			op = match(Operator.ASSIGN);
		}
		else if (check(Operator.AT))
		{
			op = match(Operator.AT);
		}
		else if (check(Operator.DOT))
		{
			op = match(Operator.DOT);
		}
		else if (check(Operator.EXP))
		{
			op = match(Operator.EXP);
		}
		else if (check(Operator.SET))
		{
			op = match(Operator.SET);
		}
		else if (check(Operator.GTHAN))
		{
			op = match(Operator.GTHAN);
		}
		else if (check(Operator.LTHAN))
		{
			op = match(Operator.LTHAN);
		}
		else if (check(Operator.NOT))
		{
			op = match(Operator.NOT);
		}
		
		return op;
	}

    private boolean functionDefPending()
    {
        return check(Keywords.DEFINE);
    }

    private Lexeme functionDef()
    {
        match(Keywords.DEFINE);
        match(Keywords.FUNCTION);
        Lexeme name = match(Keywords.ID);
        match(Punct.OPAREN);
        Lexeme params = optParamList();
        match(Punct.CPAREN);
        match(Punct.OBRACE);
        Lexeme body = block();
        body.type = "BODY";
        match(Punct.CBRACE);
        Lexeme s = e.cons(name, e.cons(params, e.cons(body, null)));
        s.type = "FUNCTIONDEF";
        return s;
    }

    private Lexeme optParamList()
    {
        if (paramListPending())
        {
        	Lexeme paramList = paramList();
        	paramList.type = "PARAMS";
        	return paramList;
        }
        else return null;
    }

    private boolean paramListPending()
    {
        return check(Keywords.ID);
    }

    private Lexeme paramList()
    {
        Lexeme name = match(Keywords.ID);
        if (check(Punct.COMMA))
        {
            match(Punct.COMMA);
            return e.cons(name, paramList());
        }
        else return e.cons(name, null);
    }
    
    private boolean returnPending()
    {
    	return check(Keywords.RETURN);
    }
    
    private Lexeme returnStatement()
    {
    	match(Keywords.RETURN);
    	Lexeme ret = new Lexeme("RETURN");
    	ret.left = expression();
    	
    	return ret;
    }

	private boolean expressionPending()
	{
		return primaryPending();
	}
	
	private Lexeme expression()
	{
		if (primaryPending())
		{
			Lexeme p = primary();
			if (operatorPending())
			{
				Lexeme op1 = operator();
				if (op1.type == "DOT")
				{
					op1.left = p;
					Lexeme id = dotFuncCall();
					op1.right = id;
					return op1;
				}
				Lexeme expr = expression();
				op1.left = p;
				op1.right = expr;
				return op1;
			}
			else
			{
				Lexeme result = p;
				return result;
			}
		}
		else return null;
	}
	
	private boolean primaryPending()
	{
		return check(Type.INTEGER) || check(Type.REAL) ||
				check(Type.STRING) || check(Keywords.ID) ||
				check(Punct.OPAREN) || functionCallPending() ||
				arrayPending() || printPending() || printLnPending() ||
				setArrayPending() || nullPending();
	}
	
	private Lexeme primary()
	{
		if (check(Type.INTEGER))
		{
			return match(Type.INTEGER);
		}
		else if (check(Type.STRING))
		{
			return match(Type.STRING);
		}
		else if (check(Type.REAL))
		{
			return match(Type.REAL);
		}
		else if (check(Punct.OPAREN))
		{
			match(Punct.OPAREN);
			Lexeme t = expression();
			match(Punct.CPAREN);
			return t;
		}
		else if (check(Keywords.ID))
		{
			return match(Keywords.ID);
		}
		else if (functionCallPending())
		{
			return functionCall();
		}
		else if (arrayPending())
		{
			return array();
		}
		else if (setArrayPending())
		{
			return setArray();
		}
		else if (printPending())
		{
			return print();
		}
		else if (printLnPending())
		{
			return printLn();
		}
		else if (nullPending())
		{
			return isNull();
		}
		else return null;
	}
	
	private boolean functionCallPending()
	{
		return check(Keywords.FUNCTION);
	}
	
	private boolean arrayPending()
	{
		return check(Keywords.ARRAY);
	}
	
	private boolean setArrayPending()
	{
		return check(Keywords.SETARRAY);
	}
	
	private boolean printPending()
	{
		return check(Keywords.PRINT);
	}
	
	private boolean printLnPending()
	{
		return check(Keywords.PRINTLN);
	}
	
	private boolean nullPending()
	{
		return check(Keywords.NULL);
	}
	
	private Lexeme functionCall()
	{
		match(Keywords.FUNCTION);
		Lexeme name = match(Keywords.ID);
		match(Punct.OPAREN);
		Lexeme args = optArgList();
		match(Punct.CPAREN);
		Lexeme s = e.cons(name, e.cons(args, null));
		s.type = "FUNCTIONCALL";
		return s;
	}
	
	private Lexeme dotFuncCall()
	{
		Lexeme name = match(Keywords.ID);
		Lexeme s = new Lexeme();
		if (check(Punct.OPAREN))
		{
			match(Punct.OPAREN);
			Lexeme args = optArgList();
			match(Punct.CPAREN);
			s = e.cons(name, e.cons(args, null));
			s.type = "DOTFUNCCALL";
			return s;
		}
		s.left = name;
		s.type = "DOTVARCALL";
		return s;
	}
	
	private Lexeme array()
	{
		match(Keywords.ARRAY);
		Lexeme name = match(Keywords.ID);
		Lexeme op = match(Operator.ASSIGN);
		op.left = name;
		match(Punct.OBRACKET);
		Lexeme args = argList();
		args.type = "ARRAYARGS";
		op.right = args;
		match(Punct.CBRACKET);
		args.aval = addToList(args);
		op.type = "ARRAY";
		return op;
	}
	
	private ArrayList<Lexeme> addToList(Lexeme pt)
	{
		ArrayList<Lexeme> list = new ArrayList<Lexeme>();
		int i = 0;
		while (pt != null)
		{
			list.add(i, pt.left);
			pt = pt.right;
			i+=1;
		}
		
		return list;
	}
	
	private Lexeme setArray()
	{
		match(Keywords.SETARRAY);
		Lexeme name = match(Keywords.ID);
		Lexeme location = match(Operator.SET);
		location.left = name;
		Lexeme index = primary();
		location.right = index;
		Lexeme op = match(Operator.ASSIGN);
		op.left = location;
		op.right = primary();
		op.type = "SETARRAY";
		
		return op;
	}
	
	private Lexeme print()
	{
		match(Keywords.PRINT);
		match(Punct.OPAREN);
		Lexeme expr = expression();
		match(Punct.CPAREN);
		
		Lexeme result = e.cons(expr, null);
		result.type = "PRINT";
		
		return result;
	}
	
	private Lexeme printLn()
	{
		match(Keywords.PRINTLN);
		match(Punct.OPAREN);
		Lexeme expr = expression();
		match(Punct.CPAREN);
		
		Lexeme result = e.cons(expr, null);
		result.type = "PRINTLN";
		
		return result;
	}
	
	private Lexeme isNull()
	{
		return match(Keywords.NULL);
	}
	
	private Lexeme optArgList()
	{
		if (argListPending())
		{
			Lexeme argList = argList();
			argList.type = "ARGS";
			return argList;
		}
		else return null;
	}
	
	private boolean argListPending()
	{
		return argPending();
	}
	
	private Lexeme argList()
	{
		Lexeme a = arg();
		if (check(Punct.COMMA))
		{
			match(Punct.COMMA);
			return e.cons(a, argList());
		}
		else return e.cons(a, null);
	}
	
	private boolean argPending()
	{
		return primaryPending();
	}
	
	private Lexeme arg()
	{
		return primary();
	}
	
	private void advance() throws IOException
	{
		currentLexeme = i.Lex();
	}
	
	private boolean check(Keywords k)
	{
		return (currentLexeme.k == k);
	}
	
	private boolean check(Type t)
	{
		return (currentLexeme.T == t);
	}
	
	private boolean check(Punct p)
	{
		return (currentLexeme.punct == p);
	}
	
	private boolean check(Operator o)
	{
		return (currentLexeme.op == o);
	}
	
	private Lexeme match(Keywords w)
	{
		Lexeme prevLex = currentLexeme;
		matchNoAdvance(w);
		try {
			advance();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prevLex;
	}
	
	private Lexeme match(Type t)
	{
		Lexeme prevLex = currentLexeme;
		matchNoAdvance(t);
		try {
			advance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prevLex;
	}
	
	private void match(Punct p)
	{
		matchNoAdvance(p);
		try {
			advance();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Lexeme match(Operator o)
	{
		Lexeme prevLex = currentLexeme;
		matchNoAdvance(o);
		try {
			advance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prevLex;
	}
	
	private void matchNoAdvance(Keywords w)
	{
		if (!check(w))
		{
			System.out.println("String ILLEGAL");
			System.out.println("A keyword is invalid. Current:" + currentLexeme.type + " Expected: " + w);
			System.exit(-1);
		}
	}
	
	private void matchNoAdvance(Type t)
	{
		if (!check(t))
		{
			System.out.println("String ILLEGAL");
			System.out.println("The type is invalid. Current:" + currentLexeme.type + " Expected: " + t);
			System.exit(-1);
		}
	}
	
	private void matchNoAdvance(Punct p)
	{
		if (!check(p))
		{
			System.out.println("ILLEGAL");
			System.out.println("The punctuation is invalid. Current:" + currentLexeme.type + " Expected: " + p);
			System.exit(-1);
		}
	}
	
	private void matchNoAdvance(Operator o)
	{
		if (!check(o))
		{
			System.out.println("ILLEGAL");
			System.out.println("The operator is invalid. Current: " + currentLexeme.type + " Expected: " + o);
			System.exit(-1);
		}
	}
}
