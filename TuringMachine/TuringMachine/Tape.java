package TuringMachine;

import java.util.LinkedList;

public class Tape {
	
	public class Node {
		public String value;
		public Node(String value) { this.value = value; }
	}
	
	LinkedList<Node> tape = new LinkedList<Node>();
	private Node head;
	private static final String BLANK = "_";
	private Node Blank = new Node(BLANK);
	
	public Tape(String input) {
		if (input == null || input.equals("")) input = BLANK;
		for (int i = 0; i <= (input.length() - 1); i++) {
			String s = input.substring(i, i+1);
			tape.addLast(new Node(s));
		}
		head = tape.getFirst();
	}
	
	public void moveLeft() {
		int i = tape.indexOf(head);
		if (i == 0) {
			tape.addFirst(Blank);
			head = tape.getFirst();
		}
		else {
			i--;
			Node temp = tape.get(i);
			head = temp;
		}
	}
	
	public void moveRight() {
		int i = tape.indexOf(head);
		i++;
		if (i < (tape.size())) {
			Node temp = tape.get(i++);
			head = temp;
		}
		else {
			tape.addLast(Blank);
			head = tape.get(i);
		}
	}
	
	public String read() {
		return head.value;
	}
	
	public void write(String s) {
		int index = tape.indexOf(head);
		Node n = new Node(s);
		tape.set(index, n);
		head = n;
	}
	
	public String printList() {
		int len = tape.size();
		String S = "";
		for (int i = 0; i <= len - 1; i++) {
			S += "|";
			if (head == tape.get(i)) S += "\"" + tape.get(i).value + "\"";
			else S += tape.get(i).value;
			S += "|";
		}
		return S;
	}
	
	public String toString() {
		return BLANK + BLANK + printList() + BLANK + BLANK;
		
	}
}
