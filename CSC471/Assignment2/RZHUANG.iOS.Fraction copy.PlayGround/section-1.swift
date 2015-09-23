/* Playground - Fraction Class
// By Rong Zhuang
// Apr 11, 2015
*/

import UIKit

//Fraction
class Fraction {
    var numerator: Int = 0
    var denominator: Int = 1
    
    init(_ numerator:Int, over denominator:Int) {
        setTo(numerator, over: denominator)
    }
    
    init() {}
    
    private func normalize() {
        if (denominator < 0) {
            denominator = -denominator
            numerator = -numerator
        }
    }
    
    func setTo(numerator:Int, over denominator:Int) {
        self.numerator = numerator
        self.denominator = denominator
        normalize()
    }
    
    func print() {
        println("\(numerator)/\(denominator)")
    }
    
    func reduce() {
        let sign = numerator >= 0 ? 1 : -1
        var u = numerator * sign
        var v = denominator
        var r: Int
        while (v != 0) {
            r = u % v; u = v; v = r
        }
        //numerator /= u * sign; //no need to use the sign
        numerator /= u;
        denominator /= u;
    }
    
    func add(f: Fraction) -> Fraction {
        var result: Fraction = Fraction()
        result.numerator = numerator * f.denominator + denominator * f.numerator;
        result.denominator = denominator * f.denominator;
        result.reduce()
        return result
    }
    
    func subtract(f: Fraction) -> Fraction {
        var result: Fraction = Fraction()
        result.numerator = numerator * f.denominator - denominator * f.numerator;
        result.denominator = denominator * f.denominator;
        result.reduce()
        return result
    }
    
    func multiply(f: Fraction) -> Fraction {
        var result: Fraction = Fraction()
        result.numerator = numerator * f.numerator;
        result.denominator = denominator * f.denominator;
        result.reduce()
        result.normalize()
        return result
    }
    
    func divide(f: Fraction) -> Fraction {
        if (f.numerator==0) {
            NSException(name:"InvalidArgumentException", reason:"Second fracton's numerator can't be zero!", userInfo:nil).raise()
        }
        var result: Fraction = Fraction()
        result.numerator = numerator * f.denominator;
        result.denominator = denominator * f.numerator;
        result.reduce()
        result.normalize()
        return result
    }
}

//override methodsß
func +(a: Fraction, b: Fraction) -> Fraction {
    return a.add(b)
}
func -(a: Fraction, b: Fraction) -> Fraction {
    return a.subtract(b)
}
func *(a: Fraction, b: Fraction) -> Fraction {
    return a.multiply(b)
}
func /(a: Fraction, b: Fraction) -> Fraction {
    return a.divide(b)
}

func add(a: Fraction, b: Fraction) -> Fraction {
    return a.add(b)
}
func substract(a: Fraction, b: Fraction) -> Fraction {
    return a.subtract(b)
}
func multiply(a: Fraction, b: Fraction) -> Fraction {
    return a.multiply(b)
}
func divide(a: Fraction, b: Fraction) -> Fraction {
    return a.divide(b)
}

func += (inout left: Fraction, right: Fraction) {
    left = left + right
}
func -= (inout left: Fraction, right: Fraction) {
    left = left - right
}
func *= (inout left: Fraction, right: Fraction) {
    left = left * right
}
func /= (inout left: Fraction, right: Fraction) {
    left = left / right
}

//test 1/2 and 1/4
let f1 = Fraction(1, over: 2)
let f2 = Fraction(1, over: 4)

let f3 = f1+f2
println("\(f3.numerator)/\(f3.denominator)")

let f4 = f1-f2
println("\(f4.numerator)/\(f4.denominator)")

let f5 = f1*f2
println("\(f5.numerator)/\(f5.denominator)")

let f6 = f1/f2
println("\(f6.numerator)/\(f6.denominator)")

//test negative: 1/2 and -1/3
f2.setTo(1, over: -3)
let f7 = f1+f2
println("\(f7.numerator)/\(f7.denominator)")

let f8 = f1-f2
println("\(f8.numerator)/\(f8.denominator)")

let f9 = f1*f2
println("\(f9.numerator)/\(f9.denominator)")

let f10 = f1/f2
println("\(f10.numerator)/\(f10.denominator)")

//test 1/3 and 0/2
let f11 = Fraction(1, over: 3)
let f12 = Fraction(0, over: 2)

let f13 = f11+f12
println("\(f13.numerator)/\(f13.denominator)")

let f14 = f11-f12
println("\(f14.numerator)/\(f14.denominator)")

let f15 = f11*f12
println("\(f15.numerator)/\(f15.denominator)")

let f16 = f11/f12 //f12.numerator is 0, exception throws
println("\(f16.numerator)/\(f16.denominator)")

