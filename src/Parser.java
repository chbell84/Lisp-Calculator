import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

interface CmdInterface
{
	String execute(double a, double b);
}

class Processor {

	private HashMap<String,CmdInterface> nameSpace;
	Processor(){
		nameSpace = new HashMap<String, CmdInterface>();
		CmdInterface add = (double x,double y)->{return String.valueOf(x+y);};
		CmdInterface subtract = (double x,double y)->{return String.valueOf(x-y);};
		CmdInterface multiply = (double x,double y)->{return String.valueOf(x*y);};
		CmdInterface divide = (double x,double y)->{return String.valueOf(x/y);};
		CmdInterface exponent = (double x,double y)->{return String.valueOf(Math.pow(x,y));};
		nameSpace.put("+", add);
		nameSpace.put("-", subtract);
		nameSpace.put("*", multiply);
		nameSpace.put("/", divide);
		nameSpace.put("^", exponent);
	}
	public String execute(ArrayList<Object> cmd) {
		CmdInterface operation = null;
		ArrayList<Double> params = new ArrayList<Double>(); 
		for(Object c:cmd) {
			if(c instanceof String) {
				if(operation==null&&nameSpace.containsKey(c))
					operation = nameSpace.get(c);
				else
					params.add(Double.valueOf((String)c));
			}
			else
				params.add(Double.valueOf(execute((ArrayList<Object>) c)));
		}
		return operation.execute(params.get(0),params.get(1));
	}
}

public class Parser {
	
	public static void main(String[] args) {
		Boolean done = false;
		Scanner in = new Scanner(System.in);
		while(!done) {
			System.out.print(":\t");
			String s = in.nextLine();
			if (s.contains("quit")){
				done = true;
				System.out.println("Quitting...");
				continue;
			}
			if(s.length()<7) continue;
		Processor p = new Processor();
		ArrayList<Object> command = parse(s);
		//System.out.println(command);
		System.out.println("Answer: " + p.execute(command));
		System.out.println();
		}
		in.close();
		
	}
	//Takes a String of Lisp code and iterates through to create nested ArrayList
	public static ArrayList<Object> parse(String s) {
		char [] line = s.toCharArray();
		ArrayList<Object> parsedList = new ArrayList<Object>();
		parse(parsedList, line, 1);
		return parsedList;
	}
	
	public static int parse(ArrayList<Object> line, char[] l, int i) {
		String w="";
		for(;i<l.length;i++) {
			if(l[i]=='(') {
				ArrayList<Object> subLine = new ArrayList<Object>();
				i=parse(subLine, l, ++i);
				line.add(subLine);
			}
			else if(l[i]==')') {
				if(!w.isBlank()) {
					line.add(w);
					w="";
				}
				return i;
			}
			else if(l[i]==' ') {
				if(!w.isBlank()) {
					line.add(w);
					w="";
				}
			}
			else
				w+=l[i];
		}
		return i;
	}
}
//tailcall recursion