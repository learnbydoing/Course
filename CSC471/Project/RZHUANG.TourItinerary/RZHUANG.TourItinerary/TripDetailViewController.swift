//
//  TripDetailViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/14/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class TripDetailViewController: UITableViewController {

    var trip: Trip?
    var viewTitle: String?
    
    @IBOutlet var textFields: [UITextField]!
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    @IBOutlet weak var imageCity: UIImageView!
    @IBOutlet weak var btnShare: UIButton!
    @IBOutlet weak var lblCity: UILabel!
    @IBOutlet weak var txtCity: UITextField!
    @IBOutlet weak var txtCountry: UITextField!
    @IBOutlet weak var btnFrom: UIButton!
    @IBOutlet weak var btnTo: UIButton!
    @IBOutlet weak var txtFlight1: UITextField!
    @IBOutlet weak var txtFlight2: UITextField!
    @IBOutlet weak var txtHotel: UITextField!
    @IBOutlet weak var txtSight1: UITextField!
    @IBOutlet weak var txtSight2: UITextField!
    @IBOutlet weak var txtSight3: UITextField!
    @IBOutlet weak var txtSight4: UITextField!
    @IBOutlet weak var txtSight5: UITextField!
    @IBOutlet weak var textviewNote: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        //self.tabBarController?.tabBar.hidden = true
        
        navigationTitle.title = viewTitle
        btnFrom.titleLabel?.textAlignment = .Right
        btnTo.titleLabel?.textAlignment = .Right
        textviewNote!.layer.borderWidth = 1
        textviewNote!.layer.borderColor = UIColor.grayColor().CGColor
        
        if trip == nil {
            btnFrom.setTitle(NSDate().formatted, forState: .Normal)
            btnTo.setTitle(NSDate().formatted, forState: .Normal)
        }
        
        if let t = trip {
            imageCity.image = getImage(t.destination)
            txtCity.text = t.destination
            txtCountry.text = t.country
            btnFrom.setTitle(t.from.formatted, forState: .Normal)
            btnTo.setTitle(t.to.formatted, forState: .Normal)
            txtFlight1.text = t.flight1
            txtFlight2.text = t.flight2
            txtHotel.text = t.hotel
            txtSight1.text = t.sights[0]
            txtSight2.text = t.sights[1]
            txtSight3.text = t.sights[2]
            txtSight4.text = t.sights[3]
            txtSight5.text = t.sights[4]
            textviewNote.text = t.note
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    @IBAction func shareTrip(sender: UIButton) {
        var textToShare = "Hi, \n\nbelow are the trip details!\n\n"
        textToShare += "City: \(trip!.destination)\n"
        textToShare += "Country: \(trip!.country)\n"
        textToShare += "From: \(trip!.from.formatted)\n"
        textToShare += "To: \(trip!.to.formatted)\n"
        textToShare += "Departure Flight: \(trip!.flight1)\n"
        textToShare += "Return Flight: \(trip!.flight2)\n"
        textToShare += "Hotel: \(trip!.hotel)\n"
        textToShare += "Note: \(trip!.note)\n"
        let objectsToShare = [textToShare, []]
        let activityVC = UIActivityViewController(activityItems: objectsToShare as [AnyObject], applicationActivities: nil)
        
        self.presentViewController(activityVC, animated: true, completion: nil)
    }
    
    @IBAction func saveTrip(sender: UIBarButtonItem) {
    
        if txtCity.text!.isEmpty{
            let title = "Error"
            let alertController = UIAlertController(title: title, message: "City can't be empty!", preferredStyle: .Alert)
            
            // Create the action.
            let cancelAction = UIAlertAction(title: "OK", style: .Cancel, handler: nil)
            alertController.addAction(cancelAction)
            presentViewController(alertController, animated: true, completion: nil)
            return
        }
        
        let fromdate: NSDate = convertDate(btnFrom.titleLabel?.text)
        let todate: NSDate = convertDate(btnTo.titleLabel?.text)
        if fromdate.compare(todate) == NSComparisonResult.OrderedDescending {
            let title = "Error"
            let alertController = UIAlertController(title: title, message: "From date can't be larger than To date!", preferredStyle: .Alert)
            
            // Create the action.
            let cancelAction = UIAlertAction(title: "OK", style: .Cancel, handler: nil)
            alertController.addAction(cancelAction)
            presentViewController(alertController, animated: true, completion: nil)
            return
        }
        
        if trip == nil {
            var newTrip = Trip()
            newTrip.destination = txtCity.text!
            newTrip.country = txtCountry.text!
            newTrip.from = fromdate
            newTrip.to = todate
            newTrip.flight1 = txtFlight1.text!
            newTrip.flight2 = txtFlight2.text!
            newTrip.hotel = txtHotel.text!
            newTrip.sights[0] = txtSight1.text!
            newTrip.sights[1] = txtSight2.text!
            newTrip.sights[2] = txtSight3.text!
            newTrip.sights[3] = txtSight4.text!
            newTrip.sights[4] = txtSight5.text!
            newTrip.note = textviewNote.text
            addNewTrip(newTrip)
        }
        else {
            trip!.destination = txtCity.text!
            trip!.country = txtCountry.text!
            trip!.from = fromdate
            trip!.to = todate
            trip!.flight1 = txtFlight1.text!
            trip!.flight2 = txtFlight2.text!
            trip!.hotel = txtHotel.text!
            trip!.sights[0] = txtSight1.text!
            trip!.sights[1] = txtSight2.text!
            trip!.sights[2] = txtSight3.text!
            trip!.sights[3] = txtSight4.text!
            trip!.sights[4] = txtSight5.text!
            trip!.note = textviewNote.text
            //tableView.reloadData()
        }
        navigationController!.popViewControllerAnimated(true)
    }
    
    @IBAction func editEnded(sender: UITextField) {
        sender.resignFirstResponder()
    }    
    @IBAction func fromDateChange(sender: UIButton) {
        popDatePicker("1")
    }
    @IBAction func toDateChange(sender: UIButton) {
        popDatePicker("2")
    }
    func popDatePicker(dateno: String) {
        let title = "Select Date"
        let message = "\n\n\n\n\n\n\n\n\n\n\n\n\n";
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.ActionSheet);
        alert.modalInPopover = true;
        
        var pickerFrame: CGRect
        if deviceOrientation == true {
            pickerFrame = CGRect(x: -130, y: 30, width: self.view.frame.width-17, height: 100); //landscape
        }
        else {
            pickerFrame = CGRect(x: 0, y: 30, width: self.view.frame.width-17, height: 100); //portrait
        }
        let datePicker: UIDatePicker = UIDatePicker(frame: pickerFrame);
        datePicker.datePickerMode = .DateAndTime
        datePicker.locale = NSLocale(localeIdentifier: "en_US")

        if dateno=="1" {
            datePicker.setDate(convertDate(self.btnFrom.titleLabel?.text), animated: true)
        }
        else if dateno=="2" {
            datePicker.setDate(convertDate(self.btnTo.titleLabel?.text), animated: true)
        }

        alert.view.addSubview(datePicker);
        
        // Create the action.
        let cancelAction = UIAlertAction(title: "Cancel", style: .Destructive, handler: nil)
        let okayAction = UIAlertAction(title: "Confirm", style: .Default) {
            action in self.updateDate(dateno, newdate: datePicker.date)
        }
        alert.addAction(okayAction)
        alert.addAction(cancelAction)
        presentViewController(alert, animated: true, completion: nil)

    }
    
    func updateDate(dateno: String, newdate: NSDate) {
        if dateno=="1" {
            btnFrom.setTitle(newdate.formatted, forState: .Normal)
            if let t = trip {
                t.from = newdate
            }
        }
        else if dateno=="2" {
            btnTo.setTitle(newdate.formatted, forState: .Normal)
            if let t = trip {
                t.to = newdate
            }
        }
    }
    
    // MARK: - Table view data source

    /*override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        return 0
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        return 0
    }*/
    
    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
    }*/

    
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return NO if you do not want the specified item to be editable.
        return true
    }
    
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if (indexPath.row == 0 && indexPath.section == 0 && trip == nil)
        {
            imageCity.hidden = true
            btnShare.hidden = true
            return 0.0
        }
        else {
            return super.tableView(tableView, heightForRowAtIndexPath: indexPath)
        }
    }

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return NO if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.    
    }
   */

}
