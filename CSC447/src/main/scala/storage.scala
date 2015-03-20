// GENERATED

/* INSTRUCTIONS
 *
 * Complete the exercises below.  For each "EXERCISE" comment, add
 * code immediately below the comment.
 *
 * Please see README.md for instructions, including compilation and testing.
 * 
 * GRADING
 * 
 * 1. Submissions MUST compile using SBT with UNCHANGED configuration and tests with no
 *    compilation errors.  Submissions with compilation errors will receive 0 points.
 *    Note that refactoring the code will cause the tests to fail.
 *
 * 2. You MUST NOT edit the SBT configuration and tests.  Altering it in your submission will
 *    result in 0 points for this assignment.
 *
 * 3. You MAY use (re)assignment to variables and "val" and "var" declarations (you will have to).
 *
 * 4. In all of the exercises in this file, you can only add code between the
 *    existing "{...}" for the val/def.  That is, you cannot add a new val/def directly
 *    inside the "storage" object.
 *
 * SUBMISSION
 *
 * 1. Push your local repository to the repository created for you on Bitbucket before the deadline.
 *
 * 2. Late submissions will not be permitted because solutions will be discussed in class.
 * 
 */


object storage {

  // EXERCISE 1: Complete the following definition, so that "constant5" is a function that returns 5
  // whenever it is invoked.
  val constant5 : () => Int = {
    // TODO: Complete the definition.
    () => 5
  }

  // EXERCISE 2: Complete the following definition, so that "constant" is a function that when
  // invoked with integer n returns a function that returns n whenever it is invoked.
  val constant : Int => () => Int = {
    // TODO: Complete the definition.
    (n: Int) => () => n
  }

  // EXERCISE 3: Complete the following definition, so that "counter0" is a (stateful) function that
  // returns 0 when it is first invoked, then 1, then 2, etc.  
  // REMEMBER: you can use "var" but everything you add has to be inside the "{...}" body of "counter0".
  // This rule applies throughout this assignment.
  val counter0 : () => Int = {
    // TODO: Complete the definition.
    var n:Int = 0
    () => { val retVal = n; n = n + 1; retVal }
  }

  // EXERCISE 4: Complete the following definition, so that "counter" is a (stateless) function that
  // when invoked with integer n returns a (stateful) function that returns n when it is first
  // invoked, then n+1, then n+2, etc.  The counters must be independent, i.e., running "counter (0)" 
  // twice should yield two functions that do not interfere with one another's state.
  val counter : Int => () => Int = {
    // TODO: Complete the definition. 
    def createCounter (initialValue:Int) : () => Int = {
      var n:Int = initialValue
      () => { val retVal = n; n = n + 1; retVal }
    }  
    (n: Int) => createCounter(n)    
  }

  // EXERCISE 5: Complete the following definition, so that "getAndSet" is a (stateless) function
  // that when invoked with integer n returns a pair of functions (that share state) that allow
  // reading and writing a var that is initialized with integer n.  The first function in the pair
  // should be the reader.  The second function in the pair should be the writer.  For example, the
  // following expression should return 5: { val (get, set) = getAndSet (5); set (10); get () } 
  // Multiple calls to "getAndSet" should yield independent pairs, i.e., the first pair returned 
  // should not share any state with the second pair returned.
  val getAndSet : Int => (() => Int, Int => Unit) = {
    // TODO: Complete the definition.    
    def createGetAndSet (initialValue:Int) : (() => Int, Int => Unit) = {
      var statevalue:Int = initialValue
      val createGet: () => Int = {
          () => statevalue
        }
      val createSet: Int => Unit = {
          (n: Int) => { statevalue = n; ()}      
        }
      (createGet, createSet)
    }
    (n: Int) => createGetAndSet(n)
    /*
    var statevalue:Int = 0
    def createGet () : () => Int = {
      () => statevalue
    }
    def createSet (n:Int) : Int => Unit = {
      (n: Int) => { statevalue = n; ()}      
    }

    (n: Int) => (createGet(), createSet(n))
    */
  }

  // EXERCISE 6: Complete the following definition, so that "getAndSetSpy" is a (stateful) function
  // that when invoked it returns a pair.  The second component of the pair should behave like
  // "getAndSet" above (with the exception noted next).  The first component of the pair is a
  // function that, when invoked, returns the total number of times that a "set" call has been made.
  // That number should cover all calls to "set" made in all pairs created via "getAndSetSpy".  That is, 
  // the total number is a piece of state shared all "set" functions created via "getAndSetSpy".
  val getAndSetSpy : () => (() => Int, Int => (() => Int, Int => Unit)) = {
    // TODO: Complete the definition.
    def createGetAndSetSpy () : (() => Int, Int => (() => Int, Int => Unit)) = {
      var setvalue:Int = 0
      def createGetAndSet (initialValue:Int) : (() => Int, Int => Unit) = {
        var statevalue:Int = initialValue
        val createGet: () => Int = {
            () => statevalue
          }
        val createSet: Int => Unit = {
            (n: Int) => { statevalue = n; setvalue = setvalue + 1; ()}      
          }
        (createGet, createSet)
      }

      val createSpy: () => Int = {
          () => setvalue
        }		
   
      val createGetAndSetIns : Int => (() => Int, Int => Unit) = {
          (n: Int) => createGetAndSet(n)
        }
      (createSpy, createGetAndSetIns)
    }
    () => createGetAndSetSpy()
  }
}

