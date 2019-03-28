package cs575Language;

import java.io.*;
import java.util.ArrayList;

public class Parser extends Types {
	
	String fname = "";
	Lexeme currentLexeme;
	Lexer lex;
	int depth = 0;
	ArrayList<String> errors = new ArrayList<String>();
	
	public Parser(String filename) throws IOException
	{
		fname = filename;
		lex = new Lexer(fname);
	}
	
	public void run() throws IOException
	{
		currentLexeme = lex.Lex();
	}
	public Lexeme parse() throws IOException
	{
		currentLexeme = lex.Lex();
		
		if (!classPending() && !functionPending())
		{
			System.out.println("ILLEGAL");
			System.out.println("Class definition missing");
			return null;
		}
		Lexeme decList = null;
		
		while (true) {
			System.out.println("DeclarationList");
			decList = optDeclarationList();
			if (check(Type.ENDofINPUT) || !errors.isEmpty()) break;
		}
		
		if (!errors.isEmpty()) 
		{
			for (int i = 0; i < errors.size() - 1; i++) 
			{
				System.out.println(errors.get(i).trim());
			}
		}
		
		Lexeme result = Lexeme.node(decList, null);
		result.type = "PROGRAM";
		return result;
	}
	
	private Lexeme optDeclarationList()
	{
		if (declarationPending())
		{
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Declaration");
			Lexeme dec = declaration();
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("DeclarationList");
			Lexeme optDec = optDeclarationList();
			depth--;
			Lexeme result = Lexeme.node(dec, optDec);
			result.type = "DECLARATION_LIST";
			return result;
		}
		else return new Lexeme();
		
	}
	private boolean declarationPending()
	{
		return (classPending() || functionPending());
	}
	private Lexeme declaration()
	{
		Lexeme classDec = new Lexeme(), funcDec = new Lexeme();
		if (classPending()) classDec = classDeclaration();
		if (functionPending()) funcDec = functionDeclaration();
		Lexeme result = Lexeme.node(classDec, funcDec);
		result.type = "DECLARATION";
		return result;
	}
	
