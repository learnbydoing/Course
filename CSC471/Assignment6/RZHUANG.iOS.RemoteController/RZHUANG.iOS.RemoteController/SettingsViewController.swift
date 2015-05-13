//
//  ConfigurationViewController.swift
//  RZHUANG.iOS.RemoteController
//
//  Created by Johnny on 5/12/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class SettingsViewController: UIViewController {
    
    @IBOutlet weak var segmentChannel: UISegmentedControl!
    @IBOutlet weak var txtTitle: UITextField!
    @IBOutlet weak var lblNumber: UILabel!
    @IBOutlet weak var stepperChannel: UIStepper!
    
    @IBAction func channelIdChanged(sender: UISegmentedControl) {
        txtTitle.resignFirstResponder()
        refresh(sender.selectedSegmentIndex)
    }
    
    @IBAction func numberChanged(sender: UIStepper) {
        txtTitle.resignFirstResponder()
        lblNumber.text = Int(sender.value).description
    }
    
    @IBAction func editEnded(sender: UITextField) {
        sender.resignFirstResponder()
    }
    
    @IBAction func backgroundTouched(sender: UIControl) {
        txtTitle.resignFirstResponder()
    }
    
    @IBAction func saveSettings(sender: UIButton) {
        
        let channelTitle = txtTitle.text
        
        //check empty
        if channelTitle.isEmpty {
            let alertController = UIAlertController(title: "Error", message: "Channel title can't be empty!", preferredStyle: .Alert)
            let okAction = UIAlertAction(title: "OK", style: .Default, handler: nil)
            alertController.addAction(okAction)
            presentViewController(alertController, animated: true, completion: nil)
            return
        }
        
        //check length
        if count(channelTitle) > 4 {
            let alertController = UIAlertController(title: "Error", message: "The length of channel title can't be larger than 4!", preferredStyle: .Alert)
            let okAction = UIAlertAction(title: "OK", style: .Default, handler: nil)
            alertController.addAction(okAction)
            presentViewController(alertController, animated: true, completion: nil)
            return
        }
        
        //check whether the current channel number has already been used.
        for item in arrayChannels {
            if item.Id == arrayChannels[segmentChannel.selectedSegmentIndex].Id {
                continue
            }
            
            var a:Double = 1.5
            var b:String = String(format:"%f", a)
            println("b: \(b)") // b: 1.500000
            
            let channelNo = lblNumber.text!.toInt()
            let channelNoStr = String(format:"%02d", channelNo!)
            if item.Number == channelNo {
                let alertController = UIAlertController(title: "Error", message: "The number \(channelNoStr) has been used by Id: \(item.Id), choose another one!", preferredStyle: .Alert)
                let okAction = UIAlertAction(title: "OK", style: .Default, handler: nil)
                alertController.addAction(okAction)
                presentViewController(alertController, animated: true, completion: nil)
                return
            }
        }
        txtTitle.resignFirstResponder()
        
        var channel = arrayChannels[segmentChannel.selectedSegmentIndex]
        channel.Title = channelTitle
        channel.Number = lblNumber.text!.toInt()
        
        let saveAlertController = UIAlertController(title: "Success", message: "The favorite channel has been updated!", preferredStyle: .Alert)
        let okAction = UIAlertAction(title: "OK", style: .Default, handler: nil)
        saveAlertController.addAction(okAction)
        presentViewController(saveAlertController, animated: true, completion: nil)
        
    }
    
    @IBAction func cancelSettings(sender: UIButton) {
        txtTitle.resignFirstResponder()
        refresh(segmentChannel.selectedSegmentIndex)
    }
    
    func refresh(index: Int) {
        var channel = arrayChannels[index]
        txtTitle.text = channel.Title
        stepperChannel.value = Double(channel.Number!)
        lblNumber.text = Int(stepperChannel.value).description
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        segmentChannel.selectedSegmentIndex = 0
        refresh(segmentChannel.selectedSegmentIndex)
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
