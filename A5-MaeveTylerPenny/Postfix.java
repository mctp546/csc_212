import java.util.Scanner;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.text.ParseException;



public class Postfix {
  /** Pattern that matches on arithmetic symbols */
  public static final String SYMBOL = "[^\\w]";

  /** Pattern that matches on words */
  public static final String WORD = "[a-zA-Z]*\\b";

  /** Run short test */
  public static void main(String[] args) throws ParseException {
    if (args.length == 0) {
      System.err.println("Usage:  java Postfix <expr>");
    } else {
      /** Scanner that scans input from console as a string and split the string at various boundaries*/
      Scanner input = new Scanner(new StringReader(args[0]));
      // Below is a complicated regular expression that will split the
      // input string at various boundaries.
      input.useDelimiter("(\\s+" // whitespace
          + "|(?<=[a-zA-Z])(?=[^a-zA-Z])" // word->non-word
          + "|(?<=[^a-zA-Z])(?=[a-zA-Z])" // non-word->word
          + "|(?<=[^0-9\\056])(?=[0-9\\056])" // non-number->number
          + "|(?<=[0-9\\056])(?=[^0-9\\056])" // number->non-number
          + "|(?<=[^\\w])(?=[^\\w]))"); // symbol->symbol

      /** stack (implemented using an ArrayDeque) to hold doubles during postfix calculation */
      ArrayDeque<Double> stack = new ArrayDeque<>();

      try {
        while (input.hasNext()) {
          if (input.hasNextDouble()) {
            stack.push(input.nextDouble());
          } else if (input.hasNext(SYMBOL)) {
            String symbol = input.next(SYMBOL);
            if (stack.peekFirst() == stack.peekLast()) {
              throw new ParseException("Malformed expression", 0); //what does "Resource leak: 'input' is not closed at this location" mean?
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

        // will only return result if stack has one value left in it (the result) --> otherwise, throws ParseException
        if (stack.peekFirst().equals(stack.peekLast())) {
          System.out.println("result: "+stack.pop());
        } else {
          throw new ParseException("Could not calculate result", 0);
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
    
    }
  }


}