	private boolean classPending()
	{
		return check(Keywords.CLASS);
	}
	private Lexeme classDeclaration()
	{
		depth++;
		if (classPending())
		{
			match(Keywords.CLASS);
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("IDENTIFIER");
			Lexeme name = match(Keywords.ID), superClass = null;
			if (check(Punct.COLON))
			{
				depth++;
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("OptionalSuperClass");
				match(Punct.COLON);
				match(Keywords.PUBLIC);
				superClass = match(Keywords.ID);
				depth--;
			}
			match(Punct.OBRACE);
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("MethodDeclarations");
			Lexeme body = methodDeclarations();
			match(Punct.CBRACE);
			match(Punct.SEMICOLON);
			Lexeme result = Lexeme.node(name, Lexeme.node(superClass, Lexeme.node(body, null)));
			result.type = "CLASS";
			return result;
		}
		else {
			return null;
		}
	}
	private Lexeme methodDeclarations()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("FieldDeclarationList");
		Lexeme fieldList = optFieldDeclarationList();
		match(Keywords.PUBLIC);
		match(Punct.COLON);
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("MethodDeclarationList");
		Lexeme methodList = optMethodDeclarationList();
		Lexeme result = Lexeme.node(fieldList, methodList);
		result.type = "METHOD_DECLARATIONS";
		depth--;
		return result;
	}
	private Lexeme optFieldDeclarationList()
	{
		depth++;
		if (fieldDeclarationPending())
		{
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("FieldDeclaration");
			Lexeme fieldDec = fieldDeclaration();
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("FieldDeclarationList");
			Lexeme fieldList = optFieldDeclarationList();
			Lexeme result = Lexeme.node(fieldDec, fieldList);
			result.type = "FIELD_LIST";
			depth--;
			return result;
		}
		else return null;
	}
	private boolean fieldDeclarationPending()
	{
		return (check(Keywords.STATIC) || isTypeName());
	}
	private boolean isTypeName()
	{
		return (isPrimitiveType() || check(Keywords.ID));
	}
	private boolean isPrimitiveType()
	{
		return (check(Keywords.INT) || check(Keywords.BOOL));
	}
	private Lexeme fieldDeclaration()
	{
		depth++;
		if (check(Keywords.STATIC)) {
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("OptionalStatic");
			match(Keywords.STATIC);
			depth--;
		}
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("TypeName");
		Lexeme type = matchTypeName();
		Lexeme result = Lexeme.node(type, restOfVariableDeclaration());
		result.type = "FIELD_DECLARATION";
		depth--;
		return result;
	}
	private Lexeme restOfVariableDeclaration()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Variable");
		Lexeme var = variable(), moreVars = null;
		if (moreVariablesPending()) {
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("More Variables");
			moreVars = moreVariables();
			depth--;
		}
		match(Punct.SEMICOLON);
		Lexeme result = Lexeme.node(var, moreVars);
		result.type = "VARIABLE_DECLARATION";
		depth--;
		return result;
	}
	private Lexeme variable()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("IDENTIFIER");
		Lexeme name = match(Keywords.ID), dims = null;
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Dimensions");
		if (check(Punct.OBRACKET)) dims = dimensions();
		Lexeme result = Lexeme.node(name, dims);
		result.type = "VARIABLE";
		depth--;
		depth--;
		return result;
	}
	private Lexeme dimensions()
	{
		depth++;
		Lexeme moreDims = null;
		match(Punct.OBRACKET);
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("NUMBER");
		Lexeme dims = match(Type.INTEGER);
		match(Punct.CBRACKET);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Dimensions");
		if (check(Punct.OBRACKET)) {
			moreDims = dimensions();
		}
		depth--;
		Lexeme result = Lexeme.node(dims, moreDims);
		result.type = "DIMENSIONS";
		depth--;
		return result;
	}
	private boolean moreVariablesPending()
	{
		return (check(Punct.COMMA));
	}
	private Lexeme moreVariables()
	{
		depth++;
		match(Punct.COMMA);
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Variables");
		Lexeme vars = variable(), moreVars = null;
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Variables");
		if (moreVariablesPending()) {
			moreVars = moreVariables();
		}
		Lexeme result = Lexeme.node(vars, moreVars);
		result.type = "MORE_VARIABLES";
		depth--;
		depth--;
		return result;
	}
	
	// Method Declaration Section
	private Lexeme optMethodDeclarationList()
	{
		depth++;
		Lexeme methodDec = null, moreMethodDecs = null;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("MethodDeclaration");
		if (methodDeclarationPending())
		{
			methodDec = methodDeclaration();
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("MethodDeclarationList");
			moreMethodDecs = optMethodDeclarationList();
		}
		Lexeme result = Lexeme.node(methodDec,moreMethodDecs);
		result.type = "METHOD_LIST";
		depth--;
		return result;
	}
	private boolean methodDeclarationPending()
	{
		return (check(Keywords.STATIC) || functionPending());
	}
	private boolean functionPending()
	{
		return (isTypeName() || check(Keywords.VOID));
	}
	private Lexeme methodDeclaration()
	{
		depth++; depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("OptionalStatic");
		if (check(Keywords.STATIC)) {
			match(Keywords.STATIC);
			depth--; depth--;
		}
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("FunctionDeclaration");
		Lexeme f = functionDeclaration();
		depth--;
		return f;
	}
	
	// Function Declaration Section
	private Lexeme functionDeclaration()
	{
		Lexeme type = null;
		depth++; depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		if (check(Keywords.VOID)) {
			System.out.println("Void");
			type = match(Keywords.VOID);
			depth--;
		}
		else {
			System.out.println("Type Name");
			type = matchTypeName();
			depth--;
		}
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("IDENTIFIER");
		depth--;
		Lexeme name = match(Keywords.ID);
		match(Punct.OPAREN);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Optional Parameter List");
		depth--;
		Lexeme paramList = optParameterList();
		match(Punct.CPAREN);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		depth--;
		System.out.println("Block");
		Lexeme block = block();
		Lexeme result = Lexeme.node(type, Lexeme.node(name, Lexeme.node(paramList, Lexeme.node(block, null))));
		result.type = "FUNCTION_DECLARATION";
		return result;
	}
	private Lexeme matchTypeName() {
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("PrimitiveType");
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Int");
		depth--; depth--;
		if (check(Keywords.INT)) {
			return match(Keywords.INT);
		}
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("PrimitiveType");
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Bool");
			depth--;
			if (check(Keywords.BOOL)) {
				return match(Keywords.BOOL);
			}
			else {
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("IDENTIFIER");
				return match(Keywords.ID);
			}
		}
	}
	private Lexeme optParameterList()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Parameter");
		if (parameterListPending())
		{
			return parameterList();
		}
		else return null;
	}
	private boolean parameterListPending()
	{
		return isTypeName();
	}
	private boolean parametersPending()
	{
		return check(Punct.COMMA);
	}
	private Lexeme parameterList()
	{
		depth++;
		Lexeme param = parameter(), moreParams = null;
		param.type = "PARAM";
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Parameter List");
		if (parametersPending())
		{
			match(Punct.COMMA);
			moreParams = parameterList();
		}
		Lexeme result = Lexeme.node(param, moreParams);
		result.type = "PARAM_LIST";
		depth--;depth--;
		return result;
	}
	private Lexeme parameter()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Type Name");
		Lexeme type = matchTypeName();
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("IDENTIFIER");
		depth--;depth--;
		return Lexeme.node(type, match(Keywords.ID));
	}
	private boolean isBlock()
	{
		return check(Punct.OBRACE);
	}
	private Lexeme block()
	{
		match(Punct.OBRACE);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Declaration Or Statement List");
		Lexeme oDS = optDeclarationOrStatementList();
		oDS.type = "BLOCK";
		match(Punct.CBRACE);
		depth--;
		return oDS;
	}
	private Lexeme optDeclarationOrStatementList()
	{
		Lexeme decOrState = null, moreDecOrState = null;
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Declaration Or Statement");
		if (declarationOrStatementPending())
		{
			decOrState = declarationOrStatement();
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Declaration Or Statement List");
			moreDecOrState = optDeclarationOrStatementList();
			depth--;
		}
		Lexeme result = Lexeme.node(decOrState,moreDecOrState);
		result.type = "DECLARATION_OR_STATEMENT";
		depth--;
		return result;
	}
	private boolean declarationOrStatementPending()
	{
		return (isPrimitiveType() || isKeywordStatement() || isOtherDeclarationOrStatement());
	}
	private boolean isKeywordStatement()
	{
		return (isSelection() || isIteration() || isJump() || isInput() || isOutput() || isBlock());
	}
	private boolean isOtherDeclarationOrStatement()
	{
		return check(Keywords.ID);
	}
	private Lexeme declarationOrStatement()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Primitive Type");
		if (isPrimitiveType()) {
			Lexeme p = primitiveDeclaration();
			depth--;
			return p;
		}
		else {
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Keyword Statement");
			if (isKeywordStatement()) {
				Lexeme k = keywordStatement();
				depth--;
				return k;
			}
			else
			{
				depth++;
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("Other Declaration Or Statement");
				depth++;
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("IDENTIFIER");
				Lexeme name = match(Keywords.ID);
				depth--;
				Lexeme result = Lexeme.node(name, restOfDeclarationOrStatement());
				result.type = "DECLARATION_OR_STATEMENT";
				depth--;
				return result;
			}
		}
	}
	private Lexeme primitiveDeclaration()
	{
		Lexeme type = null;
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Int");
		if (check(Keywords.INT)) type = match(Keywords.INT);
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Bool");
			type = match(Keywords.BOOL);
		}
		Lexeme result = Lexeme.node(type, restOfVariableDeclaration());
		result.type = "PRIMITIVE_DECLARATION";
		depth--;
		return result;
	}
	private Lexeme restOfDeclarationOrStatement()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Rest Of Variable Declaration");
		if (variablePending()) {
			Lexeme re = restOfVariableDeclaration();
			depth--;
			return re;
		}
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Rest Of Expression Statement");
			Lexeme r = restOfExpressionStatement();
			depth--;
			return r;
		}
	}
	private boolean variablePending()
	{
		return check(Keywords.ID);
	}
	private Lexeme statement()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Keyword Statement");
		if (isKeywordStatement()) {
			Lexeme k = keywordStatement();
			depth--;
			return k;
		}
		else {
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Expression Statement");
			Lexeme e = expressionStatement();
			depth--;
			return e;
		}
	}
	
	// Keyword Statement Section
	private Lexeme keywordStatement()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Selection");
		if (isSelection()) {
			Lexeme is = ifStatement();
			depth--;
			return is;
		}
		else {
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Iteration");
			depth--;
			if (isIteration()) {
				Lexeme it = iterationStatement();
				depth--;
				return it;
			}
			else {
				depth++;
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("Jump");
				depth--;
				if (isJump()) {
					Lexeme j = jumpStatement();
					depth--;
					return j;
				}
				else {
					depth++;
					for (int i = 0; i < depth; i++) System.out.print("-");
					System.out.println("Input");
					if (isInput()) {
						Lexeme i = inputStatement();
						depth--;
						return i;
					}
					else {
						depth++;
						for (int i = 0; i < depth; i++) System.out.print("-");
						System.out.println("Output");
						if (isOutput()) {
							Lexeme o = outputStatement();
							depth--;
							return o;
						}
						else {

							depth++;
							for (int i = 0; i < depth; i++) System.out.print("-");
							System.out.println("Block");
							Lexeme b = block();
							depth--;
							return b;
						}
					}
				}
			}
		}
	}
	private boolean isSelection()
	{
		return check(Keywords.IF);
	}
	private boolean isIteration()
	{
		return (check(Keywords.WHILE) || check(Keywords.DO)); 
	}
	private boolean isJump()
	{
		return check(Keywords.RETURN);
	}
	private boolean isInput()
	{
		return check(Keywords.CIN);
	}
	private boolean isOutput()
	{
		return check(Keywords.COUT);
	}
	private Lexeme ifStatement()
	{
		match(Keywords.IF);
		match(Punct.OPAREN);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Expression");
		Lexeme expr = expression();
		match(Punct.CPAREN);
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Statement");
		Lexeme statement1 = statement(), statement2 = null;
		statement1.type = "IF_STATEMENT";
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Optional Else");
		if (check(Keywords.ELSE))
		{
			match(Keywords.ELSE);
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Statement");
			statement2 = statement();
			statement2.type = "ELSE_STATEMENT";
			depth--;depth--;
		}
		depth--;
		Lexeme result = Lexeme.node(expr, Lexeme.node(statement1, Lexeme.node(statement2, null)));
		result.type = "IF";
		return result;
	}
	private Lexeme iterationStatement()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("While Statement");
		depth--;
		if (check(Keywords.WHILE)) return whileStatement();
		else {
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Do While");
			depth--;
			return doWhileStatement();
		}
	}
	private Lexeme whileStatement()
	{
		match(Keywords.WHILE);
		match(Punct.OPAREN);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Expression");
		Lexeme expr = expression();
		match(Punct.CPAREN);
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Statement");
		Lexeme statement = statement();
		Lexeme result = Lexeme.node(expr, Lexeme.node(statement, null));
		result.type = "WHILE";
		depth--;
		return result;
	}
	private Lexeme doWhileStatement()
	{
		match(Keywords.DO);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Statement");
		Lexeme statement = statement();
		match(Keywords.WHILE);
		match(Punct.OPAREN);
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Expression");
		Lexeme expr = expression();
		match(Punct.CPAREN);
		match(Punct.SEMICOLON);
		Lexeme result = Lexeme.node(statement, Lexeme.node(expr, null));
		result.type = "DO_WHILE";
		depth--;
		return result;
	}
	private Lexeme jumpStatement()
	{
		match(Keywords.RETURN);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Optional Expression");
		Lexeme expr = optExpression();
		match(Punct.SEMICOLON);
		if (expr != null) {
			expr.type = "JUMP";
		} else {
			expr = new Lexeme();
			expr.type = "JUMP";
		}
		depth--;
		return expr;
	}
	private Lexeme inputStatement()
	{
		match(Keywords.CIN);
		match(Operator.SHIFTRIGHT);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("IDENTIFIER");
		Lexeme name = match(Keywords.ID);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Primary");
		Lexeme morePrimary = morePrimary();
		match(Punct.SEMICOLON);
		Lexeme result = Lexeme.node(name, morePrimary);
		result.type = "INPUT";
		depth--;depth--;
		return result;
	}
	private Lexeme outputStatement()
	{
		Lexeme result = null;
		match(Keywords.COUT);
		match(Operator.SHIFTLEFT);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Expression");
		if (expressionPending()) result = expression();
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("STRING");
			if (check(Type.STRING)) result = match(Type.STRING);
			else {
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("ENDL");
				result = match(Keywords.ENDL);
			}
		}
		match(Punct.SEMICOLON);
		Lexeme r = Lexeme.node(result, null);
		r.type = "OUTPUT";
		depth--;
		return r;
	}
	
	// Expression Statement Section
	private Lexeme expressionStatement()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("IDENTIFIER");
		Lexeme name = match(Keywords.ID);
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Rest Of Expression Statement");
		Lexeme result = Lexeme.node(name, restOfExpressionStatement());
		result.type = "EXPRESSION_STATEMENT";
		depth--;
		return result;
	}
	private Lexeme restOfExpressionStatement()
	{
		Lexeme primary = null, assignment = null;
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Primary");
		if (morePrimaryPending()) primary = morePrimary();
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Optional Assignment");
		if (check(Operator.ASSIGN)) 
		{
			assignment = assignment();
			assignment.type = "ASSIGNMENT";
		}
		match(Punct.SEMICOLON);
		Lexeme result = Lexeme.node(primary, assignment);
		result.type = "REST_OF_EXPRESSION_STATEMENT";
		depth--;
		return result;
	}
	private Lexeme assignment()
	{
		match(Operator.ASSIGN);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Expression");
		Lexeme e = expression();
		depth--;
		return e;
	}
	
	// Expression Section
	private Lexeme optExpression()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Expression");
		if (expressionPending()) {
			Lexeme e = expression();
			depth--;
			return e;
		}
		else {
			depth--;
			return null;
		}
	}
	private boolean expressionPending()
	{
		return isDisjunction();
	}
	private boolean isDisjunction()
	{
		return isConjunction();
	}
	private boolean isConjunction()
	{
		return isEquality();
	}
	private boolean isEquality()
	{
		return isInequality();
	}
	private boolean isInequality()
	{
		return isSum();
	}
	private boolean isSum()
	{
		return isProduct();
	}
	private boolean isProduct()
	{
		return isFactor();
	}
	private boolean isFactor()
	{
		return (isPrimary() || check(Operator.PLUS) || check(Operator.MINUS) || check(Operator.NOT));
	}
	private boolean isPrimary()
	{
		return (check(Keywords.ID) || check(Type.INTEGER) || check(Keywords.TRUE) || check(Keywords.FALSE) || check(Punct.OPAREN));
	}
	private Lexeme expression()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Disjunction");
		Lexeme dis = disjunction(), cond = null;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Optional Conditional Part");
		if (conditionalPartPending()) cond = conditionalPart();
		Lexeme result = Lexeme.node(dis, cond);
		result.type = "EXPRESSION";
		depth--;
		return result;
	}
	private boolean conditionalPartPending()
	{
		return check(Operator.CONDITIONAL);
	}
	private Lexeme conditionalPart()
	{
		match(Operator.CONDITIONAL);
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Expression");
		Lexeme expr1 = expression();
		match(Punct.COLON);
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Expression");
		Lexeme expr2 = expression();
		Lexeme result = Lexeme.node(expr1, Lexeme.node(expr2, null));
		result.type = "CONDITIONAL";
		depth--;
		return result;
	}
	private Lexeme disjunction()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Conjunction");
		Lexeme con = conjunction(), moreCons = null;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Conjunctions");
		if (moreConjunctionsPending()) moreCons = moreConjunctions();
		Lexeme result = Lexeme.node(con, moreCons);
		result.type = "DISJUNCTION";
		depth--;
		return result;
	}
	private boolean moreConjunctionsPending()
	{
		return check(Operator.OR);
	}
	private Lexeme moreConjunctions()
	{
		Lexeme result = Lexeme.node(match(Operator.OR), disjunction());
		result.type = "OR_STATEMENT";
		return result;
	}
	private Lexeme conjunction()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Equality");
		Lexeme eq = equality(), moreEq = null;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Equalities");
		if (moreEqualitiesPending()) moreEq = moreEqualities();
		Lexeme result = Lexeme.node(eq, moreEq);
		result.type = "CONJUNCTION";
		depth--;
		return result;
	}
	private boolean moreEqualitiesPending()
	{
		return check(Operator.AND);
	}
	private Lexeme moreEqualities()
	{
		Lexeme result = Lexeme.node(match(Operator.AND), conjunction());
		result.type = "AND_STATEMENT";
		return result;
	}
	private Lexeme equality()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Inequality");
		Lexeme inEq = inequality(), moreInEq = null;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Rest Of Equality");
		if (equalityPending()) moreInEq = restOfEquality();
		Lexeme result = Lexeme.node(inEq, moreInEq);
		result.type = "EQUALITY";
		depth--;
		return result;
	}
	private boolean equalityPending()
	{
		return (check(Operator.EQUALS) || check(Operator.NOTEQUAL));
	}
	private Lexeme restOfEquality()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Equals");
		if (check(Operator.EQUALS))
		{
			Lexeme result = Lexeme.node(match(Operator.EQUALS), inequality());
			result.type = "EQUALS";
			depth--;
			return result;
		}
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Not Equal");
			if (check(Operator.NOTEQUAL))
			{
				Lexeme result = Lexeme.node(match(Operator.NOTEQUAL), inequality());
				result.type = "NOTEQUAL";
				depth--;
				return result;
			}
			else {
				depth--;
				return null;
			}
		}
	}
	private Lexeme inequality()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Sum");
		Lexeme sum = sum(), moreSum = null;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Rest Of Inequality");
		if (inequalityPending()) moreSum = restOfInequality();
		Lexeme result = Lexeme.node(sum, moreSum);
		result.type = "INEQUALITY";
		depth--;
		return result;
	}
	private boolean inequalityPending()
	{
		return (check(Operator.GTHAN) || check(Operator.LTHAN) || check(Operator.GTHANEQUAL) || check(Operator.LTHANEQUAL));
				
	}
	private Lexeme restOfInequality()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Greater Than");
		if (check(Operator.GTHAN))
		{
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Sum");
			Lexeme result = Lexeme.node(match(Operator.GTHAN), sum());
			depth--;
			result.type = "GTHAN";
			depth--;
			return result;
		}
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Less Than");
			if (check(Operator.LTHAN))
			{
				depth++;
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("Sum");
				Lexeme result = Lexeme.node(match(Operator.LTHAN), sum());
				depth--;
				result.type = "LTHAN";
				depth--;
				return result;
			}
			else {
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("Greater Than or Equal");
				if (check(Operator.GTHANEQUAL))
				{
					depth++;
					for (int i = 0; i < depth; i++) System.out.print("-");
					System.out.println("Sum");
					Lexeme result = Lexeme.node(match(Operator.GTHANEQUAL), sum());
					depth--;
					result.type = "GTHANEQUAL";
					depth--;
					return result;
				}
				else {
					for (int i = 0; i < depth; i++) System.out.print("-");
					System.out.println("Less Than or Equal");
					if (check(Operator.LTHANEQUAL))
					{
						depth++;
						for (int i = 0; i < depth; i++) System.out.print("-");
						System.out.println("Sum");
						Lexeme result = Lexeme.node(match(Operator.LTHANEQUAL), sum());
						depth--;
						result.type = "LTHANEQUAL";
						depth--;
						return result;
					}
					else return null;
				}
			}
		}
	}
	private Lexeme sum()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Product");
		Lexeme prod = product(), moreProds = null;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Products");
		if (moreProductsPending()) moreProds = moreProducts();
		Lexeme result = Lexeme.node(prod, moreProds);
		result.type = "SUM";
		depth--;
		return result;
	}
	private Lexeme product()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Factor");
		Lexeme f = factor(), moreF = null;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Factors");
		if (moreFactorsPending()) moreF = moreFactors();
		Lexeme result = Lexeme.node(f, moreF);
		result.type ="PRODUCT";
		depth--;
		return result;
	}
	private boolean moreProductsPending()
	{
		return (check(Operator.PLUS) || check(Operator.MINUS));
	}
	private Lexeme moreProducts()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Plus");
		if (check(Operator.PLUS)) 
		{
			Lexeme result = Lexeme.node(match(Operator.PLUS), sum());
			result.type = "PLUS";
			depth--;
			return result;
		}
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Minus");
			if (check(Operator.MINUS))
			{
				Lexeme result = Lexeme.node(match(Operator.MINUS), sum());
				result.type = "MINUS";
				depth--;
				return result;
			}
			else {
				depth--;
				return null;
			}
		}
	}
	private Lexeme factor()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Primary");
		if (isPrimary()) {
			Lexeme p = primary();
			depth--;
			return p;
		}
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Plus Primary");
			if (check(Operator.PLUS))
			{
				Lexeme result = Lexeme.node(match(Operator.PLUS), primary());
				result.type = "PLUS";
				depth--;
				return result;
			}
			else {
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("Minus Primary");
				if (check(Operator.MINUS))
				{
					Lexeme result = Lexeme.node(match(Operator.MINUS), primary());
					result.type = "MINUS";
					depth--;
					return result;
				}
				else
				{
					for (int i = 0; i < depth; i++) System.out.print("-");
					System.out.println("Not Primary");
					Lexeme result = Lexeme.node(match(Operator.NOT), primary());
					result.type = "NOT";
					depth--;
					return result;
				}
			}
		}
	}
	private boolean moreFactorsPending()
	{
		return (check(Operator.STAR) || check(Operator.SLASH) || check(Operator.MODULO));
	}
	private Lexeme moreFactors()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Star");
		if (check(Operator.STAR))
		{
			Lexeme result = Lexeme.node(match(Operator.STAR), product());
			result.type = "STAR";
			depth--;
			return result;
		}
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Slash");
			if (check(Operator.SLASH))
			{
				Lexeme result = Lexeme.node(match(Operator.SLASH), product());
				result.type = "SLASH";
				depth--;
				return result;
			}
			else {
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("Modulo");
				if (check(Operator.MODULO))
				{
					Lexeme result = Lexeme.node(match(Operator.MODULO), product());
					result.type = "MODULO";
					depth--;
					return result;
				}
				else {
					depth--;
					return null;
				}
			}
		}
	}
	private Lexeme primary()
	{
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("IDENTIFIER");
		if (check(Keywords.ID))
		{
			Lexeme name = match(Keywords.ID), morePrimary = null;
			if (morePrimaryPending()) morePrimary = morePrimary();
			Lexeme result = Lexeme.node(name, morePrimary);
			result.type = "PRIMARY";
			depth--;
			return result;
		}
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Int");
			if (check(Type.INTEGER)) {
				depth--;
				return match(Type.INTEGER);
			}
			else {
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("True");
				if (check(Keywords.TRUE)) {
					depth--;
					return match(Keywords.TRUE);
				}
				else {
					for (int i = 0; i < depth; i++) System.out.print("-");
					System.out.println("False");
					if (check(Keywords.FALSE)) {
						depth--;
						return match(Keywords.FALSE);
					}
					else
					{
						match(Punct.OPAREN);
						for (int i = 0; i < depth; i++) System.out.print("-");
						System.out.println("Expression");
						Lexeme expr = expression();
						match(Punct.CPAREN);
						depth--;
						return expr;
					}
				}
			}
		}
	}
	private boolean morePrimaryPending()
	{
		return (check(Punct.OBRACKET) || check(Operator.DOT) || check(Punct.OPAREN));
	}
	private Lexeme morePrimary()
	{
		Lexeme result = null, morePrimary = null;
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Array Access");
		if (check(Punct.OBRACKET))
		{
			match(Punct.OBRACKET);
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Expression");
			result = expression();
			result.type = "ARRAY_DIMENSIONS";
			match(Punct.CBRACKET);
			depth--;
		}
		else {
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("Field Access");
			if (check(Operator.DOT))
			{
				depth++;
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("IDENTIFIER");
				result = Lexeme.node(match(Operator.DOT), match(Keywords.ID));
				result.type = "DOT_CALL";
				depth--;
			}
			else {
				depth++;
				for (int i = 0; i < depth; i++) System.out.print("-");
				System.out.println("Function Call");
				if (check(Punct.OPAREN))
				{
					match(Punct.OPAREN);
					depth++;
					for (int i = 0; i < depth; i++) System.out.print("-");
					System.out.println("Optional Argument List");
					result = optArgList();
					match(Punct.CPAREN);
					result.type = "ARG_LIST";
					depth--;
				}
			}
		}
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Primary");
		if (morePrimaryPending()) morePrimary = morePrimary();
		Lexeme r = Lexeme.node(result, morePrimary);
		r.type = "PRIMARY";
		depth--;depth--;
		return r;
	}
	private Lexeme optArgList()
	{
		Lexeme expr = null, moreExpr = null;
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("Expression");
		if (expressionPending())
		{
			expr = expression();
			expr.type = "ARG";
			depth++;
			for (int i = 0; i < depth; i++) System.out.print("-");
			System.out.println("More Expressions");
			if (moreExpressionsPending())
			{
				moreExpr = moreExpressions();
				moreExpr.type = "ARG_LIST";
			}
			depth--;
		}
		Lexeme result = Lexeme.node(expr, moreExpr);
		depth--;
		return result;
	}
	private boolean moreExpressionsPending()
	{
		return check(Punct.COMMA);
	}
	private Lexeme moreExpressions()
	{
		match(Punct.COMMA);
		System.out.println("Expression");
		Lexeme expr = expression(), moreExpr = null;
		expr.type = "ARG";
		depth++;
		for (int i = 0; i < depth; i++) System.out.print("-");
		System.out.println("More Expressions");
		if (moreExpressionsPending())
		{
			moreExpr = moreExpressions();
			moreExpr.type = "MORE_ARGS";
		}
		Lexeme result = Lexeme.node(expr, moreExpr);
		result.type = "EXPRESSION";
		return result;
	}

	// Checks for matches and advances if match found
	// exits if no match
	private void advance() throws IOException
	{
		currentLexeme = lex.Lex();
	}
	private boolean check(Keywords word) { return currentLexeme.k == word; }
	private boolean check(Punct p) { return currentLexeme.punct == p; }
	private boolean check(Operator op) { return currentLexeme.op == op; }
	private boolean check(Type t) { return currentLexeme.T == t; }
	private Lexeme match(Keywords word) 
	{
		Lexeme prevLex = currentLexeme;
		matchNoAdvance(word);
		try
		{
			advance();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return prevLex;
	}
	private Lexeme match(Punct p) 
	{
		Lexeme prevLex = currentLexeme;
		matchNoAdvance(p);
		try
		{
			advance();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return prevLex;
	}
	private Lexeme match(Operator op) 
	{
		Lexeme prevLex = currentLexeme;
		matchNoAdvance(op);
		try
		{
			advance();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return prevLex;
	}
	private Lexeme match(Type t) 
	{
		Lexeme prevLex = currentLexeme;
		matchNoAdvance(t);
		try
		{
			advance();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return prevLex;
	}
	private void matchNoAdvance(Keywords word) 
	{ 
		if (!check(word))
		{
			errors.add("String ILLEGAL\nA keyword is invalid. Current: " + currentLexeme.type + " Expected: " + word);
			System.out.println("String ILLEGAL");
			System.out.println("A keyword is invalid. Current: " + currentLexeme.type + " Expected: " + word);
			//System.exit(-1);
		}
	}
	private void matchNoAdvance(Punct p) 
	{
		if (!check(p))
		{
			errors.add("String ILLEGAL\nA keyword is invalid. Current: " + currentLexeme.type + " Expected: " + p);
			System.out.println("ILLEGAL");
			System.out.println("The punctuation is invalid. Current: " + currentLexeme.type + " Expected: " + p + " " + currentLexeme.i);
			//System.exit(-1);
		}
	}
	private void matchNoAdvance(Operator op) 
	{
		if (!check(op))
		{
			errors.add("String ILLEGAL\nA keyword is invalid. Current: " + currentLexeme.type + " Expected: " + op);
			System.out.println("ILLEGAL");
			System.out.println("The operator is invalid. Current: " + currentLexeme.type + " Expected: " + op);
			//System.exit(-1);
		}
	}
	private void matchNoAdvance(Type t) 
	{
		if (!check(t))
		{
			errors.add("String ILLEGAL\nA keyword is invalid. Current: " + currentLexeme.type + " Expected: " + t);
			System.out.println("ILLEGAL");
			System.out.println("The type is invalid. Current: " + currentLexeme.type + " Expected: " + t);
			//System.exit(-1);
		}
	}
}
