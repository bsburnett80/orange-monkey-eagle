Readme file for my DPL

I've included a makefile with all of my test files having a make command,
also can be ran using "language 'FILE_NAME'"

To show that recursion works, I've included the test files fibonacciTest.txt
and factorialTest.txt. I've gotten fibonacciTest to work up to fib(32) but it
about 10 seconds or so to spit out the number. factorialTest works up to around
the same number.

To run fibonacciTest, use the command "make fibonacci"
To run factorialTest, use the command "make factorial"

My language uses the ArrayList implementation in Java which has a constant time lookup,
and an amortized constant time insert. To show that arrays work, and you can set and get
values at certain points, I've included arrayTest.txt.

To run arrayTest, use the command "make arrays"

My language supports the normal operations, +, -, *, /, %, ^. To show this, I've included
operationsTest.txt.

To run operationsTest, use the command "make operations" (sensing a pattern with the names yet?)

My language supports traditional while and if/if-else statements. To show that these conditionals
work, conditionalsTest.txt was included.

To run conditionalsTest, use the command "make conditionals"

My language also supports functions as objects. Many of my test files show this, but
functionTest.txt was created to show this, but operationsTest.txt and fullAdder.txt
show this feature as well.

To run functionsTest, use the command "make functions"

To change the values of the signals in fullAdder.txt, on what should be lines 304, 306, 308,
should be a.setSignal(X), b.setSignal(X), and c.setSignal(X), respectively. Change the value of 
X to set the signal as something different.

To run fullAdder, use the command "make adder"

To write your own program, the instructions below should take care of walking you through how it's
structured.

To run any file not submitted, use the command "make run arg=FILE_NAME"

Also, the command "make clean" removes all the ".class" files

That pretty much sums up everything needed to run the files for my language. Enjoy.

To write programs in my language,
    
    1. Every file must start with a class ID { body }
    2. To define a function --> define function ID (param1, param2, ...) { body } **the paramList can also be empty**
    3. To call a function --> function ID (arg1, arg2, ...) **the argList can be empty also**
    4. To access a function in a function, use a dot operator --> ID.FUNC_NAME(args) **the id must be assigned to the outer function previously**
    5. Accessing a variable in a function is the same except leave off the (args) --> ID.VAR_NAME
    6. Dot calls must be on the RHS of an "=", ex. i = a.value will assign i the value of a.value, but a.value = i will break
    7. For if and while statements of equality:
        A. Less than --> "<"
        B. Greater than --> ">"
        C. Equality --> "=" (single = instead of the normal ==)
        D. Not equal --> "!" (exclamation point)
        E. There is no true/false keyword, so true should be evaluated to 1, and false to 0
        F. Must be structured, if ( x = 1 ) { body } else { body }, while ( i < 10 ) { body }, where the operators can be what's in either A, B, C.
        G. The AND operator is & and can be used to see if two boolExpressions are true --> x=1 & y=1
        H. The OR operator is | and can be used to see if either of two are true --> x=1 | y=1
    8. To create an array --> array ID = [array_val0, array_val1, ...]
    9. To assign a variable to a value at a certain point in an array --> i = ID@x, where x can be an INT or variable representing an INT
    10. To set the value at an index in the array --> set ID$x = i, again, x can be an INT or variable representing an INT
    11. Assigning variables --> i = value, where value can be anything from INTs, REALs, STRINGs, or function calls
    12. Math functions --> c = a + b, again with single IDs on the LHS

That should be everything about how to write a program in my language.

Also, for some reason, when I'm testing, on my pretty printer, the first letter of some of the words is getting "covered" up by a BRACE,
it only does it on my Terminal on my Mac, and it just started that tonight (Sunday, Dec 11) and I don't have time to go through and try to fix it.
It doesn't affect how any of the code gets evaluated, just how it looks on my Terminal window.
