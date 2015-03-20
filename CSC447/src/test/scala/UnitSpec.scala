import org.scalatest.{Matchers,PropSpec}
import Matchers._
import org.scalatest.prop.{PropertyChecks,TableDrivenPropertyChecks}

abstract class UnitSpec
         extends PropSpec
         with Matchers
         with PropertyChecks
         // with TableDriven
         // with GeneratorDrivenPropertyChecks

// with Matchers
// with ShouldMatchers
// with OptionValues
// with Inside
// with Inspectors
