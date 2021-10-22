import java.util.Scanner;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.text.ParseException;
import java.util.ListIterator;
import java.util.Stack;



public class Calculator {
  /** Pattern that matches on arithmetic symbols */
  public static final String SYMBOL = "[^\\w]";

  /** Pattern that matches on words */
  public static final String WORD = "[a-zA-Z]*\\b";

  /** main function runs complete calculator */
  public static void main(String[] args) throws ParseException {
    calcPostfix(infixToPostfix(args));
  }

  /** 
   * @param infix mathematical expression input by user
   * @return LinkedList outputQ the output queue with given infix expression in postfix notation
   */
  public static LinkedList<Object> infixToPostfix(String[] args) throws ParseException {
    /** LinkedList output queue */
    LinkedList<Object> outputQ = new LinkedList<Object>();

    if (args.length == 0) {
      System.err.println("Usage:  java Calculator <expr>");
    } else {
      Scanner input = new Scanner(new StringReader(args[0]));
      // Below is a complicated regular expression that will split the
      // input string at various boundaries.
      input.useDelimiter("(\\s+" // whitespace
          + "|(?<=[a-zA-Z])(?=[^a-zA-Z])" // word->non-word
          + "|(?<=[^a-zA-Z])(?=[a-zA-Z])" // non-word->word
          + "|(?<=[^0-9\\056])(?=[0-9\\056])" // non-number->number
          + "|(?<=[0-9\\056])(?=[^0-9\\056])" // number->non-number
          + "|(?<=[^\\w])(?=[^\\w]))"); // symbol->symbol

      /** stack of type Object that stores symbols taken in by the scanner */
      Stack<Object> stack = new Stack<Object>();

      try {
      while (input.hasNext()) {
        if (input.hasNextDouble()) {
          outputQ.addLast(input.nextDouble());
        } else if (input.hasNext(SYMBOL)) {
          String symbol = input.next(SYMBOL);
          if (symbol.equals("(")) {
            stack.push(symbol); 
          } else if (symbol.equals(")")) {
            if (stack.isEmpty()) {
              throw new ParseException("Mismatched parenthses", 0); //what does "Resource leak: 'input' is not closed at this location" mean?
            } else {
              while (!stack.peek().equals("(")) {
                  Object operation = stack.pop();
                  outputQ.addLast(operation);
                
              }
            }
            if (stack.isEmpty()) {
              throw new ParseException("Mismatched parenthses", 0);
            } else {
              stack.pop();
            }
            /* if the symbol isn't a parentheses */
          } else {
            if (stack.isEmpty()) {
              stack.push(symbol);
              //System.out.println("stack now has one symbol in it: "+symbol);
            } else if (stack.peek().equals("(")) {
              stack.push(symbol);
            } else {
              if (symbol.equals("+") || symbol.equals("-")) {
                outputQ.addLast(stack.pop());
                stack.push(symbol);
              } else if (symbol.equals("*") || symbol.equals("/")) {
                if (stack.peek().equals("*") || stack.peek().equals("/")) {
                  outputQ.addLast(stack.pop());
                  stack.push(symbol);
                } else if (stack.peek().equals("+") || stack.peek().equals("-")) {
                  stack.push(symbol);
                }
              }
            }
          }
        } else {
          System.out.println("Unkown: "+input.next());
        }
      }
      while(!stack.isEmpty()) {
        Object operator = stack.pop();
        if (operator.equals("(")) {
          throw new ParseException("Mismatched parentheses", 0);
        } else {
          outputQ.addLast(operator);
        }
      }
      System.out.println("output queue final: " + outputQ.toString());
    
    } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    return outputQ;
    
  }

  /**
   * method that takes in the output queue returned by the infixToPostfix() method, calculates the result, and prints it to the console
   * @param inputList the output queue (in LinkedList form) returned by infixToPostix() method
   */
  public static void calcPostfix(LinkedList<Object> inputList) {

    /** ListIterator to iterate over output queue and pass objects into the postfix calculator */
    ListIterator<Object> input = inputList.listIterator(0);

    /** stack (implemented using an ArrayDeque) to hold doubles during postfix calculation */
    ArrayDeque<Double> stack = new ArrayDeque<>();

    try {
        while (input.hasNext()) {
          Object thing = input.next();
          if (thing instanceof Double) {
            stack.push((Double)thing);
          } else if (thing instanceof String) {
            String symbol = (String)thing;
            if (stack.peekFirst() == stack.peekLast()) {
              throw new ParseException("Malformed expression", 0);
            } else {
              double num1 = stack.pop();
              double num2 = stack.pop();
              if (symbol.equals("+")) {
                double sum = num2 + num1;
                stack.push(sum);
              } else if (symbol.equals("-")) {
                double sub = num2 - num1;
                stack.push(sub);
              } else if (symbol.equals("*")) {
                double product = num2 * num1;
                stack.push(product);
              } else if (symbol.equals("/")) {
                double divid = num2 / num1;
                stack.push(divid);
              }
            }
          } else {
              System.out.println("Unknown:  "+input.next());
            }
          }

        if (!stack.isEmpty() && stack.peekFirst().equals(stack.peekLast())) {
          System.out.println("result: "+stack.pop());
        } else {
          throw new ParseException("Could not calculate result", 0);
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
    
  }

    
}

