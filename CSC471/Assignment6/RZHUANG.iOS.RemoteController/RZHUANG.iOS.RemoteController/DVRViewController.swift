//
//  SecondViewController.swift
//  RZHUANG.iOS.RemoteController
//
//  Created by Johnny on 5/12/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class DVRViewController: UIViewController {

    //Favorate Channel List
    enum State: Int {
        case Stopped = 1
        case Playing = 2
        case Paused = 3
        case FastForwarding = 4
        case FastRewinding = 5
        case Recording = 6
    }
    
    var dvrState = State.Stopped
    
    @IBOutlet weak var lblPower: UILabel!
    @IBOutlet weak var lblState: UILabel!
    @IBOutlet weak var btnPlay: UIButton!
    @IBOutlet weak var btnStop: UIButton!
    @IBOutlet weak var btnPause: UIButton!
    @IBOutlet weak var btnFastForward: UIButton!
    @IBOutlet weak var btnFastRewind: UIButton!
    @IBOutlet weak var btnRecord: UIButton!
    
    func reset() {
        lblPower.text = "On"
        dvrState = State.Stopped
        updateState(dvrState)
    }
    
    func updateState(newstate: State) -> Void {
        
        lblState.text = getStateText(newstate);
        setButtonImage(newstate)
        dvrState = newstate
    }
    
    //Set the button image to 'off'.
    func setButtonImage(state: State) {
        switch state {
        case .Playing:
            let image1 = UIImage(named: "DVR_Play_Off") as UIImage!
            btnPlay.setImage(image1, forState: .Normal)
            let image2 = UIImage(named: "DVR_Pause_On") as UIImage!
            btnPause.setImage(image2, forState: .Normal)
            let image3 = UIImage(named: "DVR_Stop_On") as UIImage!
            btnStop.setImage(image3, forState: .Normal)
            let image4 = UIImage(named: "DVR_Fast_Forward_On") as UIImage!
            btnFastForward.setImage(image4, forState: .Normal)
            let image5 = UIImage(named: "DVR_Fast_Rewind_On") as UIImage!
            btnFastRewind.setImage(image5, forState: .Normal)
            let image6 = UIImage(named: "DVR_Record_Off") as UIImage!
            btnRecord.setImage(image6, forState: .Normal)
        case .Paused:
            let image1 = UIImage(named: "DVR_Play_On") as UIImage!
            btnPlay.setImage(image1, forState: .Normal)
            let image2 = UIImage(named: "DVR_Pause_Off") as UIImage!
            btnPause.setImage(image2, forState: .Normal)
            let image3 = UIImage(named: "DVR_Stop_On") as UIImage!
            btnStop.setImage(image3, forState: .Normal)
            let image4 = UIImage(named: "DVR_Fast_Forward_Off") as UIImage!
            btnFastForward.setImage(image4, forState: .Normal)
            let image5 = UIImage(named: "DVR_Fast_Rewind_Off") as UIImage!
            btnFastRewind.setImage(image5, forState: .Normal)
            let image6 = UIImage(named: "DVR_Record_Off") as UIImage!
            btnRecord.setImage(image6, forState: .Normal)
        case .Stopped:
            let image1 = UIImage(named: "DVR_Play_On") as UIImage!
            btnPlay.setImage(image1, forState: .Normal)
            let image2 = UIImage(named: "DVR_Pause_Off") as UIImage!
            btnPause.setImage(image2, forState: .Normal)
            let image3 = UIImage(named: "DVR_Stop_Off") as UIImage!
            btnStop.setImage(image3, forState: .Normal)
            let image4 = UIImage(named: "DVR_Fast_Forward_Off") as UIImage!
            btnFastForward.setImage(image4, forState: .Normal)
            let image5 = UIImage(named: "DVR_Fast_Rewind_Off") as UIImage!
            btnFastRewind.setImage(image5, forState: .Normal)
            let image6 = UIImage(named: "DVR_Record_On") as UIImage!
            btnRecord.setImage(image6, forState: .Normal)
        case .FastForwarding:
            let image1 = UIImage(named: "DVR_Play_On") as UIImage!
            btnPlay.setImage(image1, forState: .Normal)
            let image2 = UIImage(named: "DVR_Pause_Off") as UIImage!
            btnPause.setImage(image2, forState: .Normal)
            let image3 = UIImage(named: "DVR_Stop_On") as UIImage!
            btnStop.setImage(image3, forState: .Normal)
            let image4 = UIImage(named: "DVR_Fast_Forward_Off") as UIImage!
            btnFastForward.setImage(image4, forState: .Normal)
            let image5 = UIImage(named: "DVR_Fast_Rewind_Off") as UIImage!
            btnFastRewind.setImage(image5, forState: .Normal)
            let image6 = UIImage(named: "DVR_Record_Off") as UIImage!
            btnRecord.setImage(image6, forState: .Normal)
        case .FastRewinding:
            let image1 = UIImage(named: "DVR_Play_On") as UIImage!
            btnPlay.setImage(image1, forState: .Normal)
            let image2 = UIImage(named: "DVR_Pause_Off") as UIImage!
            btnPause.setImage(image2, forState: .Normal)
            let image3 = UIImage(named: "DVR_Stop_On") as UIImage!
            btnStop.setImage(image3, forState: .Normal)
            let image4 = UIImage(named: "DVR_Fast_Forward_Off") as UIImage!
            btnFastForward.setImage(image4, forState: .Normal)
            let image5 = UIImage(named: "DVR_Fast_Rewind_Off") as UIImage!
            btnFastRewind.setImage(image5, forState: .Normal)
            let image6 = UIImage(named: "DVR_Record_Off") as UIImage!
            btnRecord.setImage(image6, forState: .Normal)
        case .Recording:
            let image1 = UIImage(named: "DVR_Play_Off") as UIImage!
            btnPlay.setImage(image1, forState: .Normal)
            let image2 = UIImage(named: "DVR_Pause_Off") as UIImage!
            btnPause.setImage(image2, forState: .Normal)
            let image3 = UIImage(named: "DVR_Stop_On") as UIImage!
            btnStop.setImage(image3, forState: .Normal)
            let image4 = UIImage(named: "DVR_Fast_Forward_Off") as UIImage!
            btnFastForward.setImage(image4, forState: .Normal)
            let image5 = UIImage(named: "DVR_Fast_Rewind_Off") as UIImage!
            btnFastRewind.setImage(image5, forState: .Normal)
            let image6 = UIImage(named: "DVR_Record_Off") as UIImage!
            btnRecord.setImage(image6, forState: .Normal)
        }
    }
    
    //get state text
    func getStateText(state: State) ->String {
        switch state {
        case .Playing:
            return "Playing"
        case .Paused:
            return "Paused"
        case .Stopped:
            return "Stopped"
        case .FastForwarding:
            return "Fast Forwarding"
        case .FastRewinding:
            return "Fast Rewinding"
        case .Recording:
            return "Recording"
        }
    }
    
    func showPopup(message: String, newstate: State, oldstate: State) {
        //second popup.
        let confirmMessage = "DVR state has been switched from " + getStateText(oldstate) + " to " + getStateText(newstate)
        let forceActionHandler = { (action:UIAlertAction!) in
            self.updateState(newstate)
        }
        
        let callActionHandler = { (action:UIAlertAction!) -> Void in
            let alertMessage = UIAlertController(title: "Force to continue", message: confirmMessage, preferredStyle: .Alert)
            alertMessage.addAction(UIAlertAction(title: "Confirm", style: .Default, handler: forceActionHandler))
            self.presentViewController(alertMessage, animated: true, completion: nil)
        }
        
        // First popup.
        let title = "Inproper Operation"
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .ActionSheet)
        let cancelAction = UIAlertAction(title: "Cancel", style: .Destructive, handler: nil)
        alertController.addAction(cancelAction)
        let continueAction = UIAlertAction(title: "Continue", style: .Default, handler: callActionHandler);
        alertController.addAction(continueAction)
        
        presentViewController(alertController, animated: true, completion: nil)
    }
    
    func showConfirmation() {
    }
    
    @IBAction func powerSwitch(sender: UISwitch) {
        lblPower.text = sender.on ? "On" : "Off"
        if sender.on {
            reset();
        }
        
        let enabled = (sender.on ? true : false)
        
        btnPlay.enabled = enabled
        btnStop.enabled = enabled
        btnPause.enabled = enabled
        btnFastForward.enabled = enabled
        btnFastRewind.enabled = enabled
        btnRecord.enabled = enabled
    }
    
    @IBAction func onPlay(sender: UIButton) {
        if dvrState == State.Recording {
            showPopup("Recording now, turn to play anyway?", newstate: State.Playing, oldstate: dvrState)
        }
        else {
            updateState(State.Playing)
        }
    }
    @IBAction func onStop(sender: UIButton) {
        updateState(State.Stopped)
    }
    @IBAction func onPause(sender: UIButton) {
        if dvrState == State.Playing {
            updateState(State.Paused)
        }
        else {
            showPopup("Not playing now, can't pause, continue?", newstate: State.Paused, oldstate: dvrState)
        }
    }
    @IBAction func onFastForward(sender: UIButton) {
        if dvrState == State.Playing {
            updateState(State.FastForwarding)
        }
        else {
            showPopup("Not playing now, can't fast forward, continue?", newstate: State.FastForwarding, oldstate: dvrState)
        }
    }
    @IBAction func onFastRewind(sender: UIButton) {
        if dvrState == State.Playing {
            updateState(State.FastRewinding)
        }
        else {
            showPopup("Not playing now, can't fast rewind, continue?", newstate: State.FastRewinding, oldstate: dvrState)
        }
    }
    @IBAction func onRecord(sender: UIButton) {
        if dvrState == State.Stopped {
            updateState(State.Recording)
        }
        else {
            showPopup("Not stopped now, can't record, continue?", newstate: State.Recording,oldstate: dvrState)
        }
    }
    
    @IBAction func switchToTV(sender: AnyObject) {
        dismissViewControllerAnimated(true, completion: nil)
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        reset();
        
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

