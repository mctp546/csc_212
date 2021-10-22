import java.text.ParseException;
//is this right? program gets mad if i don't have the main() method throw a ParseException
//didn't have this issue when using ParseException in Postfix class --> only happened for Calculator class

class Main {
  public static void main(String[] args) throws ParseException {
    Postfix.main(args);
    Calculator.main(args);
  }
}