import org.scalatest._
import org.scalacheck.Gen


class fp1tests extends UnitSpec {
  val EX : Map[Int, Tag] = 
    (for (i <- (1 to 10).toList) yield {
      object T extends Tag ("fp1ex%02d".format (i))
      (i, T)
    }).toMap

  import fp1._

  property ("EX01 - factTest", EX (1)) {
    assert (factTest === List (1, 2, 6, 24, 120))
  }

  property ("EX02 - fib", EX (2)) { 
    val fibTable = Table (
      ("n", "fib (n)"),
      ( 0,  0),
      ( 1,  1),
      ( 2,  1),
      ( 3,  2),
      ( 4,  3),
      ( 5,  5),
      ( 6,  8),
      ( 7, 13),
      ( 8, 21),
      ( 9, 34),
      (10, 55),
      (11, 89)
    )
    forAll (fibTable) { (n : Int, res : Int) =>
      assert (fib (n) ===  res)
    }
  }

  property ("EX03 - p1", EX (3)) { 
    p1 should not equal (null)
    assert (p1._1 === 7)
    assert (p1._2 === "hello")
  }

  property ("EX04 - t1", EX (4)) { 
    t1 should not equal (null)
    assert (t1._1 === 7)
    assert (t1._2 === "hello")
    assert (t1._3 === false)
  }

  property ("EX05 - swap", EX (5)) { 
    swap ( (5, "hello") ) should not equal (null)
    assert (swap ( (5, "hello") ) === ("hello", 5) ) 
    assert (swap ( (9, "world") ) === ("world", 9) )
  }

  val sumTable = Table (
    ("xs", "sum (xs)"),
    (Nil,  0),
    (List (15),  15),
    ((0 to 100).toList,  5050)
  )

  property ("EX06 - sum", EX (6)) { 
    forAll (sumTable) { (xs : List[Int], res : Int) =>
      assert (sum (xs) ===  res)
    }
  }

  property ("EX07 - sumTailRecursive", EX (7)) { 
    forAll (sumTable) { (xs : List[Int], res : Int) =>
      assert (sumTailRecursive (xs) ===  res)
    }
  }

  val maxTable = Table (
    ("xs", "max (xs)"),
    (List (15),  15),
    ((0 to 100).toList,  100),
    ((0 to 100).toList.reverse,  100),
    ((0 to 50).toList ::: ((30 to 70).toList.reverse),  70)
  )

  property ("EX08 - max", EX (8)) { 
    forAll (maxTable) { (xs : List[Int], res : Int) =>
      assert (max (xs) ===  res)
    }
  }

  property ("EX09 - maxTailRecursiveAux", EX (9)) { 
    forAll (maxTable) { (xs : List[Int], res : Int) =>
      assert (maxTailRecursive (xs) ===  res)
    }
  }

  property ("EX10 - otpu", EX (10)) { 
    val otpuTable = Table (
      ("start", "end", "otpu (start, end)"),
      (20, 10,  (10 to 20).toList.reverse), 
      (15, 10,  (10 to 15).toList.reverse), 
      (15,  7,  ( 7 to 15).toList.reverse)
    )
    forAll (otpuTable) { (start : Int, end : Int, res : List[Int]) =>
      assert (otpu (start, end) ===  res)
    }
  }

}
