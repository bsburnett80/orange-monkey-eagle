# makefile for dpl

# variable for compiler
jc = javac
JFLAGS = -g

.SUFFIXES: .java .class

arg = 

.java.class:
		$(jc) $(JFLAGS) $*.java

CLASSES = \
		Recognizer.java \
		Lexer.java \
		Lexeme.java \
		Environment.java \
		Types.java \
		Evaluator.java \
		Scanner.java \

default: classes

classes: $(CLASSES:.java=.class)

fibonacci: $(CLASSES:.java=.class)
		language fibonacciTest.txt

factorial: $(CLASSES:.java=.class)
		language factorialTest.txt

conditionals: $(CLASSES:.java=.class)
		language conditionalsTest.txt

arrays: $(CLASSES:.java=.class)
		language arrayTest.txt

operations: $(CLASSES:.java=.class)
		language operationsTest.txt

functions: $(CLASSES:.java=.class)
		language functionTest.txt

run: $(CLASSES:.java=.class)
		language $(arg)

adder: $(CLASSES:.java=.class)
		language fullAdder.txt

# remove .class files
clean:
		$(RM) *.class
