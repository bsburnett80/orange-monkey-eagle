
public class Environment extends Types {
	
	public Lexeme env;
	
	public Environment()
	{
		env = newEnvironment();
	}
	
	public Lexeme newEnvironment()
	{
		return cons(cons(null, cons(null, null)), null);
	}
	
	public Lexeme cons(Lexeme lex1, Lexeme lex2)
	{
		Lexeme n = new Lexeme();
		n.left = lex1;
		n.right = lex2;
		return n;
	}
	
	public Lexeme car(Lexeme env)
	{
		return env.left;
	}
	
	public Lexeme cdr(Lexeme env)
	{
		return env.right;
	}
	
	public Lexeme populate(Lexeme var, Lexeme val, Lexeme env)
	{
		Lexeme local = car(env);
		local.left = cons(var, local.left);
		local.right.left = cons(val, local.right.left);
		return local;
	}
	
	public boolean varExists(Lexeme var, Lexeme env)
	{
		while (env != null)
		{
			Lexeme local = car(env), vars = car(local);
			while (vars != null)
			{
				if (sameVar(var, car(vars))) return true;
				else
				{
					vars = cdr(vars);
				}
			}
			
			env = cdr(env);
		}
		
		return false;
	}
	
	public Lexeme lookUp(Lexeme var, Lexeme env)
	{
		while (env != null)
		{
			Lexeme local = car(env), vars = car(local), vals = car(cdr(local));
			while (vars != null)
			{
				if (sameVar(var, car(vars)))
				{
					return car(vals);
				}
				else
				{	
					vars = cdr(vars);
					vals = cdr(vals);
				}
			}
			
			env = cdr(env);
		}
		System.out.println("Variable " + var.sval + " not found");
		return null;
	}
	
	public Lexeme update(Lexeme var, Lexeme val, Lexeme env)
	{
		while (env != null)
		{
			Lexeme local = car(env), vars = car(local), vals = car(cdr(local));
			while (vars != null)
			{
				if (sameVar(var, car(vars)))
				{
					vals.left = val;
					return local;
				}
				else
				{
					vars = cdr(vars);
					vals = cdr(vals);
				}
			}
			
			env = cdr(env);
		}
		System.out.println("Variable not found");
		return null;
	}
	
	public Lexeme extend(Lexeme vars, Lexeme vals, Lexeme env)
	{
		return cons(cons(vars, cons(vals, null)), env);
	}
		
	public boolean sameVar(Lexeme var1, Lexeme var2)
	{
		if (var1.sval.equalsIgnoreCase(var2.sval)) return true;
		else return false;
	}
	
	public void display(Lexeme env)
	{
		while (env != null)
		{
            displayLocal(env);

			env = cdr(env);
		}
		
		System.out.print("\n");
	}
	
	public void displayLocal(Lexeme env)
	{
		Lexeme local = car(env), vars = car(local), vals = car(cdr(local));
		if (vars == null) System.out.println("EMPTY");
		while (vars != null)
		{
            System.out.print(car(vars).sval + "= ");
			if (car(vals).type == "INTEGER")
			{
				System.out.print(car(vals).ival);
			}
			else if (car(vals).type == "REAL")
			{
				System.out.print(car(vals).rval);
			}
			else if (car(vals).type == "STRING")
			{
				System.out.print("\"" + car(vals).sval + "\"");
			}
            System.out.print("; ");
			vars = cdr(vars);
            vals = cdr(vals);
		}
	}
}
