import org.scalatest._
import org.scalacheck.Gen


class storagetests extends UnitSpec {
  val EX : Map[Int, Tag] = 
    (for (i <- (1 to 6).toList) yield {
      object T extends Tag ("storageex%02d".format (i))
      (i, T)
    }).toMap

  import storage._

  property ("EX01 - constant5", EX (1)) {
    assert ({ 
      val r1 : Int = constant5 ()
      val r2 : Int = constant5 ()
      (r1, r2)
    } === (5, 5)
    )
  }

  property ("EX02 - constant", EX (2)) {
    assert ({ 
      val k1 : () => Int = constant (1)
      val k2 : () => Int = constant (2)
      val r1 : Int = k1 ()
      val r2 : Int = k1 ()
      val r3 : Int = k2 ()
      val r4 : Int = k2 ()
      val r5 : Int = k2 ()
      val r6 : Int = k1 ()
      (r1, r2, r3, r4, r5, r6)
    } === (1, 1, 2, 2, 2, 1)
    )
  }

  property ("EX03 - counter0", EX (3)) {
    assert ({ 
      val r1 : Int = counter0 ()
      val r2 : Int = counter0 ()
      val r3 : Int = counter0 ()
      (r1, r2, r3)
    } === (0, 1, 2)
    )
  }

  property ("EX04 - counter", EX (4)) {
    assert ({ 
      val k1 : () => Int = counter (1)
      val k2 : () => Int = counter (2)
      val r1 : Int = k1 ()
      val r2 : Int = k1 ()
      val r3 : Int = k2 ()
      val r4 : Int = k2 ()
      val r5 : Int = k2 ()
      val r6 : Int = k1 ()
      (r1, r2, r3, r4, r5, r6)
    } === (1, 2, 2, 3, 4, 3)
    )
  }

  property ("EX05 - getAndSet", EX (5)) {
    assert ({ 
      val (get1, set1) : (() => Int, Int => Unit) = getAndSet (1)
      val (get2, set2) : (() => Int, Int => Unit) = getAndSet (2)
      val r1 : Int = get1 ()
      val r2 : Int = get1 ()
      set1 (10)
      val r3 : Int = get1 ()
      val r4 : Int = get1 ()
      val r5 : Int = get2 ()
      val r6 : Int = get2 ()
      set2 (20)
      val r7 : Int = get2 ()
      val r8 : Int = get1 ()
      (r1, r2, r3, r4, r5, r6, r7, r8)
    } === (1, 1, 10, 10, 2, 2, 20, 10)
    )
  }

  property ("EX06 - getAndSetSpy", EX (6)) {
    assert ({ 
      val (spy, getAndSet1) : (() => Int, Int => (() => Int, Int => Unit)) = getAndSetSpy ()
      val (get1, set1) : (() => Int, Int => Unit) = getAndSet1 (1)
      val (get2, set2) : (() => Int, Int => Unit) = getAndSet1 (2)
      val r1 : Int = get1 ()
      val r2 : Int = get1 ()
      set1 (10)
      val r3 : Int = get1 ()
      val r4 : Int = get1 ()
      val r5 : Int = get2 ()
      val r6 : Int = get2 ()
      set2 (20)
      val r7 : Int = get2 ()
      val r8 : Int = get1 ()
      val r9 : Int = spy ()
      set1 (30)
      val r10 : Int = spy ()
      (r1, r2, r3, r4, r5, r6, r7, r8, r9, r10)
    } === (1, 1, 10, 10, 2, 2, 20, 10, 2, 3)
    )
  }

}
