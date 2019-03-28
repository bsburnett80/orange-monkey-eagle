import java.lang.Math;

public class Evaluator
{
	private Environment e = new Environment();
	public Lexeme lex = new Lexeme();
	
	public Evaluator(Lexeme pt, Lexeme env)
	{
		lex = runEval(pt, env);
	}
	
	public Lexeme runEval(Lexeme pt, Lexeme env)
	{
		return eval(pt, env);
	}
	
	public Lexeme eval(Lexeme pt, Lexeme env)
	{
		if (pt.type == "INTEGER") return pt;
		else if (pt.type == "STRING") return pt;
		else if (pt.type == "REAL") return pt;
		else if (pt.type == "ID") return e.lookUp(pt, env);
		else if (pt.type == "CLASS") return evalClass(pt, env);
		else if (pt.type == "FUNCTIONDEF") return evalFuncDef(pt, env);
		else if (pt.type == "FUNCTIONCALL") return evalFuncCall(pt, env);
		else if (pt.type == "IF") return evalIf(pt, env);
		else if (pt.type == "IF-ELSE") return evalIfElse(pt, env);
		else if (pt.type == "WHILESTATEMENT") return evalWhile(pt, env);
		else if (pt.type == "PRINT") return evalPrint(pt, env);
		else if (pt.type == "PRINTLN") return evalPrintLn(pt, env);
		else if (pt.type == "PARAMS") return evalParams(pt, env);
		else if (pt.type == "ARGS") return evalArgs(pt, env);
		else if (pt.type == "BODY") return evalBody(pt, env);
		else if (pt.type == "ASSIGN") return evalAssign(pt, env);
		else if (pt.type == "PLUS") return evalPlus(pt, env);
		else if (pt.type == "MINUS") return evalMinus(pt, env);
		else if (pt.type == "STAR") return evalStar(pt, env);
		else if (pt.type == "MODULO") return evalMod(pt, env);
		else if (pt.type == "SLASH") return evalSlash(pt, env);
		else if (pt.type == "GTHAN") return evalGthan(pt, env);
		else if (pt.type == "LTHAN") return evalLthan(pt, env);
		else if (pt.type == "NOT") return evalNot(pt, env);
		else if (pt.type == "AND") return evalAnd(pt, env);
		else if (pt.type == "OR") return evalOr(pt, env);
		else if (pt.type == "EQUAL") return evalEqual(pt, env);
		else if (pt.type == "EXP") return evalExp(pt, env);
		else if (pt.type == "AT") return evalAt(pt, env);
		else if (pt.type == "DOT") return evalDot(pt, env);
		else if (pt.type == "SET") return evalSet(pt, env);
		else if (pt.type == "ARRAY") return evalArray(pt, env);
		else if (pt.type == "SETARRAY") return evalSetArray(pt, env);
		else if (pt.type == "RETURN") return evalReturn(pt, env);
		else if (pt.type == "NULL") return null;
		else return null;
	}
	
	private Lexeme evalClass(Lexeme pt, Lexeme env)
	{		
		return e.populate(getClassName(pt), eval(getClassBody(pt), env), env);
	}
	
	private Lexeme getClassName(Lexeme pt)
	{
		return pt.left;
	}
	
	private Lexeme getClassBody(Lexeme pt)
	{
		return pt.right.left;
	}
	
	private Lexeme evalAssign(Lexeme pt, Lexeme env)
	{	
		if (e.varExists(pt.left, env))
		{
			return e.update(pt.left, eval(pt.right, env), env);
		}
		else 
		{
			return e.populate(pt.left, eval(pt.right, env), env);
		}
	}
	
