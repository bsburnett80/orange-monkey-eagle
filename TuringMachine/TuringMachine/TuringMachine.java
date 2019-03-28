package TuringMachine;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TuringMachine {

	public class Transition {
		public final static String DEL = "|";
		private State to, from;
		private String[] read, write, move;
		
		public Transition(State from, State to, String[] read, String[] write, String[] move) {
			this.from = from;
			this.to = to;
			this.read = read;
			this.write = write;
			this.move = move;
		}
		public String[] getWrite() {
			return write;
		}
		
		public String[] getMove() {
			return move;
		}
		
		public String getRead() {
			String r = "";
			for (int i = 0; i < read.length; i++) r += read[i];
			return r;
		}
		public State getNextState() {
			return to;
		}
		public String toString() {
			return "from: " + from + ", to: " + ", read: " + read + ", write: " + write;
		}
	}
	
	public class State {
		private String name;
		private String type = "";
		
		public State(String name, String type) {
			this.name = name;
			this.type = type;
		}
		
		public State(String name) {
			this.name = name;
		}
		
		public boolean halt() {
			return type.equals("HALT");
		}
		public boolean accept() {
			return type.equals("FINAL");
		}
		public boolean reject() {
			return type.equals("REJECT");
		}
		public String getStateName() {
			return name;
		}
		public String toString() {
			return name + " " + type;
		}
	}
	
	private HashMap<String, State> states = new HashMap<String, State>();
	private HashMap<String, Transition> transitions = new HashMap<String, Transition>();
	private State state;
	private BufferedReader reader;
	private int numTapes = 1;
	private ArrayList<Tape> tapes = new ArrayList<Tape>();
	
	public State getState() {
		return state;
	}
	
	public void setState(State s) {
		state = s;
	}
	
	public State addState(String n) { 
		return this.addState(n,"REJECT");
	}
	
	public State addState(String n, String t) {
		State s = new State(n, t);
		states.put(n, s);
		if (state == null) state = s; // sets s as starting state
		return s;
	}
	
	public State findState(String s) {
		return states.get(s);
	}
	
	public void addTransition(String f, String t, String[] r, String[] w, String[] m) {
		State from = findState(f);
		if (from == null) {
			from = this.addState(f);
			//from = findState(f);
		}
		State to = findState(t);
		if (to == null) {
			to = this.addState(t);
			//to = findState(t);
		}
		Transition trans = new Transition(from, to, r, w, m);
		String read = "";
		for (int i = 0; i < r.length; i++) read += r[i];
		if (transitions.get(f + Transition.DEL + read) != null) {
			throw new RuntimeException("This Turing Machine is non-deterministic\nPlease try again.");
		}
		transitions.put(f + Transition.DEL + read, trans);
	}

	public Transition getTransition(String r) {
		return transitions.get(state.getStateName() + Transition.DEL + r);
	}
	
	// Input file
	public TuringMachine(String fname, String s) throws IOException {
		File file = new File(fname);
		reader = new BufferedReader(new FileReader(file));
		
		String line = reader.readLine().trim();
		while (true) {
			while (!line.equalsIgnoreCase("start")) {
				line = reader.readLine().trim();
			}
			while (!line.equalsIgnoreCase("final")) {
				line = reader.readLine().trim();
				if (line.equals("")) continue;
				if (line.startsWith("//")) continue;
				if (line.equalsIgnoreCase("final")) continue;
				this.addState(line);
			}
			while (!line.equalsIgnoreCase("transitions")) {
				line = reader.readLine().trim();
				if (line.equals("")) continue;
				if (line.startsWith("//")) continue;
				if (line.equalsIgnoreCase("transitions")) continue;
				State t = this.findState(line);
				if (t != null) {
					t.type = "FINAL";
				}
				else {
					this.addState(line, "FINAL");	
				}
			}
			while (!line.equalsIgnoreCase("number of tapes")) {
				line = reader.readLine().trim();
				if (line.equals("")) continue;
				if (line.startsWith("//")) continue;
				if (line.equalsIgnoreCase("number of tapes")) continue;
				//line = reader.readLine().trim();
				String[] ifField = line.split(":");
				String from = ifField[0];
				String[] reads = ifField[1].split(",");
				line = reader.readLine().trim();
				String[] doField = line.split(":");
				String to = doField[0];
				String[] writesMoves = doField[1].split(";");
				this.addTransition(from, to, reads, writesMoves[0].split(","), writesMoves[1].split(","));
			}
			while (!line.equalsIgnoreCase("input string")) {
				line = reader.readLine().trim();
				if (line.equals("")) continue;
				if (line.startsWith("//")) continue;
				if (line.equalsIgnoreCase("input string")) continue;
				numTapes = Integer.parseInt(line);
			}
			while (!line.equalsIgnoreCase("end")) {
				line = reader.readLine();
				//if (line.equals("")) continue;
				if (line.startsWith("//")) continue;
				if (line.equalsIgnoreCase("end")) continue;
				if (s.equals("")) {
					for (int i = 0; i < numTapes; i++) {
						tapes.add(new Tape(line));
						line = reader.readLine().trim();
					}
				}
				else {
					tapes.add(new Tape(s));
//					for (int i = 1; i < numTapes; i++) {
//						tapes.add(new Tape(""));
//					}
				}
			}
			if (line.equalsIgnoreCase("end")) break;
		}
		@SuppressWarnings("unused")
		int a = 0;
	}
	
	public void simulate() {
		State currentState = this.getState();
		ArrayList<String> read = new ArrayList<String>();
		boolean halt = false;
		boolean accept = false;
		boolean reject = false;
		
		while (halt == false && accept == false && reject == false) {
			for (int i = 0; i < tapes.size(); i++) {
				read.add(tapes.get(i).read());
			}
			String r = "";
			for (int i = 0; i < read.size(); i++) r += read.get(i);
			Transition trans = this.getTransition(r);
			if (trans != null) {
				currentState = trans.getNextState();
				this.setState(currentState);
				String[] write = trans.getWrite();
				String[] move = trans.getMove();
				for (int i = 0; i < tapes.size(); i++) {
					tapes.get(i).write(write[i]);
					if (move[i].equalsIgnoreCase("S")) continue;
					if (move[i].equalsIgnoreCase("R")) tapes.get(i).moveRight();
					if (move[i].equalsIgnoreCase("L")) tapes.get(i).moveLeft();
				}
				read.clear();
			} else {
				if (currentState.accept()) {
					System.out.println("INPUT IS ACCEPTED.");
					printTapes();
					accept = true;
				}
				if (currentState.reject()) {
					System.out.println("INPUT IS REJECTED.");
					printTapes();
					reject = true;
				}
				if (currentState.halt()) {
					System.out.println("MACHINE HAS HALTED.");
					printTapes();
					halt = true;
				}
			}
		}
	}

	private void printTapes() {
		for (int i = 0; i < tapes.size(); i++) {
			int j = i + 1;
			System.out.println("tape-" + j + ": " + tapes.get(i).toString());
		}
	}
}
