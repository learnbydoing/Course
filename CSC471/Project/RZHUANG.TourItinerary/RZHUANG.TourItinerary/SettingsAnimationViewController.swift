//
//  SettingsAnimationViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 6/3/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

let options = [
    "Curl Down",
    "Curl Up(Default)",
    "Dissolve",
    "Flip Left",
    "Flip Right",
    "Flip Top",
    "Flip Bottom",
]

class SettingsAnimationViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {

    @IBOutlet weak var pickerTransitionMode: UIPickerView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(animated: Bool) {
        
        super.viewWillAppear(animated)
        
        //set the selection of the picker
        var selectedIndex = -1
        switch appSettings.transitionOptions {
        case UIViewAnimationOptions.TransitionCurlDown:
            selectedIndex = 0
        case UIViewAnimationOptions.TransitionCurlUp:
            selectedIndex = 1
        case UIViewAnimationOptions.TransitionCrossDissolve:
            selectedIndex = 2
        case UIViewAnimationOptions.TransitionFlipFromLeft:
            selectedIndex = 3
        case UIViewAnimationOptions.TransitionFlipFromRight:
            selectedIndex = 4
        case UIViewAnimationOptions.TransitionFlipFromTop:
            selectedIndex = 5
        case UIViewAnimationOptions.TransitionFlipFromBottom:
            selectedIndex = 6
        default:
            selectedIndex = 1
        }
        
        pickerTransitionMode.selectRow(selectedIndex, inComponent: 0, animated: true)
    }
    
    @IBAction func saveSettings(sender: UIBarButtonItem) {
        var transitionOptions = UIViewAnimationOptions.TransitionNone
        switch options[pickerTransitionMode.selectedRowInComponent(0)] {
            case "Curl Down":
                transitionOptions = .TransitionCurlDown
            case "Curl Up":
                transitionOptions = .TransitionCurlUp
            case "Dissolve":
                transitionOptions = .TransitionCrossDissolve
            case "Flip Left":
                transitionOptions = .TransitionFlipFromLeft
            case "Flip Right":
                transitionOptions = .TransitionFlipFromRight
            case "Flip Top":
                transitionOptions = .TransitionFlipFromTop
            case "Flip Bottom":
                transitionOptions = .TransitionFlipFromBottom
            default:
                transitionOptions = .TransitionCurlUp
        }
        
        appSettings.transitionOptions = transitionOptions
        navigationController!.popViewControllerAnimated(true)
    }
    
    func numberOfComponentsInPickerView(pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return options.count
    }
    
    // MARK: UIPickerViewDelegate
    
    func pickerView(pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String {
        return options[row]
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
