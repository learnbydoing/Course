// GENERATED

/* INSTRUCTIONS
 *
 * In this assignment, you will gain experience with several different concepts:
 * - case classes and objects
 * - representation of programs as data structures
 * - interpreters
 * - behavior of closures
 *
 * You will do this by using (and extending) an interpreter for a dynamically-typed language similar to
 * the language with closures discussed in class.
 *
 * Complete the exercises below.  The exercises occur after the code for the interpreter below.
 * Search for "EXERCISE".  Most of the exercises should be completed by replacing "todoExp" or "todoVal" with
 * your own code.
 *
 * HINT: read the whole of this file before writing any code.  You might find helpful examples buried within.
 *
 * HINT: there are few tests inside this file.  This is deliberate.  You should write your own tests
 * to verify your work as you go.  You can print abstract syntax trees for expression and values
 * using the "prettyPrintExp" and "prettyPrintVal" functions.  You can evaluate your expressions using
 * the "evaluate" function (or just using "testExample" which calls "evaluate").
*/


object interpreter {

  sealed trait Val
  // ValInt represents integer literals.
  case class ValInt (n:Int) extends Val
  // ValStr represents string literals.
  case class ValStr (s:String) extends Val
  // ValNil represents the Nil pointer.
  case object ValNil extends Val
  // ValPair represents a pair with evaluated members, i.e., "fst" and "snd" have type "Val".  
  // Contrast with ExpPair, where "fst" and "snd" have type "Exp", further below.
  case class ValPair (fst:Val, snd:Val) extends Val
  // ValPrimFun represents a primitve function, i.e., a function written in Scala
  case class ValPrimFun (display:Option[String], f:Val=>Val) extends Val
  //case class ValAbs (nm:String, body:Exp) extends Val  
  // ValClosure represents a closure (not a lambda abstraction), i.e., there is a stored "env" environment 
  // in addition to the argNm variable and the body of the closure.
  case class ValClosure (env:Env, argNm:String, body:Exp) extends Val
  // NEW: ValRecClosure represents a closure for a recursively-defined function.  This differs from 
  // ValClosure by also storing the function name itself.
  case class ValRecClosure (env:Env, funNm:String, argNm:String, body:Exp) extends Val


  sealed trait Exp
  // ExpVal allows values to be used as expressions.
  case class ExpVal (v:Val) extends Exp
  // ExpVar represents variables.
  case class ExpVar (nm:String) extends Exp
  // ExpPrint represents an expression that prints the value of the expression, and then returns ValNil.
  case class ExpPrint (e:Exp) extends Exp
  // ExpPair represents a pair with members to be evaluated.  When the "fst" and "snd" have been evaluated, 
  // a ValPair is returned.
  case class ExpPair (fst:Exp, snd:Exp) extends Exp
  // ExpApp represents function application (a function "op" - short for operator - applied to a single argument "arg").
  case class ExpApp (op:Exp, arg:Exp) extends Exp
  // ExpLet represents an immutable binding of the value of an expression "e" to a variable "nm".  The binding
  // is only in scope in the expression "body".
  case class ExpLet (nm:String, e:Exp, body:Exp) extends Exp
  // ExpAbs creates an Anonymous function (also known as a lambda abstraction, hence the "Abs" in "ExpAbs").
  case class ExpAbs (argNm:String, body:Exp) extends Exp                     
  // NEW: ExpRecAbs adds a function definition syntax as an expression (not a value).  In order to allow recursion,
  // the function is named (so it is not an anonymous function / lambda abstraction).  This construct is more
  // expressive than ExpAbs above.  Note that it only allows taking a single argument.
  case class ExpRecAbs (funNm:String, argNm:String, body:Exp) extends Exp 
  // NEW: get first element of a pair
  case class ExpFst (e:Exp) extends Exp
  // NEW: get second element of a pair
  case class ExpSnd (e:Exp) extends Exp
  // NEW: Conditional expression representing "if (b) then tt else ff", where "b" is an Exp that is expected
  // to evaluate to an integer.  
  // If the integer is non-zero, then the value of the whole expression is the value resulting from evaluating "tt".  
  // If the integer is zero,  then the value of the whole expression is the value resulting from evaluating "ff".  
  // TODO: Uncomment the following line.
  case class ExpIf (b:Exp, tt:Exp, ff:Exp) extends Exp


  type Env = scala.collection.immutable.Map[String, Val]
  val emptyEnv:Env = scala.collection.immutable.Map.empty


  def error (s:String) = throw new RuntimeException (s)