	private Lexeme evalPlus(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER") c = plusInts(a, b);
			else if (b.type == "REAL")
			{
				b.ival = (int) b.rval;
				c = plusInts(a, b);
			}
			else if (b.type == "STRING")
			{
				b.ival = Integer.parseInt(b.sval);
				c = plusInts(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = plusReals(a, b);
			}
			else if (b.type == "REAL") c = plusReals(a, b);
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = plusReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				b.sval = Integer.toString(b.ival);
				c = plusStrings(a, b);
			}
			else if (b.type == "REAL")
			{
				b.sval = Double.toString(b.rval);
				c = plusStrings(a, b);
			}
			else if (b.type == "STRING") c = plusStrings(a, b);
		}
		
		return c;
	}
	
	private Lexeme plusInts(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("INTEGER");
		c.ival = a.ival + b.ival;
		return c;
	}
	
	private Lexeme plusReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("REAL");
		c.rval = a.rval + b.rval;
		return c;
	}
	
	private Lexeme plusStrings(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("STRING");
		c.sval = a.sval + b.sval;
		return c;
	}
	
	private Lexeme evalStar(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER") c = multInts(a, b);
			else if (b.type == "REAL")
			{
				b.ival = (int) b.rval;
				c = multInts(a, b);
			}
			else if (b.type == "STRING")
			{
				b.ival = Integer.parseInt(b.sval);
				c = multInts(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = multReals(a, b);
			}
			else if (b.type == "REAL") c = multReals(a, b);
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = multReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				a.ival = Integer.parseInt(a.sval);
				c = multInts(a, b);
			}
			else if (b.type == "REAL")
			{
				a.rval = Double.parseDouble(a.sval);
				c = multReals(a, b);
			}
			else if (b.type == "STRING")
			{
				a.rval = Double.parseDouble(a.sval);
				b.rval = Double.parseDouble(b.sval);
				c = multReals(a, b);
			}
		}
		
		return c;
	}
	
	private Lexeme multInts(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("INTEGER");
		c.ival = a.ival * b.ival;
		return c;
	}
	
	private Lexeme multReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("REAL");
		c.rval = a.rval * b.rval;
		return c;
	}
	
	private Lexeme evalMod(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER") c = modInts(a, b);
			else if (b.type == "REAL")
			{
				b.ival = (int) b.rval;
				c = modInts(a, b);
			}
			else if (b.type == "STRING")
			{
				b.ival = Integer.parseInt(b.sval);
				c = modInts(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = modReals(a, b);
			}
			else if (b.type == "REAL") c = modReals(a, b);
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = modReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				a.ival = Integer.parseInt(a.sval);
				c = modInts(a, b);
			}
			else if (b.type == "REAL")
			{
				a.rval = Double.parseDouble(a.sval);
				c = modReals(a, b);
			}
			else if (b.type == "STRING")
			{
				a.rval = Double.parseDouble(a.sval);
				b.rval = Double.parseDouble(b.sval);
				c = modReals(a, b);
			}
		}
		
		return c;
	}
	
	private Lexeme modInts(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("INTEGER");
		c.ival = a.ival % b.ival;
		return c;
	}
	
	private Lexeme modReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("REAL");
		c.rval = a.rval % b.rval;
		return c;
	}
	
	private Lexeme evalSlash(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER") c = divInts(a, b);
			else if (b.type == "REAL")
			{
				b.ival = (int) b.rval;
				c = divInts(a, b);
			}
			else if (b.type == "STRING")
			{
				b.ival = Integer.parseInt(b.sval);
				c = divInts(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = divReals(a, b);
			}
			else if (b.type == "REAL") c = divReals(a, b);
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = divReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				a.ival = Integer.parseInt(a.sval);
				c = divInts(a, b);
			}
			else if (b.type == "REAL")
			{
				a.rval = Double.parseDouble(a.sval);
				c = divReals(a, b);
			}
			else if (b.type == "STRING")
			{
				a.rval = Double.parseDouble(a.sval);
				b.rval = Double.parseDouble(b.sval);
				c = divReals(a, b);
			}
		}
		
		return c;
	}
	
	private Lexeme divInts(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("REAL");
		c.rval = (double) a.ival / (double) b.ival;
		return c;
	}
	
	private Lexeme divReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("REAL");
		c.rval = a.rval / b.rval;
		return c;
	}
	
	private Lexeme evalMinus(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER") c = subInts(a, b);
			else if (b.type == "REAL")
			{
				b.ival = (int) b.rval;
				c = subInts(a, b);
			}
			else if (b.type == "STRING")
			{
				b.ival = Integer.parseInt(b.sval);
				c = subInts(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = subReals(a, b);
			}
			else if (b.type == "REAL") c = subReals(a, b);
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = subReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				a.ival = Integer.parseInt(a.sval);
				c = subInts(a, b);
			}
			else if (b.type == "REAL")
			{
				a.rval = Double.parseDouble(a.sval);
				c = subReals(a, b);
			}
			else if (b.type == "STRING")
			{
				a.rval = Double.parseDouble(a.sval);
				b.rval = Double.parseDouble(b.sval);
				c = subReals(a, b);
			}
		}
		
		return c;
	}
	
	private Lexeme subInts(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("INTEGER");
		c.ival = a.ival - b.ival;
		return c;
	}
	
	private Lexeme subReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("REAL");
		c.rval = a.rval - b.rval;
		return c;
	}
	
	private Lexeme evalLthan(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER") c = lthanInts(a, b);
			else if (b.type == "REAL")
			{
				b.ival = (int) b.rval;
				c = lthanInts(a, b);
			}
			else if (b.type == "STRING")
			{
				b.ival = Integer.parseInt(b.sval);
				c = lthanInts(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = lthanReals(a, b);
			}
			else if (b.type == "REAL") c = lthanReals(a, b);
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = lthanReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				b.sval = Integer.toString(b.ival);
				c = lthanStrings(a, b);
			}
			else if (b.type == "REAL")
			{
				b.sval = Double.toString(b.rval);
				c = lthanStrings(a, b);
			}
			else if (b.type == "STRING") c = lthanStrings(a, b);
		}
		
		return c;
	}
	
	private Lexeme lthanInts(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a.ival < b.ival) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme lthanReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a.rval < b.rval) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme lthanStrings(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a.sval.length() < b.sval.length()) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme evalGthan(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER") c = gthanInts(a, b);
			else if (b.type == "REAL")
			{
				b.ival = (int) b.rval;
				c = gthanInts(a, b);
			}
			else if (b.type == "STRING")
			{
				b.ival = Integer.parseInt(b.sval);
				c = gthanInts(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = gthanReals(a, b);
			}
			else if (b.type == "REAL") c = gthanReals(a, b);
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = gthanReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				b.sval = Integer.toString(b.ival);
				c = gthanStrings(a, b);
			}
			else if (b.type == "REAL")
			{
				b.sval = Double.toString(b.rval);
				c = gthanStrings(a, b);
			}
			else if (b.type == "STRING") c = gthanStrings(a, b);
		}
		
		return c;
	}
	
	private Lexeme gthanInts(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a.ival > b.ival) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme gthanReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a.rval > b.rval) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme gthanStrings(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a.sval.length() > b.sval.length()) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme evalNot(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (b == null || a == null) c = notNulls(a, b);
		else if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER") c = notInts(a, b);
			else if (b.type == "REAL")
			{
				b.ival = (int) b.rval;
				c = notInts(a, b);
			}
			else if (b.type == "STRING")
			{
				b.ival = Integer.parseInt(b.sval);
				c = notInts(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = notReals(a, b);
			}
			else if (b.type == "REAL") c = notReals(a, b);
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = notReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				b.sval = Integer.toString(b.ival);
				c = notStrings(a, b);
			}
			else if (b.type == "REAL")
			{
				b.sval = Double.toString(b.rval);
				c = notStrings(a, b);
			}
			else if (b.type == "STRING") c = notStrings(a, b);
		}
		
		return c;
	}
	
	private Lexeme notNulls(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a != null && b != null) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme notInts(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a.ival != b.ival) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme notReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a.rval != b.rval) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme notStrings(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (!a.sval.equalsIgnoreCase(b.sval)) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme evalAnd(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "TRUE" && b.type == "TRUE")
		{
			c.type = "TRUE";
			return c;
		}
		else
		{
			c.type = "FALSE";
			return c;
		}
	}
	
	private Lexeme evalOr(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "TRUE" || b.type == "TRUE")
		{
			c.type = "TRUE";
			return c;
		}
		else
		{
			c.type = "FALSE";
			return c;
		}
	}
	
	private Lexeme evalEqual(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (b == null || a == null) c = equalNulls(a, b);
		else if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER") c = equalInts(a, b);
			else if (b.type == "REAL")
			{
				b.ival = (int) b.rval;
				c = equalInts(a, b);
			}
			else if (b.type == "STRING")
			{
				b.ival = Integer.parseInt(b.sval);
				c = equalInts(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = equalReals(a, b);
			}
			else if (b.type == "REAL") c = equalReals(a, b);
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = equalReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				b.sval = Integer.toString(b.ival);
				c = equalStrings(a, b);
			}
			else if (b.type == "REAL")
			{
				b.sval = Double.toString(b.rval);
				c = equalStrings(a, b);
			}
			else if (b.type == "STRING") c = equalStrings(a, b);
		}
		
		return c;
	}
	
	private Lexeme equalNulls(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		if (a == null && b == null) c.type = "TRUE";
		else c.type = "FALSE";
		
		return c;
	}
	
	private Lexeme equalInts(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		
		if (a.ival == b.ival)
		{
			c.type = "TRUE";
		}
		else
		{
			c.type = "FALSE";
		}
		
		return c;
	}
	
	private Lexeme equalReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		
		if (a.rval == b.rval)
		{
			c.type = "TRUE";
		}
		else
		{
			c.type = "FALSE";
		}
		
		return c;
	}
	
	private Lexeme equalStrings(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme();
		
		if (a.sval.equalsIgnoreCase(b.sval))
		{
			c.type = "TRUE";
		}
		else
		{
			c.type = "FALSE";
		}
		
		return c;
	}
	
	private Lexeme evalExp(Lexeme pt, Lexeme env)
	{
		Lexeme a = eval(pt.left, env);
		Lexeme b = eval(pt.right, env);
		Lexeme c = new Lexeme();
		
		if (a.type == "INTEGER")
		{
			if (b.type == "INTEGER")
			{
				a.rval = (double) a.ival;
				b.rval = (double) b.ival;
				c = expReals(a, b);
			}
			else if (b.type == "REAL")
			{
				a.rval = (double) a.ival;
				c = expReals(a, b);
			}
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = expReals(a, b);
			}
		}
		else if (a.type == "REAL")
		{
			if (b.type == "INTEGER")
			{
				b.rval = (double) b.ival;
				c = expReals(a, b);
			}
			else if (b.type == "REAL")
			{
				c = expReals(a, b);
			}
			else if (b.type == "STRING")
			{
				b.rval = Double.parseDouble(b.sval);
				c = expReals(a, b);
			}
		}
		else if (a.type == "STRING")
		{
			if (b.type == "INTEGER")
			{
				a.rval = Double.parseDouble(a.sval);
				b.rval = (double) b.ival;
				c = expReals(a, b);
			}
			else if (b.type == "REAL")
			{
				a.rval = Double.parseDouble(a.sval);
				c = expReals(a, b);
			}
			else if (b.type == "STRING")
			{
				a.rval = Double.parseDouble(a.sval);
				b.rval = Double.parseDouble(b.sval);
				c = expReals(a, b);
			}
		}
		
		return c;
	}
	
	private Lexeme expReals(Lexeme a, Lexeme b)
	{
		Lexeme c = new Lexeme("REAL");
		c.rval = Math.pow(a.rval, b.rval);
		
		return c;
	}
	
	private Lexeme evalAt(Lexeme pt, Lexeme env)
	{
		Lexeme id = eval(pt.left, env);
		
		return getArrayValue(pt.right, id, env);
	}
	
	private Lexeme evalDot(Lexeme pt, Lexeme env)
	{
		Lexeme object = e.cons(eval(getLeftHandSide(pt), env), null);
		
		if (pt.right.type == "DOTFUNCCALL")
		{
			 return evalDotFuncCall(getRightHandSide(pt), object, env);
		}
		else
		{
			
			return evalDotVarCall(getRightHandSide(pt), object);
		}
	}
	
	private Lexeme evalSet(Lexeme pt, Lexeme env)
	{
		Lexeme location = new Lexeme();
		location.left = eval(pt.left, env);
		location.right = eval(pt.right, env);
		
		return location;
	}
	
	private Lexeme getLeftHandSide(Lexeme pt)
	{
		return pt.left;
	}
	
	private Lexeme getRightHandSide(Lexeme pt)
	{
		return pt.right;
	}
	
	private Lexeme evalDotFuncCall(Lexeme pt, Lexeme cenv, Lexeme env)
	{
		Lexeme closure = eval(getFuncCallName(pt), cenv);
		Lexeme args = getFuncCallArgs(pt);
		Lexeme params = getClosureParams(closure);
		Lexeme body = getClosureBody(closure);
		Lexeme senv = getClosureEnvironment(closure);
		Lexeme eargs = evalDotArgs(args, env);
		Lexeme xenv = e.extend(params, eargs, senv);
		
		e.populate(xenv, new Lexeme(Types.Keywords.ID, "this"), xenv);
		
		return eval(body, xenv);
	}
	
	private Lexeme evalDotArgs(Lexeme pt, Lexeme env)
	{
		Lexeme args = null;
		if (pt != null)
		{
			args = e.cons(eval(pt.left, env), evalDotArgs(pt.right, env));
		}
		return args;
	}
	
	private Lexeme evalDotVarCall(Lexeme pt, Lexeme env)
	{
		return eval(pt.left, env);
	}
	
	private Lexeme evalParams(Lexeme pt, Lexeme env)
	{
		Lexeme result = null;
		if (pt != null)
		{
			result = e.cons(pt.left, evalParams(pt.right, env));
		}
		return result;
	}

	private Lexeme evalWhile(Lexeme pt, Lexeme env)
	{
		Lexeme last = new Lexeme();
		while (isTrue(eval(pt.left, env)))
		{
			last = eval(pt.right.left, env);
		}
		return last;
	}

	private boolean isTrue(Lexeme lex)
	{
		if (lex.type == "TRUE") return true;
		else return false;
	}

	private Lexeme evalIfElse(Lexeme pt, Lexeme env)
	{
		Lexeme last = new Lexeme();
		if (isTrue(eval(pt.left, env)))
		{
			last = eval(pt.right.left, env);
		}
		else last = eval(pt.right.right.left, env);
		return last;
	}

	private Lexeme evalIf(Lexeme pt, Lexeme env)
	{
		Lexeme last = new Lexeme();
		if (isTrue(eval(pt.left, env)))
		{
			last = eval(pt.right.left, env);
		}
		
		return last;
	}

	private Lexeme evalFuncDef(Lexeme pt, Lexeme env)
	{
		Lexeme closure = e.cons(getFuncDefParams(pt), e.cons(getFuncDefBody(pt), e.cons(env, null)));

		return e.populate(getFuncDefName(pt), closure, env);
	}
	
	private Lexeme getFuncDefName(Lexeme pt)
	{
		return pt.left;
	}

	private Lexeme getFuncDefBody(Lexeme pt)
	{
		return pt.right.right.left;
	}

	private Lexeme getFuncDefParams(Lexeme pt)
	{
		return pt.right.left;
	}

	private Lexeme evalFuncCall(Lexeme pt, Lexeme env)
	{
		Lexeme closure = eval(getFuncCallName(pt), env);
		Lexeme args = getFuncCallArgs(pt);
		Lexeme params = getClosureParams(closure);
		Lexeme body = getClosureBody(closure);
		Lexeme senv = getClosureEnvironment(closure);
		Lexeme eargs = evalArgs(args, env);
		Lexeme xenv = e.extend(params, eargs, senv);
		
		e.populate(xenv, new Lexeme(Types.Keywords.ID, "this"), xenv);
		
		return eval(body, xenv);
	}
	
	private Lexeme evalArgs(Lexeme pt, Lexeme env)
	{
		Lexeme args = null;
		if (pt != null)
		{
			args = e.cons(eval(pt.left, env), evalArgs(pt.right, env));
		}
		return args;
	}

	private Lexeme getClosureEnvironment(Lexeme pt)
	{
		return pt.right.right.left;
	}

	private Lexeme getClosureBody(Lexeme pt)
	{
		return pt.right.left;
	}

	private Lexeme getFuncCallArgs(Lexeme pt)
	{
		return pt.right.left;
	}

	private Lexeme getClosureParams(Lexeme pt)
	{
		return pt.left;
	}

	private Lexeme getFuncCallName(Lexeme pt)
	{
		return pt.left;
	}
	
	private Lexeme evalArray(Lexeme pt, Lexeme env)
	{
		Lexeme id = getArrayName(pt);
		Lexeme args = getArrayArgs(pt);
		
		return e.populate(id, args, env);
	}
	
	private Lexeme getArrayName(Lexeme pt)
	{
		return pt.left;
	}
	
	private Lexeme getArrayArgs(Lexeme pt)
	{
		return pt.right;
	}
	
	private Lexeme getArrayValue(Lexeme pt, Lexeme id, Lexeme env)
	{
		if (pt.type == "INTEGER") return id.aval.get(pt.ival);
		else 
		{
			Lexeme index = eval(pt, env);
			return id.aval.get(index.ival);
		}
	}
	
	private Lexeme evalSetArray(Lexeme pt, Lexeme env)
	{
		Lexeme value = eval(pt.right, env);
		Lexeme name = pt.left.left;
		Lexeme location = eval(pt.left, env);
		Lexeme arr = setArrayValue(location.left, location.right, value, env);
		
		return e.update(name, arr, env);
	}
	
	private Lexeme setArrayValue(Lexeme id, Lexeme loc, Lexeme val, Lexeme env)
	{
		id.aval.set(loc.ival, val);
		
		return id;
	}
	
	private Lexeme evalReturn(Lexeme pt, Lexeme env)
	{
		return eval(pt.left, env);
	}
	
	private Lexeme evalPrint(Lexeme pt, Lexeme env)
	{
		Lexeme val = eval(pt.left, env);
		if (val.type == "INTEGER") printInts(val);
		else if (val.type == "REAL") printReals(val);
		else if (val.type == "STRING") printStrings(val);
		
		return pt.left;
	}
	
	private void printInts(Lexeme pt)
	{
		System.out.print(pt.ival + " ");
	}
	
	private void printReals(Lexeme pt)
	{
		System.out.print(pt.rval + " ");
	}
	
	private void printStrings(Lexeme pt)
	{
		System.out.print(pt.sval + " ");
	}
	
	private Lexeme evalPrintLn(Lexeme pt, Lexeme env)
	{
		Lexeme val = eval(pt.left, env);
		if (val.type == "INTEGER") printLnInts(val);
		else if (val.type == "REAL") printLnReals(val);
		else if (val.type == "STRING") printLnStrings(val);
		
		return pt.left;
	}
	
	private void printLnInts(Lexeme pt)
	{
		System.out.println(pt.ival);
	}
	
	private void printLnReals(Lexeme pt)
	{
		System.out.println(pt.rval);
	}
	
	private void printLnStrings(Lexeme pt)
	{
		System.out.println(pt.sval);
	}

	private Lexeme evalBody(Lexeme pt, Lexeme env)
	{
		Lexeme result = null;
		while (pt != null)
		{
			result = eval(pt.left, env);
			pt = pt.right;
		}
		return result;
	}
}
