import org.scalatest._
import org.scalacheck.Gen


class interpretertests extends UnitSpec {
  val EX : Map[Int, Tag] = 
    (for (i <- (1 to 12).toList) yield {
      object T extends Tag ("interpreterex%02d".format (i))
      (i, T)
    }).toMap

  import interpreter._

  property ("EX01 - exFiftyVal", EX (1)) {
    assert (exFiftyVal.asInstanceOf[ValInt].n === 50)
  }

  property ("EX02 - exFiftyExp", EX (2)) { 
    assert (exFiftyExp.asInstanceOf[ExpVal].v.asInstanceOf[ValInt].n === 50)
  }

  property ("EX03 - exX", EX (3)) { 
    assert ((exX ("x")).asInstanceOf[ValInt].n === 50)
    assert (exX.keySet === List ("x").toSet)
  }

  property ("EX04 - exXPlusFifty", EX (4)) { 
    assert (exXPlusFifty.asInstanceOf[ExpApp].op.asInstanceOf[ExpApp].arg.asInstanceOf[ExpVar].nm === "x")
    assert (exXPlusFifty.asInstanceOf[ExpApp].arg.asInstanceOf[ExpVal].v.asInstanceOf[ValInt].n === 50)
    assert (evaluate (exX, exXPlusFifty) === ValInt (100))
    assert (evaluate (Map ("x" -> ValInt (10)), exXPlusFifty) === ValInt (60))
    assert (prettyPrintExp (exXPlusFifty) === "(([fun: _+_] x) 50)")
  }

  property ("EX05 - exXFunPlusFifty", EX (5)) { 
    assert (exXFunPlusFifty.asInstanceOf[ExpAbs].argNm === "x")
    assert (evaluate (exX, exXFunPlusFifty).asInstanceOf[ValClosure].env === exX)
    assert (prettyPrintExp (exXFunPlusFifty) === "(x => (([fun: _+_] x) 50))")
  }

  property ("EX06 - exXFunPlusFiftyApplied", EX (6)) { 
    assert (exXFunPlusFiftyApplied.asInstanceOf[ExpApp].arg.asInstanceOf[ExpVal].v.asInstanceOf[ValInt].n === 30)
    assert (evaluate (exX, exXFunPlusFiftyApplied).asInstanceOf[ValInt].n === 80)
    assert (prettyPrintExp (exXFunPlusFiftyApplied) === "((x => (([fun: _+_] x) 50)) 30)")
  }

  property ("EX07 - exLetXLetY", EX (7)) { 
    assert (exLetXLetY.asInstanceOf[ExpLet].nm === "x")
    assert (exLetXLetY.asInstanceOf[ExpLet].e.asInstanceOf[ExpVal].v.asInstanceOf[ValInt].n === 5)
    assert (exLetXLetY.asInstanceOf[ExpLet].body.asInstanceOf[ExpLet].nm === "y")
    assert (exLetXLetY.asInstanceOf[ExpLet].body.asInstanceOf[ExpLet].e.asInstanceOf[ExpVal].v.asInstanceOf[ValInt].n === 6)
    assert (evaluate (emptyEnv, exLetXLetY).asInstanceOf[ValInt].n === 11)
    assert (prettyPrintExp (exLetXLetY) === "(let x = 5 in (let y = 6 in (([fun: _+_] x) y)))")
  }

  property ("EX08 - exLetF", EX (8)) { 
    assert (exLetF.asInstanceOf[ExpLet].nm == "f")
    assert (exLetF.asInstanceOf[ExpLet].e.asInstanceOf[ExpAbs].argNm == "x")
    assert (exLetF.asInstanceOf[ExpLet].body.asInstanceOf[ExpApp].op.asInstanceOf[ExpVar].nm == "f")
    assert (exLetF.asInstanceOf[ExpLet].body.asInstanceOf[ExpApp].arg.asInstanceOf[ExpVal].v.asInstanceOf[ValInt].n == 5)
    assert (evaluate (emptyEnv, exLetF).asInstanceOf[ValInt].n == 55)
    assert (prettyPrintExp (exLetF) === "(let f = (x => (([fun: _+_] x) 50)) in (f 5))")
  }

  // EX09: Cannot test this one, because it requires ExpIf to be defined in order to compile.

  property ("EX10 - exPair", EX (10)) { 
    assert (exPair.asInstanceOf[ExpPair].fst.asInstanceOf[ExpVal].v.asInstanceOf[ValInt].n === 5)
    assert (exPair.asInstanceOf[ExpPair].snd.asInstanceOf[ExpAbs].argNm === "x")
    assert (exPair.asInstanceOf[ExpPair].snd.asInstanceOf[ExpAbs].body.asInstanceOf[ExpVal].v.asInstanceOf[ValInt].n === 6)
    assert (evaluate (emptyEnv, exPair).asInstanceOf[ValPair].fst.asInstanceOf[ValInt].n === 5)
    assert (evaluate (emptyEnv, exPair).asInstanceOf[ValPair].snd.asInstanceOf[ValClosure].env === emptyEnv)
    assert (evaluate (emptyEnv, exPair).asInstanceOf[ValPair].snd.asInstanceOf[ValClosure].argNm === "x")
    assert (evaluate (emptyEnv, exPair).asInstanceOf[ValPair].snd.asInstanceOf[ValClosure].body.asInstanceOf[ExpVal].v.asInstanceOf[ValInt].n === 6)
    assert (prettyPrintExp (exPair) === "(5, (x => 6))")
  }

  property ("EX11 - exCounter", EX (11)) { 
    assert (exCounter.asInstanceOf[ExpFst].e.asInstanceOf[ExpApp].arg.asInstanceOf[ExpVal].v == ValNil)
    assert (prettyPrintExp (exCounter) === "(fst ((snd (((counterFunction n => (m => (n, (counterFunction (([fun: _+_] n) 1))))) 4) Nil)) Nil))")
  }

  // EX12: Cannot test this one, because it requires a written answer.
}