  def evaluate (env:Env, e:Exp) : Val = {

    // OPTIONAL: if you want to see the intermediate steps of evaluation, uncomment the following line.
    // println (e + " --> ?   in environment " + env)

    val result:Val = e match {
      case ExpVal (v) => v

      case ExpVar (nm) => 
        val envContents:Option[Val] = env.get (nm)
        envContents match {
          case None => error ("variable " + nm + " not found in environment")
          case Some (v) => v
        }
    
      case ExpPrint (e) => 
        val eVal:Val = evaluate (env, e)
        println (prettyPrintVal (eVal))
        ValNil
    
      case ExpPair (fst, snd) => 
        val fstVal:Val = evaluate (env, fst)
        val sndVal:Val = evaluate (env, snd)
        ValPair (fstVal, sndVal)
    
      case ExpApp (op, arg) => 
        val opVal:Val = evaluate (env, op)
        opVal match {
          case ValPrimFun (_, f) => 
            val argVal:Val = evaluate (env, arg)
            f (argVal)
          
          // The application of a closure to an argument restores the environment from the closure during the
          // evaluation of the body (and also adds the binding of "nm" to the argument value "argVal").
          case ValClosure (envC, argNm, body) => 
            val argVal:Val = evaluate (env, arg)
            evaluate (envC + ( (argNm, argVal) ), body)
          /*
          case ValAbs (argNm, body) =>
            val argVal:Val = evaluate (env, arg)
            evaluate (env + ( (argNm, argVal) ), body)
        */
          // NEW: Similar to ValClosure, but also adds the recursive function back into the environment used to run the body of
          // the recursively-defined function.  Consequently, the body of the function can call itself (by referencing its name "funNm"). 
          case ValRecClosure (envC, funNm, argNm, body) => 
            val argVal:Val = evaluate (env, arg)
            evaluate (envC + ( (argNm, argVal) ) + ( (funNm, opVal) ), body)

          case _ => error ("expected function in application")
        }

      case ExpLet (nm, e, body) => 
        val eVal:Val = evaluate (env, e)
        evaluate (env + ( (nm, eVal) ), body)

      // A lambda abstraction expression evaluates to a closure value with the same argument variable "nm" and expression "body".
      // Additionally, the closure value captures the current environment, so that it can be restored when "body" is evaluated.
      case ExpAbs (argNm, body) =>
        ValClosure (env, argNm, body)

      // NEW: Behaves as for lambda abstractions ExpAbs above, except that the function name funNm is also captured.
      case ExpRecAbs (funNm, argNm, body) =>
        ValRecClosure (env, funNm, argNm, body)

      case ExpFst (e) =>
        val eVal:Val = evaluate (env, e)
        eVal match {
          case ValPair (fst, _) => fst
          case _ => error ("expected pair in fst")
        }

      case ExpSnd (e) =>
        val eVal:Val = evaluate (env, e)
        eVal match {
          case ValPair (_, snd) => snd
          case _ => error ("expected pair cell in snd")
        }

      // TODO: Add case for ExpIf below.
      case ExpIf (b, tt, ff) =>
        val eVal:Val = evaluate (env, b)
        eVal match {
          case ValInt(0) => evaluate (env, ff)
          case _ => evaluate (env, tt)
        }
    }

    // OPTIONAL: if you want to see the intermediate steps of evaluation and their results, uncomment the following line.
    // println (e + " --> " + result + "   in environment " + env)
    
    result
  }


  // Pretty print a value as a string.
  def prettyPrintVal (v:Val) : String = v match {
    case ValInt (n) => Integer.toString (n)
    case ValStr (s) => s
    case ValNil => "Nil"
    case ValPair (fst, snd) => "(" + prettyPrintVal (fst) + ", " + prettyPrintVal (snd) + ")"
    case ValPrimFun (display, f) => "[fun: " + display.getOrElse ("?") + "]"
    case ValClosure (env, argNm, body) => "[" + env + "; " + argNm + " => " + prettyPrintExp (body) + "]"
    case ValRecClosure (env, funNm, argNm, body) => "[" + env + "; " + funNm + "; " + argNm + " => " + prettyPrintExp (body) + "]"
  }


