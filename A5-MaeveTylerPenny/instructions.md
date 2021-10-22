# Assignment 5:  Stack-Based Parsing of Arithmetic Expressions

This assignment will give you practice using stacks in a real parsing application. You will write a program that computes the result of arithmetic expressions. The simplest version will read an expression in postfix notation (e.g. `3 2 + 5 *`). For full credit, implement a form of the shunting yard algorithm to interpret regular infix expressions (e.g. `(3+2) * 5`).

## Specifications

Your program should read the expression to be computed from the command line, in the form shown below:

    java Calculate "(3+2)*5"

The quotation marks are required because the parentheses would otherwise be interpreted by the shell rather than being passed to your program as input. In this instance, the program should print out the answer 25.

For details on reading command line arguments, see the [guide](https://jcrouser.github.io/CSC212/redirection.html). (You don't need to complete the activity.)

## Tokenization
Since we are reading the expression from the command line arguments, it will be of type String. We can convert this to a series of discrete tokens using a Scanner. This will separate numbers, operators, parentheses, etc. and feed them to us one at a time. To see how the scanner works, compile and run the short demonstration program `Tokenizer.java`. You may also copy and use this as the starting point for your own program, if you wish.

## Postfix Computation
The simplest possible version of this assignment (sufficient for a passing grade) will only accept arithmetic expressions in postfix notation. For example, "3 2 + 5 \*" means (3+2)\*5 in our standard infix notation. You would call it like this:

    java Postfix "3 2 + 5 *"

As a reminder, here is the algorithm for computing the value of postfix expressions:

* Scan the tokens in order from left to right
* If the token is a number, push it on the stack
* If the token is an operator, pop two numbers off the stack, combine them using the operator, and push the result onto the stack
* Once all the tokens have been processed in this way, the stack should contain exactly one element: the result.

Even if you intend to implement the more complicated algorithm below, you should begin with a postfix calculator program named `Postfix.java`.

## Infix Computation
Handling infix expressions is slightly more complicated, since you have to deal with parentheses. However, it turns out that infix expressions can be converted (parens and all) into postfix ones using a single stack. You can hold the converted symbols in a queue as the conversion proceeds.  The symbols in the queue may then be converted to a final value using the postfix expression algorithm given above. The outline below is a simplified version (ignoring operator associativity) of the one given on Wikipedia: [Shunting-yard algorithm](http://en.wikipedia.org/w/index.php?title=Shunting-yard_algorithm&oldid=572362024). Note that to follow this implementation you will need one stack of type `Stack<Object>`. For the output queue, just use a `LinkedList<Object>` and use the `addLast method` to add things to it at the tail.

* While there are tokens to be read:
  * Read a token.
  * If the token is a number, then add it to the output queue.
  * If the token is an operator, o1, then:
    * while there is an operator token, o2, at the top of the stack, and o1 has precedence less than or equal to o2,
      * pop o2 off the stack, onto the output queue;
    * Finally push o1 onto the stack.
  * If the token is a left parenthesis, then push it onto the stack.
  * If the token is a right parenthesis:
  * Until the token at the top of the stack is a left parenthesis, pop operators off the stack onto the output queue.
  * Pop the left parenthesis from the stack, but not onto the output queue.
  * If the stack runs out without finding a left parenthesis, then there are mismatched parentheses.
* When there are no more tokens to read:
  * While there are still operator tokens in the stack:
    * If the operator token on the top of the stack is a parenthesis, then there are mismatched parentheses.
    * Pop the operator onto the output queue.
* Exit.

## Hints and Comments

Both the stack and the output stream for the shunting-yard algorithm will need to hold data of heterogeneous types: `Double` for the numbers, and `Character` for the operators (not to mention `String` for function names if you want to attempt the kudos challenge described below). When you pop an element off the stack and need to figure out what type it has, use the `instanceof` operator.  For example,  `s instanceof String` has the value `true` if `s` is type `String`.

Using your `Postfix.java` class as a basis, you should be able to reuse most of the code to handle the output of the shunting-yard algorithm without making too many changes.

The pseudocode above will work for the operators `+` `-` `*` and `/` but not `^` (exponentiation). While the first four operators are left-associative, the latter is right-associative and needs slightly different treatment (described in the the full pseudocode under the wikipedia link) -- but it boils down to using less than in the precedence comparison rather than less than or equal to. What does left-associative mean? We interpret 1+2+3 as (1+2)+3. On the other hand, 2^1^3 is interpreted as 2^(1^3) because ^ is right-associative. For full credit your program should implement the complete shunt-yard algorithm linked above from Wikipedia, including associativity.

## Kudos

For this assignment, you need only consider the normal arithmetic operators `+`, `-`, `*`, `/`, and `^` (addition, subtraction, multiplication, division, and exponentiation), plus parentheses for grouping. You may ignore the unary minus (e.g., `5+(-3)`). The full shunting-yard algorithm given at the wikipedia page can also handle the unary minus, constants such as `pi`, and functional expressions like `sin(0)` and `max(3,4)`. Can you augment your program to compute expressions that include elements like these? If you do, describe the augmentations in your `readme.md`.