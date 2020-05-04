import java.io.*;
import java.util.Vector;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Type
  implements Comparable<Type>
{
  NUMBER(0, false), PARENTHESES(1, false), SUM(2, false),
  MULTIPLE(3, false), UNARY(4, true), EXPONENTIAL(5, true);

  private int precedence;
  public final boolean rightAssociative;

  private Type(int precedence, boolean isRightAssoc)
  {
    this.precedence = precedence;
    this.rightAssociative = isRightAssoc;
  }
}


class Token implements Comparable<Token>
{
	public Type type;
	public Object value;
	
	public Token(Object value) {
		if(value instanceof Long) {
			type = Type.NUMBER;
		}

		else {
			final char op = ((String) value).charAt(0);

			switch (op) {
				case '(' : case ')' :
					type = Type.PARENTHESES;
					break;
				case '^' :
					type = Type.EXPONENTIAL;
					break;
				case '~' : 
					type = Type.UNARY;
					break;
				case '*' : case '/' : case '%' : 
					type = Type.MULTIPLE;
					break;
				case '+' : case '-' : 
					type = Type.SUM;
					break;
			}
		}
		this.value = value;
	}

	@Override
	public int compareTo(final Token t) {
		return this.type.compareTo(t.type);
	}

	public String toString()
	{
		if (type == Type.NUMBER)
			return String.valueOf((Long) value);
		else
			return (String) value;
	}
}

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

				if (input.compareTo("q") == 0) {break;}

				Vector<Token> tempTokens = tokenize(input);
				long result = evaluate(tempTokens);
				boolean firstDone = false;
				for (Token token: tempTokens)
				{
					if (!firstDone)
						firstDone = true;
					else
						System.out.print(" ");

					System.out.print(token.toString());
				}
				System.out.println();

				System.out.println(result);
				
			}
			catch (final Exception e)
			{
				System.out.println("ERROR");
				// System.out.println("wrong input : " + e.toString());
			}
		}
	}


	public static Vector<Token> tokenize(final String input) throws Exception
	{	
		Vector<Token> tokens = new Vector<Token>();
		Stack<Token> operatorStack = new Stack<Token>();
		
		final Pattern p = Pattern.compile("[+\\-*/%^()]|\\d+");
		final Matcher m = p.matcher(input);

		boolean afterNumber = false;
		
		while(m.find()) {

			String tokenStr = m.group();
			final char token = tokenStr.charAt(0);

			switch(token) {
				case '-':
					if (afterNumber == false)
					{
						
						afterNumber = true;
						tokenStr = "~";
					}

				case '+' : case '*' : case '/' : case '%' : case '^' : {
					if(!afterNumber) {
						throw new Exception();
					} else {
						afterNumber = false;
						arrangement(tokenStr, tokens, operatorStack);
					}
				}
					break;


				case '(':
					if (afterNumber)
						throw new Exception();

					operatorStack.push(new Token(tokenStr));
					break;

				case ')':
					if (!afterNumber)
						throw new Exception();

					if (!parenthesisValidation(tokens, operatorStack))
						throw new Exception();

					break;

				default	:
					// System.out.println("Default");
					if(afterNumber) {
						// System.out.println("Exception thrown afterNumber");
						throw new Exception();
					} else {
						long num = Long.parseLong(tokenStr);
						// System.out.println("num: " + num);
						Token token1 = new Token(num);
						// System.out.println("token: " + token1);
						tokens.add(token1);
						afterNumber = !afterNumber;
						break;
					}
					
			}
		}
		// System.out.println("After while");
		if (!afterNumber)
			throw new Exception();

		while (!operatorStack.empty()) {
			// System.out.println(" - emptying stack");
			Token token = operatorStack.pop();
			// System.out.println(" [*] " + token);
			tokens.add(token);
		}

		return tokens;	
	}

	private static void arrangement(String tokenStr, Vector<Token> tokens, Stack<Token> operatorStack)
	{
		Token token = new Token(tokenStr);

		while (!operatorStack.empty())
		{
			Token peekToken = operatorStack.peek();

			if (peekToken.compareTo(token) < 0)
				break;
			else if (peekToken.compareTo(token) == 0 && token.type.rightAssociative)
			{
				break;
			}
			Token popped = operatorStack.pop();
			tokens.add(popped);
		}
		operatorStack.push(token);
	}

	private static boolean parenthesisValidation(Vector<Token> tokens, Stack<Token> operatorStack)
	{
		boolean balanced = false;

		while (!operatorStack.empty())
		{
			Token peekToken = operatorStack.peek();
			if (peekToken.type == Type.PARENTHESES)
			{
				operatorStack.pop();
				balanced = true;
				break;
			}
			Token popped = operatorStack.pop();
			tokens.add(popped);
		}

		return balanced;
	}

	private static long evaluate(Vector<Token> tokens) throws Exception
	{
		// System.out.println("In evaluate");
		Stack<Long> numbers = new Stack<Long>();

		for (Token token : tokens)
		{
			// System.out.println("token: " + token);
			if (token.type == Type.NUMBER){
				long num = (long) token.value;
				numbers.push(num);
			}
			else if (token.type == Type.UNARY)
				numbers.push(-1 * numbers.pop());
			else
			{
				long right = numbers.pop();
				long left = numbers.pop();

				char character = token.toString().charAt(0);
				if (character == '+')
				{
					numbers.push(left + right);
				}
				else if (character == '-')
				{
					numbers.push(left - right);
				}
				else if (character == '*')
				{
					numbers.push(left * right);
				}
				else if (character == '/')
				{
					if (right == 0)
						throw new Exception();
					numbers.push(left / right);
				}
				else if (character == '%')
				{
					if (right == 0)
						throw new Exception();
					numbers.push(left % right);
				}
				else if (character == '^')
				{
					if (left == 0 && right < 0)
						throw new Exception();
					long answer = (long)Math.pow((double)left, (double)right);
					numbers.push(answer);
				}
				else
				{
					throw new Exception();
				}
			}
		}
		long result = numbers.pop();

		if (numbers.empty())
			return result;
		else
			throw new Exception();
	}



}