  // Pretty print an expression as a string.
  def prettyPrintExp (e:Exp) : String = e match {
    case ExpVal (v) => prettyPrintVal (v)
    case ExpVar (nm) => nm
    case ExpPrint (e) => "(print " + prettyPrintExp (e) + ")"
    case ExpPair (fst, snd) => "(" + prettyPrintExp (fst) + ", " + prettyPrintExp (snd) + ")"
    case ExpApp (op, arg) => "(" + prettyPrintExp (op) + " " + prettyPrintExp (arg) + ")"
    case ExpLet (nm, e, body) => "(let " + nm + " = " + prettyPrintExp (e) + " in " + prettyPrintExp (body) + ")"
    case ExpAbs (argNm, body) => "(" + argNm + " => " + prettyPrintExp (body) + ")"
    case ExpRecAbs (funNm, argNm, body) => "(" + funNm + " " + argNm + " => " + prettyPrintExp (body) + ")"
    case ExpFst (e) => "(fst " + prettyPrintExp (e) + ")"
    case ExpSnd (e) => "(snd " + prettyPrintExp (e) + ")"
    // TODO: Add case for ExpIf below.
    case ExpIf (b, tt, ff) => "(if " + prettyPrintExp (b) + " then " + prettyPrintExp (tt) + " else " + prettyPrintExp (ff) + ")"
  }


  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // BEGIN EXAMPLES                                                                                            //
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

  // A primitive function for adding two values.  It will throw an exception if
  // the values are not both integers.
  // 
  // NOTE: it is not necessary to add primSub for subtraction, because one can substitute uses of 
  // addition using, for example:
  //
  // i.    (x - 5) = (x + (-5))
  // ii.   (x - y) = (x + (-1 * y))
  val primAdd:Val = ValPrimFun (Some ("_+_"), { 
    case ValInt (n1) => 
      ValPrimFun (None, {
        case ValInt (n2) => ValInt (n1 + n2)
        case _  => throw new RuntimeException ("expected two integer arguments")
      })
    case _  => throw new RuntimeException ("expected two integer arguments")
  })

  // A primitive function for multiplying two values.  It will throw an exception if
  // the values are not both integers.
  val primMul:Val = ValPrimFun (Some ("_*_"), { 
    case ValInt (n1) => 
      ValPrimFun (None, {
        case ValInt (n2) => ValInt (n1 * n2)
        case _  => throw new RuntimeException ("expected two integer arguments")
      })
    case _  => throw new RuntimeException ("expected two integer arguments")
  })

  // A primitive function for testing equality of two values.  It will throw an exception if
  // the values are not both integers.
  val primEqInt = ValPrimFun (Some ("_==_"), { 
    case ValInt (n1) => 
      ValPrimFun (None, {
        case ValInt (n2) => ValInt (if (n1 == n2) 1 else 0)
        case _  => throw new RuntimeException ("expected two integer arguments")
      })
    case _  => throw new RuntimeException ("expected two integer arguments")
  })

  // Sample program: (let x = 5 + 6; x)
  val prog01:Exp = 
    ExpLet (
      "x", 
      ExpApp (ExpApp (ExpVal (primAdd), ExpVal (ValInt (5))), ExpVal (ValInt (6))),
      ExpVar ("x") 
    )

  // Sample program: (let x = 5 + 6; y = 20; x * y)
  val prog02:Exp = 
    ExpLet (
      "x", 
      ExpApp (ExpApp (ExpVal (primAdd), ExpVal (ValInt (5))), ExpVal (ValInt (6))),
      ExpLet (
        "y",
        ExpVal (ValInt (20)),
        ExpApp (ExpApp (ExpVal (primMul), ExpVar ("x")), ExpVar ("y"))
      )
    )

  // Sample program: ((let x = 5 in (y => x + y)) 6)
  val prog03:Exp = 
    ExpApp (
      ExpLet (
        "x",
        ExpVal (ValInt (5)),
        ExpAbs (
          "y",
          ExpApp (ExpApp (ExpVal (primAdd), ExpVar ("x")), ExpVar ("y"))
        )
      ),
      ExpVal (ValInt (6))
    )

  // Sample program corresponding to the Scala program:
  //
  //   val fact:Int=>Int = { 
  //     def fact(n:Int):Int = if (n != 0) n * fact(n + -1) else 1
  //     fact
  //   }
  //   fact (5)
  //
  // Note that this program has the same effect as:
  //
  //   def fact(n:Int):Int = if (n != 0) n * fact(n + -1) else 1
  //   fact (5)
  //
  // But there is a slight mismatch between Scala's form of recursive function definitions and the 
  // language used in this interpreter.  The one in this interpreter is simpler (and chosen for that reason),
  // whereas Scala's form is easier to use in practice, but slightly more complex to specify.
  //
  // NOTE: this example is commented out.  You can uncomment it once you have added ExpIf in one of the exercises below.
  // TODO: Change the body of the definition below.
   
