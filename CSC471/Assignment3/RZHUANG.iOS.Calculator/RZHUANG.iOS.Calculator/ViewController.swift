//
//  ViewController.swift
//  RZHUANG.iOS.Calculator
//
//  Created by Johnny on 4/14/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    //Distinguish the status
    enum CalculatorStatus {
        case NumberRequired   //Accepting the numbers
        case AddClicked       //The '+' button is clicked
        case EqualClicked     //The '=' button is clicked, only '+' button is allowed for the next operation.
    }
    
    var number1: Int?
    var number2: Int?
    var status: CalculatorStatus = CalculatorStatus.NumberRequired

    @IBOutlet weak var lblResult: UILabel!
    
    //Reset all of the states
    @IBAction func btnReset(sender: UIButton) {
        lblResult.text=""
        number1 = nil
        number2 = nil
        status = CalculatorStatus.NumberRequired
    }
    
    //Number panel
    @IBAction func btnNumber(sender: UIButton) {
        if let btnTitle = sender.currentTitle {
            if let lblText = lblResult.text {
                if status == CalculatorStatus.NumberRequired {
                    //Append the new digit to the end
                    lblResult.text = lblText + "\(btnTitle)"
                }
                else if status == CalculatorStatus.AddClicked {
                    //Save the current digits as number 1 and prepare the second one
                    number1 = lblText.toInt()
                    lblResult.text = "\(btnTitle)"
                    status = CalculatorStatus.NumberRequired
                }
            }
            
        }
    }
    
    //Change the status, prepare for the second number
    @IBAction func btnAdd(sender: UIButton) {
        status = CalculatorStatus.AddClicked
    }
    
    //Take the second number and calculate the sum
    @IBAction func btnEquals(sender: UIButton) {
        if (number1 != nil && status == CalculatorStatus.NumberRequired) {
            if let lblText = lblResult.text {
                number2 = lblText.toInt()
                if (number2 != nil) {
                    var sum:Int = number1! + number2!
                    lblResult.text = "\(sum)"
                    status = CalculatorStatus.EqualClicked
                }
            }
        }
    }
   
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

