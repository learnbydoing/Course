import org.scalatest._
import org.scalacheck.Gen


class fp2tests extends UnitSpec {
  val EX : Map[Int, Tag] = 
    (for (i <- (1 to 20).toList) yield {
      object T extends Tag ("fp2ex%02d".format (i))
      (i, T)
    }).toMap

  import fp2._

  property ("EX01 - map", EX (1)) {
    assert (map (Nil, (n:Int) => n + 1) === Nil)
    assert (map ((1 to 5).toList, (n:Int) => n + 1) === (2 to 6).toList)
  }

  property ("EX02 - filter", EX (2)) {
    assert (filter (Nil, (n:Int) => false) === Nil)
    assert (filter (Nil, (n:Int) => true) === Nil)
    assert (filter ((1 to 5).toList, (n:Int) => false) === Nil)
    assert (filter ((1 to 5).toList, (n:Int) => true) === (1 to 5).toList)
    assert (filter ((1 to 5).toList, (n:Int) => n % 2 === 1) === List (1,3,5))
  }

  property ("EX03 - append", EX (3)) {
    assert (append (            Nil,              Nil) === Nil)
    assert (append (            Nil, (6 to 10).toList) === (6 to 10).toList)
    assert (append ((1 to 5).toList,              Nil) === (1 to 5).toList)
    assert (append ((1 to 5).toList, (6 to 10).toList) === (1 to 10).toList)
  }

  property ("EX04 - flatten", EX (4)) {
    assert (flatten (Nil) === Nil)
    assert (flatten (List ((1 to 5).toList)) === (1 to 5).toList)
    assert (flatten (List ((1 to 5).toList, (6 to 10).toList)) === (1 to 10).toList)
    assert (flatten (List ((1 to 5).toList, (6 to 10).toList, (11 to 15).toList)) === (1 to 15).toList)
  }

  property ("EX05 - foldLeft", EX (5)) {
    def f (s : String, n : Int) : String = s + "[" + n + "]"
    assert (foldLeft (Nil, "@", f) === "@")
    assert (foldLeft ((1 to 3).toList, "@", f) === "@[1][2][3]")
  }

  property ("EX06 - foldRight", EX (6)) {
    def f (n : Int, s : String) : String = s + "[" + n + "]"
    assert (foldRight (Nil, "@", f) === "@")
    assert (foldRight ((1 to 3).toList, "@", f) === "@[3][2][1]")
  }

  property ("EX07 - joinTerminateLeft", EX (7)) {
    assert (joinTerminateLeft (Nil, ";") === "")
    assert (joinTerminateLeft (List ("a"), ";") === "a;")
    assert (joinTerminateLeft (List ("a","b","c","d"), ";") === "a;b;c;d;")
  }

  property ("EX08 - joinTerminateRight", EX (8)) {
    assert (joinTerminateRight (Nil, ";") === "")
    assert (joinTerminateRight (List ("a"), ";") === "a;")
    assert (joinTerminateRight (List ("a","b","c","d"), ";") === "a;b;c;d;")
  }

  property ("EX09 - firstNumGreaterThan", EX (9)) {
    assert (firstNumGreaterThan (5, List (4, 6, 8, 5)) === 6)
    assert (firstNumGreaterThan (7, List (4, 6, 8, 5)) === 8)
    assert (firstNumGreaterThan (4, List (4, 6, 8, 5)) === 4)
  }

  property ("EX10 - firstIndexNumGreaterThan", EX (10)) {
    assert (firstIndexNumGreaterThan (5, List (4, 6, 8, 5)) === 1)
    assert (firstIndexNumGreaterThan (7, List (4, 6, 8, 5)) === 2)
    assert (firstIndexNumGreaterThan (4, List (4, 6, 8, 5)) === 0)
  }

  property ("EX11 - member", EX (11)) {
    assert (member (5, List (4, 6, 8, 5)) === true)
    assert (member (3, List (4, 6, 8, 5)) === false)
  }

  property ("EX12 - allEqual", EX (12)) {
    assert (allEqual (Nil) === true)
    assert (allEqual (List (5)) === true)
    assert (allEqual (List (5, 5, 5)) === true)
    assert (allEqual (List (6, 5, 5, 5)) === false)
    assert (allEqual (List (5, 5, 6, 5)) === false)
    assert (allEqual (List (5, 5, 5, 6)) === false)
  }

  val test1 = List ("The", "rain", "in", "Spain", "falls", "mainly", "on", "the", "plain.")
  val test2 = List (1,1,2,3,3,3,4,4,5,6,7,7,8,9,2,2,2,9)

  property ("EX13 - stringLengths", EX (13)) {
    assert (stringLengths (Nil) === Nil)
    assert (stringLengths (test1) === test1.zip (List (3, 4, 2, 5, 5, 6, 2, 3, 6)))
  }

  property ("EX14 - delete1", EX (14)) {
    assert (delete1 (1, Nil) === Nil)
    assert (delete1 ("the", List ("the","the","was","a","product","of","the","1980s")) === List ("was","a","product","of","1980s"))
  }

  property ("EX15 - delete2", EX (15)) {
    assert (delete2 (1, Nil) === Nil)
    assert (delete2 ("the", List ("the","the","was","a","product","of","the","1980s")) === List ("was","a","product","of","1980s"))
  }

  property ("EX16 - delete3", EX (16)) {
    assert (delete3 (1, Nil) === Nil)
    assert (delete3 ("the", List ("the","the","was","a","product","of","the","1980s")) === List ("was","a","product","of","1980s"))
  }

  property ("EX17 - removeDupes1", EX (17)) {
    assert (removeDupes1 (Nil) === Nil)
    assert (removeDupes1 (test2) === List (1,2,3,4,5,6,7,8,9,2,9))
  }

  property ("EX18 - removeDupes2", EX (18)) {
    assert (removeDupes2 (Nil) === Nil)
    assert (removeDupes2 (test2) === List ((2,1),(1,2),(3,3),(2,4),(1,5),(1,6),(2,7),(1,8),(1,9),(3,2),(1,9)))
  }

  property ("EX19 - splitAt", EX (19)) {
    assert (splitAt (7, (1 to 15).toList) === ((1 to 7).toList, (8 to 15).toList))
    assert (splitAt (1, (1 to 15).toList) === ((1 to 1).toList, (2 to 15).toList))
    assert (splitAt (0, (1 to 15).toList) === (List (), (1 to 15).toList))
    assert (splitAt (-1, (1 to 15).toList) === (List (), (1 to 15).toList))
    assert (splitAt (15, (1 to 15).toList) === ((1 to 15).toList, List ()))
    assert (splitAt (16, (1 to 15).toList) === ((1 to 15).toList, List ()))
  }

  property ("EX20 - allDistinct", EX (20)) {
    assert (allDistinct (Nil) === true)
    assert (allDistinct (List (1,2,3,4,5)) === true)
    assert (allDistinct (List (1,2,3,4,5,1)) === false)
    assert (allDistinct (List (1,2,3,2,4,5)) === false)
  }
}