  val recTest:Exp = 
    ExpLet (
      "fact",
      ExpRecAbs (
        "fact", 
        "n", 
        ExpIf (
          ExpVar ("n"), 
          ExpApp (
            ExpApp (
              ExpVal (primMul), 
              ExpVar ("n")
            ), 
            ExpApp (
              ExpVar ("fact"), 
              ExpApp (
                ExpApp (
                  ExpVal (primAdd), 
                  ExpVar ("n")
                ), 
                ExpVal (ValInt (-1))
              )
            )
          ), 
          ExpVal (ValInt (1))
        )
      ),
      ExpApp (ExpVar ("fact"), ExpVal (ValInt (5)))
    )
  // TODO: Change the body of the definition below.
  

  // def counter (n) = m => (n, counter (n + 1)); 
  val counterFunction:Exp = 
    ExpRecAbs (
      "counterFunction",
      "n",
      ExpAbs (
        "m",
        ExpPair (
          ExpVar ("n"),
          ExpApp (
            ExpVar ("counterFunction"), 
            ExpApp (
              ExpApp (
                ExpVal (primAdd), 
                ExpVar ("n")
              ), 
              ExpVal (ValInt (1))
            )
          )
        )
      )
    )

  val counter:Exp = ExpApp (counterFunction, ExpVal (ValInt (4)))

  val printTest:Exp = 
    ExpPrint (ExpVal (ValInt (5)))

  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // END EXAMPLES                                                                                              //
  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

  val todoVal:Val = ValNil
  val todoExp:Exp = ExpVal (todoVal)
  val todoEnv:Env = emptyEnv

  // EXERCISE 1: write abstract syntax for the integer 50.  Your abstract syntax should have type "Val".
  // TODO: Change the body of the definition below.
  val exFiftyVal:Val = ValInt(50)

  // EXERCISE 2: write abstract syntax for the integer 50.  Your abstract syntax should have type "Exp".
  // TODO: Change the body of the definition below.
  val exFiftyExp:Exp = ExpVal(ValInt(50))

  // EXERCISE 3: write an environment that maps variable "x" to the integer value 50.  
  // TODO: Change the body of the definition below.
  val exX:Env = emptyEnv + ( ("x", exFiftyVal) ) 

  // EXERCISE 4: write abstract syntax corresponding to (x + 50).  
  // HINT: use the primitive function "primAdd" (search for it in this file).
  // HINT: see the sample abstract syntax trees for uses of "primAdd".
  // TODO: Change the body of the definition below.
  val exXPlusFifty:Exp = ExpApp (ExpApp (ExpVal (primAdd), ExpVar ("x")), ExpVal (ValInt (50)))

  // HINT: you can test your code for "exX" and "xPlusFifty" by adding a call to the following 
  // function to the "main" method below (this has already been done for you).
  def testXPlusFifty () {
    println ("EVALUATING: " + prettyPrintExp (exXPlusFifty))
    val v = evaluate (exX, exXPlusFifty)
    println ("RESULT: " + prettyPrintVal (v))
  }

  // EXERCISE 5: write abstract syntax corresponding to ( (x:Int) => x + 50 ), i.e., an anonymous function that 
  // takes an argument "x" and returns (x+50).  
  // NOTE: the programming language for which you are writing abstract syntax is dynamically-typed and so
  // the Scala type annotation ":Int" cannot be represented in the abstract syntax (so do not try to represent it!).
  // TODO: Change the body of the definition below.
  val exXFunPlusFifty:Exp = 
    ExpAbs (
      "x",
      exXPlusFifty
    )        
  
  // EXERCISE 6: write abstract syntax corresponding to (( (x:Int) => x + 50 ) (30) ), i.e., applying an anonymous function (that 
  // takes an argument "x" and returns (x+50)) to the argument (30).  You can reference "exXFunPlusFifty" in your answer
  // to eliminate the need to copy that abstract syntax tree.
  // TODO: Change the body of the definition below.
  val exXFunPlusFiftyApplied:Exp = 
    ExpApp (
      exXFunPlusFifty,
      ExpVal (ValInt (30))
    )
  
  // EXERCISE 7: write abstract syntax corresponding to:
  //   let x = 5; 
  //   let y = 6; 
  //   x + y
  // (in Scala this would be: (val x = 5; val y = 6; x + y))
  // TODO: Change the body of the definition below.
  val exLetXLetY:Exp = 
    ExpLet (
      "x", 
      ExpVal (ValInt (5)),     
      ExpLet (
        "y",
        ExpVal (ValInt (6)),
        ExpApp (ExpApp (ExpVal (primAdd), ExpVar ("x")), ExpVar ("y"))
      )
    )
  
