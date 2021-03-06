//
//  ViewController.swift
//  RZHUANG.iOS.TVRemoteControl
//
//  Created by Johnny on 4/20/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    //Favorate Channel List
    enum Favorite: Int {
        case ABC = 33
        case NBC = 44
        case CBS = 55
        case FOX = 66
    }
    
    let defaultVolume: Int = 30  //Default volume value
    let defaultChannel: Int = 14 //Defualt channel
    var volume : Int = 0
    var channel: Int = 1
    var strChannel: String = ""
    
    @IBOutlet weak var viewControl: UIView!
    @IBOutlet weak var lblPower: UILabel!
    @IBOutlet weak var lblVolume: UILabel!
    @IBOutlet weak var lblChannel: UILabel!
    @IBOutlet weak var sliderVolume: UISlider!
    @IBOutlet weak var btnNum0: UIButton!
    @IBOutlet weak var btnNum1: UIButton!
    @IBOutlet weak var btnNum2: UIButton!
    @IBOutlet weak var btnNum3: UIButton!
    @IBOutlet weak var btnNum4: UIButton!
    @IBOutlet weak var btnNum5: UIButton!
    @IBOutlet weak var btnNum6: UIButton!
    @IBOutlet weak var btnNum7: UIButton!
    @IBOutlet weak var btnNum8: UIButton!
    @IBOutlet weak var btnNum9: UIButton!
    @IBOutlet weak var btnChannelNext: UIButton!
    @IBOutlet weak var btnChannelPrevious: UIButton!
    @IBOutlet weak var segmentChannel: UISegmentedControl!
    @IBOutlet weak var channelImage: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        reset()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    //Reset the remote control
    func reset() {
        volume = defaultVolume
        channel = defaultChannel
        lblVolume.text = String(volume)
        lblChannel.text = String(channel)
        sliderVolume.value = Float(volume)
        segmentChannel.selectedSegmentIndex = -1
        channelImage.image = nil
    }

    //Power toggle event
    @IBAction func powerToggled(sender: UISwitch) {
        lblPower.text = sender.on ? "On" : "Off"
        let enabled = (sender.on ? true : false)
        sliderVolume.enabled = enabled
        //viewControl.userInteractionEnabled = enabled
        btnNum0.enabled = enabled
        btnNum1.enabled = enabled
        btnNum2.enabled = enabled
        btnNum3.enabled = enabled
        btnNum4.enabled = enabled
        btnNum5.enabled = enabled
        btnNum6.enabled = enabled
        btnNum7.enabled = enabled
        btnNum8.enabled = enabled
        btnNum9.enabled = enabled
        btnChannelNext.enabled = enabled
        btnChannelPrevious.enabled = enabled
        btnChannelPrevious.enabled = enabled
        segmentChannel.enabled = enabled
        //if enabled==true{
        //    reset()
        //}
        
    }
    
    //Volume slide event
    @IBAction func volumeChanged(sender: UISlider) {
        lblVolume.text = String(Int(sender.value))
    }
    
    //Next channel event
    @IBAction func nextChannel(sender: UIButton) {
        if (channel<99) {
            updateChannel(++channel)
        }
    }
    
    //Previous chaneel event
    @IBAction func previousChannel(sender: UIButton) {
        if (channel>1) {
            updateChannel(--channel)
        }
    }
    
    //Number(0~9) click event
    @IBAction func numClicked(sender: UIButton) {
        if let btnTitle = sender.currentTitle {
            if strChannel.isEmpty {
                strChannel="\(btnTitle)"
            }
            else {
                strChannel+="\(btnTitle)"
                var newchannel:Int = strChannel.toInt()!
                updateChannel(newchannel)
            }
        }
    }
    
    //Shortcut key for favorate channel
    @IBAction func favoriteChannelChanged(sender: UISegmentedControl) {
        let index = sender.selectedSegmentIndex
        switch index {
            case 0:
                updateChannel(Favorite.ABC.rawValue)
            case 1:
                updateChannel(Favorite.NBC.rawValue)
            case 2:
                updateChannel(Favorite.CBS.rawValue)
            case 3:
                updateChannel(Favorite.FOX.rawValue)
            default:
                updateChannel(defaultChannel)
        }
    }
    
    //Update channel number according to the change events
    func updateChannel(newchannel: Int) {
        if (newchannel>0&&newchannel<100) {
            lblChannel.text = String(newchannel)
            self.channel = newchannel
            switch self.channel {
            case Favorite.ABC.rawValue:
                    segmentChannel.selectedSegmentIndex = 0
                    channelImage.image = UIImage(named: "ABC")
                case Favorite.NBC.rawValue:
                    segmentChannel.selectedSegmentIndex = 1
                    channelImage.image = UIImage(named: "NBC")
                case Favorite.CBS.rawValue:
                    segmentChannel.selectedSegmentIndex = 2
                    channelImage.image = UIImage(named: "CBS")
                case Favorite.FOX.rawValue:
                    segmentChannel.selectedSegmentIndex = 3
                    channelImage.image = UIImage(named: "FOX")
                default:
                    segmentChannel.selectedSegmentIndex = -1
                    channelImage.image = nil
            }
            strChannel = "";
        }
    }
}

