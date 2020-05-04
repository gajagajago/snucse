/*
 * Description
 * ===========
 * This program is run under three processes. 
 * 
 * 1. infixToPostfix : Change infix string into a postfix. 
 * 2. printPostfix : Change postfix into a string and print.
 * 3. calculatePostfix : Calculate postfix. 
 * 
 * If exception occurs in any of the process, only throw `ERROR`.   
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.ArrayList;

//
// Program Initialization
//
public class CalculatorTest
{
	public static void main(final String args[])
	{
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				final String input = br.readLine();

				if (input.compareTo("q") == 0) 
					break;
				
				ArrayList<String> postfix = infixToPostfix(input);				
				Long result = calculatePostfix(postfix);
				
				printPostfix(postfix);
				System.out.print("\n");
				System.out.println(result);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}
	
	// Assign each operator preference based on precedence level.  
	public static int getPreference(char c) {
		int pref;
		
		switch (c) {
			case '+' : case '-' : 
				pref = 1;
				break;
			case '*' : case '/' : case '%' : 
				pref = 2;
				break;
			case '~' :
				pref = 3;
				break;
			case '^' :
				pref = 4;
				break;	
			default : 
				pref = 0;
				break;
		}
		
		return pref;
	}
	
	//
	// Operation 1 - 
	// Change infix string into a postfix arraylist. 
	//
	public static ArrayList<String> infixToPostfix(String s) throws Exception {
		
		Stack<Character> operatorStack = new Stack<Character>();
		ArrayList<String> postfix = new ArrayList<String>();
		
		boolean afterNumber = false;		// whether next 'char' is preceded by num		
		boolean isDigit = false;			// whether next 'num' is preceded by num 
		boolean afterSpace = false;			// whether next 'char' is preceded by space/indent 
		boolean isRightAssociative = false;	// whether next 'operator' is right associative == ('~', '^')  
		
		for(int i = 0; i < s.length(); ++i) {
			char target = s.charAt(i);
			
			if(target == ' ' || target == '\t') {
				afterSpace = true;
				continue;
			}
			
			if(target == '(') {
				if(afterNumber)
					throw new Exception();
				
				operatorStack.push(target);
				
				isDigit = false;
				afterSpace = false;
				isRightAssociative = false;
			}
			
			else if(target == ')') {
				boolean isParenthesesOpened = false;	// check if ')' is twined with '('
				
				if(!afterNumber) 
					throw new Exception();
				
				if(operatorStack.isEmpty()) 
					throw new Exception();
				
				while(!operatorStack.isEmpty()) {
					if(operatorStack.peek() == '(') {
						operatorStack.pop();
						isParenthesesOpened = true;	
						break;
					} else {
						postfix.add(operatorStack.pop()+"");
					}				
				}
				
				if(!isParenthesesOpened)
					throw new Exception();
				
				isDigit = false;
				afterSpace = false;
				isRightAssociative = false;
			} 
			
			else if(target == '-') {
				if(!afterNumber) {
					target = '~';
					isRightAssociative = true;
				} else {
					isRightAssociative = false;
					afterNumber = false;
				}
				
				// pop operators with higher precedence from stack 
				while(!operatorStack.isEmpty() && getPreference(operatorStack.peek()) >= getPreference(target)) {
					// break at ~ ~ case
					if(getPreference(operatorStack.peek()) == getPreference(target) && isRightAssociative == true) 
						break;
					
					postfix.add(operatorStack.pop()+"");
				}
				operatorStack.push(target);
					
				isDigit = false;
				afterSpace = false;
			}
			
			else if(target == '+' || target == '*' || target == '/' || target == '%' || target == '^') {
				if(target == '^') 
					isRightAssociative = true;
				
				else {
					isRightAssociative = false;
				}
				
				if(!afterNumber) 
					throw new Exception();
				
				else {
					if(operatorStack.isEmpty()) 
						operatorStack.push(target);
					
					else {
						// pop operators with higher precedence from stack 
						while(!operatorStack.isEmpty() && getPreference(operatorStack.peek()) >= getPreference(target)) {
							 // break at ^ ^ case
							if(getPreference(operatorStack.peek()) == getPreference(target) && isRightAssociative == true) 
								break;
							
							postfix.add(operatorStack.pop()+"");
						}
						operatorStack.push(target);
					}
					
					afterNumber = false;
					isDigit = false;
					afterSpace = false;
				}
			}
			
			else {	// target == 'number'				
				if(isDigit) {
					if(afterSpace)
						throw new Exception();
					
					// group each digits into one whole number
					String digit = postfix.get(postfix.size()-1);
					String sb = digit.concat(target+"");
					postfix.set(postfix.size()-1, sb);
				} else {							
					postfix.add(target+"");
					isDigit = true;
				}
				
				afterNumber = true;
				afterSpace = false;
			}
		}
		
		if(!afterNumber) 
			throw new Exception();
		
		while(!operatorStack.isEmpty()){
			if(postfix.isEmpty())
				throw new Exception();
			
			postfix.add(operatorStack.pop()+"");
		}   
		
		return postfix;
	}
	
	//
	// Operation 2 - 
	// Change postfix into a string and print.
	//
	public static void printPostfix(ArrayList<String> postfix) {
		String print =  String.join(" ", postfix);
		
		System.out.print(print);
	}
	
	//
	// Operation 3 - 
	// calculate postfix
	//
	public static long calculatePostfix(ArrayList<String> postfix) throws Exception {
		Stack<Long> operandStack = new Stack<Long>();
		
		for(String s : postfix) {
			if(s.charAt(0) == '+' || s.charAt(0) == '-' || s.charAt(0) == '*' || 
					s.charAt(0) == '%' || s.charAt(0) == '/' || s.charAt(0) == '^') {
				long num2 = operandStack.pop();
				long num1 = operandStack.pop();
				long num = 0;
				
				switch(s.charAt(0)) {
					case '+' :
						num = num1 + num2;
						break;
					case '-' : 
						num = num1 - num2;
						break;
					case '*' :
						num = num1 * num2;
						break;
					case '%' : 
						if(num2 == 0)
							throw new Exception();
						num = num1 % num2;
						break;
					case '/' : 
						if(num2 == 0)
							throw new Exception();
						num = num1 / num2;
						break;
					case '^' : 
						if(num1 == 0 && num2 < 0)
							throw new Exception();
						num = (long) Math.pow(num1, num2);
						break;
					default : 
						break;
				}
				
				operandStack.push(num);
				
			} else if(s.charAt(0) == '~') {
				long num1 = operandStack.pop();
				operandStack.push(-1*num1);	
				
			} else {	// s.charAt(0) == 'number'
				operandStack.push(Long.parseLong(s));
			}
		}
		
		return operandStack.peek();
	}
}
