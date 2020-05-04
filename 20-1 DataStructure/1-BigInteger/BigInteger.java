import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigInteger {
    private boolean isPositive = true;
    private byte[] inputNum;
    private int digits;

    public BigInteger(String userInput) {
        String input = userInput;
        if(input.charAt(0) == '-') {
            input = input.substring(1);
            isPositive = false;
        }
        if(input.charAt(0) == '0') { // -0 case debug
        	isPositive = true;
        }
        if(input.charAt(0) == '+') {
            input = input.substring(1);
        }
        int length = input.length(); 
        digits = length;
        inputNum = new byte[256];

        for(int i =0; i < length; i++) {
            inputNum[length-i-1] = (byte)(input.charAt(i)-'0');
        }
    }

    public BigInteger(BigInteger big) {
        digits = big.digits;
        isPositive = big.isPositive;
        inputNum = new byte[256];

        for (int i = 0; i < digits; i++) {
            inputNum[i] = big.inputNum[i];
        }
    }

    public BigInteger add(BigInteger big) {
        if (!big.isPositive) {
            if (this.isPositive) {
                BigInteger copy = new BigInteger(big);
                copy.isPositive = true;
                this.subtract(copy);
                return this;
            }
        } else if (!this.isPositive) {
            BigInteger copy = new BigInteger(big);
            this.isPositive = true;
            copy.subtract(this);
            this.isPositive = copy.isPositive;
            this.inputNum = copy.inputNum;
            this.digits = copy.digits;
            return this;
        }

        for (int i = 0; i < big.digits; ++i) {
            this.inputNum[i] += big.inputNum[i];
            this.inputNum[i + 1] += this.inputNum[i] / 10;
            this.inputNum[i] %= 10;
        }

        this.digits = Math.max(this.digits, big.digits);

        for (int i = big.digits; i < this.digits; ++i) {
            this.inputNum[i + 1] += this.inputNum[i] / 10;
            this.inputNum[i] %= 10;
        }

        if (this.inputNum[this.digits] != 0) {
            this.digits++;
        }

        return this;
    }

    public BigInteger subtract(BigInteger big) {
        if (!big.isPositive) {
            if (this.isPositive) {
                BigInteger copy = new BigInteger(big);
                copy.isPositive = true;
                this.add(copy);
                return this;
            } else {
                BigInteger copy = new BigInteger(big);
                copy.isPositive = true;
                copy.add(this);
                this.isPositive = copy.isPositive;
                this.inputNum = copy.inputNum;
                this.digits = copy.digits;
                return this;
            }
        } else if (!this.isPositive) {
            BigInteger copy = new BigInteger(big);
            copy.isPositive = false;
            this.add(copy);
            return this;
        }

        BigInteger copy1 = new BigInteger(this);
        BigInteger copy2 = new BigInteger(big);

        BigInteger greater = copy1, smaller = copy2;

        if (this.digits < big.digits) {
            greater = copy2;
            smaller = copy1;
            this.isPositive = false;
        } else if (this.digits == big.digits) {
            boolean thisGreater = true;

            for (int i = this.digits - 1; i >= 0; --i) {
                if (this.inputNum[i] < big.inputNum[i]) {
                    thisGreater = false;
                    break;
                } else if (this.inputNum[i] > big.inputNum[i]) {
                    break;
                }
            }

            if (!thisGreater) {
                greater = copy2;
                smaller = copy1;
                this.isPositive = false;
            }
        }

        for (int i = 0; i < smaller.digits; ++i) {
            greater.inputNum[i] -= smaller.inputNum[i];
            if (greater.inputNum[i] < 0) {
            	greater.inputNum[i + 1]--;
            	greater.inputNum[i] = (byte)(greater.inputNum[i] + 10);
            	}
        }

        for (int i = smaller.digits; i < greater.digits; ++i) {
            if (greater.inputNum[i] < 0) {
            	greater.inputNum[i + 1]--;
                greater.inputNum[i] = (byte)(greater.inputNum[i] + 10);
            	}
        }

        greater.digits = 1;
        for (int i = 255; i >= 0; --i) {
            if (greater.inputNum[i] != 0) {
                greater.digits = i + 1;
                break;
            }
        }

        this.inputNum = greater.inputNum;
        this.digits = greater.digits;

        return this;
    }

    public BigInteger multiply(BigInteger big) {
        boolean positiveProduct = this.isPositive == big.isPositive;

        BigInteger answer = new BigInteger("0");

        for (int i = 0; i < big.digits; ++i) {
            BigInteger intermediate = new BigInteger("0");
            intermediate.digits = i;

            for (int j = 0; j < this.digits; ++j) {
                byte product = (byte)(this.inputNum[j] * big.inputNum[i]);
                byte carry = (byte)(product / 10);
                product %= 10;

                intermediate.inputNum[intermediate.digits] += product;
                intermediate.inputNum[intermediate.digits + 1] += carry;
                intermediate.digits++;
            }

            intermediate.digits = 1;
            for (int j = 255; j >= 0; --j) {
                if (intermediate.inputNum[j] != 0) {
                    intermediate.digits = j + 1;
                    break;
                }
            }

            answer.add(intermediate);
        }

        this.inputNum = answer.inputNum;
        this.digits = answer.digits;

        boolean isZero = true;

        for (int i = 0; i < this.digits; ++i) {
            if (this.inputNum[i] != 0) {
                isZero = false;
                break;
            }
        }

        if (isZero) {
            this.isPositive = true;
        } else {
            this.isPositive = positiveProduct;
        }
        
        return this;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(digits + 1);

        if (!isPositive) {
            sb.append("-");
        }

        for (int i = digits - 1; i >= 0; --i) {
            sb.append((char)(inputNum[i] + '0'));
        }

        return sb.toString();
    }

    private static final Pattern p = Pattern.compile("\\s*([+-]?)\\s*(\\d+)\\s*([+\\-*])\\s*([+-]?)\\s*(\\d+)\\s*");

    static BigInteger evaluate(String input) throws IllegalArgumentException {
        Matcher m = p.matcher(input);

        if (!m.matches()) {
            throw new IllegalArgumentException();
        }

        final String leftOp = m.group(1), 
        		      leftB = m.group(2), 
        		   binaryOp = m.group(3), 
        		    rightOp = m.group(4),
        		     rightB = m.group(5);

        if (binaryOp.length() != 1 || leftOp.length() > 1 || rightOp.length() > 1) {
            throw new IllegalArgumentException();
        }

        BigInteger leftBig = new BigInteger(leftOp+leftB), 
        		   rightBig = new BigInteger(rightOp+rightB);

        if(binaryOp.equals("+")) {
            return leftBig.add(rightBig);
        } else if (binaryOp.equals("-")) {
            return leftBig.subtract(rightBig);
        } else {
            return leftBig.multiply(rightBig);
        }

    }

    public static void main(String args[]) throws Exception {
        try (InputStreamReader isr = new InputStreamReader(System.in)) {
            try (BufferedReader br = new BufferedReader(isr)) {
                boolean done = false;
                while (!done) {
                    String input = br.readLine();
                    if (isQuitCmd(input))
                        done = true;
                    else
                        processInput(input);

                    try {

                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid input.");
                    }
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static boolean processInput(String input) throws IllegalArgumentException {
        BigInteger result = evaluate(input);

        System.out.println(result.toString());

        return true;
    }

    static boolean isQuitCmd(String input) {
        if (input == null) {
            return true;
        }
        return input.equalsIgnoreCase("quit");
    }
}