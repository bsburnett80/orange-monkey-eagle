
// example Mini-C++ program

class Stack {
	int a[100], top;
public:
	void makeEmpty() {
		top=-1;
	}

	bool isEmpty() {
		return top==-1;
	}

	bool isFull() {
		return top>=99;
	}

	void push (int value) {
		if (!isFull()) {
			top=top+1;
			a[top]=value;
		}
	}

	int pop() {
		if (isEmpty())
			return 0;
		else {
			int temp;
			temp=a[top];
			top=top-1;
			return temp;
		}
	}
};


void main() {            // driver to test the Stack abstract data type
	Stack s;
	s.makeEmpty();
	int k;
	k=1;
	while (k<=1024) {
		s.push(k);
		k=k*2;
	}

	while(!s.isEmpty())
	{
		cout<<s.pop();
		cout<<endl;
	}
}
