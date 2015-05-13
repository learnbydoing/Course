/* Playground - Rank Function
// By Rong Zhuang
// Apr 11, 2015
*/

import UIKit

//sorted integer array
var nums:[Int] = [1,2,3,4,5,6,7,8,9,11,13,99]

//rank function to find the key from given list
func Rank(sortedNums: [Int], key: Int, low:Int, high: Int) -> Int{
    if (high<low) {
        return -1; //if no found
    }
    
    var mid:Int = low+(high-low)/2
    if (key<sortedNums[mid]) {
        return Rank(sortedNums,key,low, mid-1)
    }
    else if (key>sortedNums[mid]) {
        return Rank(sortedNums,key,mid+1, high)
    }
    else {
        return mid  //key found
    }
}

//simplified test method with only one input argument
func testRank(key:Int) ->Int{
    return Rank(nums, key, 0, nums.count-1)
}

var key: Int
var result: Int

//test case1: key=-1
key = -1
result = testRank(key)
if (result == -1) {
    println("Correct! Key:\(key) is not in the array!")
}
else{
    println("Wrong! Key:\(key) should not be found!")
}

//test case2: key=1
key = 1
result = testRank(key)
if (result == -1) {
    println("Wrong! You failed to find key:\(key) !")
}
else{
    println("Correct! Key:\(key) is found in position: \(result) !")
}

//test case3: key=7
key = 7
result = testRank(key)
if (result == -1) {
    println("Wrong! You failed to find key:\(key) !")
}
else{
    println("Correct! Key:\(key) is found in position: \(result) !")
}

//test case4: key=99
key = 99
result = testRank(key)
if (result == -1) {
    println("Wrong! You failed to find key:\(key) !")
}
else{
    println("Correct! Key:\(key) is found in position: \(result) !")
}

//test case5: key=101
key = 101
result = testRank(key)
if (result == -1) {
    println("Correct! Key:\(key) is not in the array!")
}
else{
    println("Wrong! Key:\(key) should not be found!")
}