  // EXERCISE 8: write abstract syntax corresponding to:
  //   let f = ( (x:Int) => x + 50 ); 
  //   f (5)
  // TODO: Change the body of the definition below.
  val exLetF:Exp = 
    /*ExpLet (
      "x",
      ExpVal (ValInt (5)),
      ExpApp (exXFunPlusFifty, ExpVar ("x"))
    )*/
    ExpLet (
      "f", 
      exXFunPlusFifty,
      ExpApp (ExpVar("f"), ExpVal (ValInt (5)))
    )    

  // EXERCISE 9: extend the abstract syntax with a conditional expression.  You should uncomment the 
  // line "case class ExpIf (b:Exp, tt:Exp, ff:Exp) extends Exp" and then add support for ExpIf to 
  // the "evaluate" and "prettyPrintExp" functions.  Once your support is complete, 
  // you can uncomment the "recTest" example and run it using "testExample (recTest)".

  // EXERCISE 10: write abstract syntax corresponding to:
  //   (5, (x) => 6)
  // That is, a pair consisting of an integer and an anonymous function (that takes an unused argument x, and returns 6 when run).
  // TODO: Change the body of the definition below.
  val exPair:Exp = 
    ExpPair (
      ExpVal (ValInt (5)),
      ExpAbs (
        "x",
        ExpVal (ValInt (6))
      )
    )

  // EXERCISE 11: look for the definition of "counter:Exp" above.  The abstract syntax represents the
  // application of a function "counterFunction" to the integer 4.  When applied to a number n, "counterFunction" 
  // returns an anonymous function that takes an unused argument x, and returns a pair when it is invoked.
  // The first element of the pair is the given number n.  The second element of the pair is the result of
  // running "counterFunction" applied to (n+1).
  //
  // The definition of "counterTest:Exp" below shows how to extract the integer from the first component of the pair.
  // It evaluates to the value 4 when run.
  val counterTest:Exp = ExpLet ("counter", ExpApp (counter, ExpVal (ValNil)), ExpFst (ExpVar ("counter")))
  // 
  // Write abstract syntax that extracts the integer from the first element of the second element of the pair returned from "counter:Exp".
  // It should evaluate to the value 5 when run.
  // You MUST use "counter:Exp".  You MUST NOT write some other expression that evaluates to 5, e.g., (5) or (4+1), etc.
  // TODO: Change the body of the definition below.
  val exCounter:Exp = 
    /*ExpLet (
      "x",
      ExpLet ("counter", ExpApp (counter, ExpVal (ValNil)), ExpSnd (ExpVar ("counter"))),
      //ExpApp (ExpFst(ExpVar("x")), ExpVal (ValNil))
      ExpFst(ExpVar("x"))
    )
    ExpFst (
      ExpLet (
        "f", 
        counter,
        ExpSnd (ExpVar ("f"))
      )
    )*/
    ExpFst (ExpApp (ExpSnd(ExpApp (counter, ExpVal (ValNil))), ExpVal (ValNil)))

  // EXERCISE 12: explain in words why the functions used in exercise 11 require an implementation to use closures (or something similar).
  // Your answer should be specific to the example, i.e., you should explain what would happen when running this code
  // if the naive (incorrect) implementation of functions was used.
  // 
  // ANSWER: PUT YOUR ANSWER TO EXERCISE 12 HERE.

  def testExample (e:Exp) = {
    try {
      println ("EVALUATING: " + prettyPrintExp (e))
      val v = evaluate (emptyEnv, e)
      println ("RESULT: " + prettyPrintVal (v))
      println ("+=" * 40)
    } catch {
      case e:Exception => 
        println ("ERROR: ")
        e.printStackTrace
    }
  }

  def main (args:Array[String]) = {
    // Test the sample programs.
    testExample (prog01)
    testExample (prog02)
    testExample (prog03)
    testExample (printTest)
    testExample (counterTest)

    println ("EXERCISE 2 TEST")
    testExample (exFiftyExp)

    println ("EXERCISE 4 TEST")
    testXPlusFifty ()
    println ("+=" * 40)
  
    println ("EXERCISE 5 TEST")
    testExample (exXFunPlusFifty)
    
    println ("EXERCISE 6 TEST")
    testExample (exXFunPlusFiftyApplied)
    
    println ("EXERCISE 7 TEST")
    testExample (exLetXLetY)
    
    println ("EXERCISE 8 TEST")
    testExample (exLetF)
  
    println ("EXERCISE 9 TEST")
    // TODO: UNCOMMENT THIS LINE ONCE ExpIf IS FINISHED
    testExample (recTest) 
  
    println ("EXERCISE 10 TEST")
    testExample (exPair)
  
    println ("EXERCISE 11 TEST")
    testExample (exCounter)
   }

}
  
