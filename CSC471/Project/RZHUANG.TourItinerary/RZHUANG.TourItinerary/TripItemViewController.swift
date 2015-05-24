//
//  TripItemViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/14/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class TripItemViewController: UITableViewController {

    var trip: Trip?
    var city: City?
   
    @IBOutlet weak var navigationTitle: UINavigationItem!

    @IBOutlet var textFields: [UITextField]!
    
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
    @IBOutlet weak var txtNote: UITextField!
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        btnFrom.titleLabel?.textAlignment = .Right
        btnTo.titleLabel?.textAlignment = .Right
        
        if trip == nil {
            if let c = city {
                navigationTitle.title = city?.name
                txtCity.text = city?.name
            }
            else {
                navigationTitle.title = ""
            }
            btnFrom.setTitle(NSDate().formatted, forState: .Normal)
            btnTo.setTitle(NSDate().formatted, forState: .Normal)
        }
        
        if let t = trip {
            //var indexPath = NSIndexPath(forRow: 1, inSection: 0)
            //tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
            navigationTitle.title = t.destination
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
            txtNote.text = t.note
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        self.tabBarController?.tabBar.hidden = true
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func saveTrip(sender: UIBarButtonItem) {
        
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
        var title = "Select Date"
        var message = "\n\n\n\n\n\n\n\n\n\n\n\n\n";
        var alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.ActionSheet);
        alert.modalInPopover = true;
        
        //Create a frame (placeholder/wrapper) for the picker and then create the picker
        var pickerFrame: CGRect = CGRectMake(0, 30, self.view.frame.width-17, 100); // CGRectMake(left), top, width, height) - left and top are like margins
        var datePicker: UIDatePicker = UIDatePicker(frame: pickerFrame);
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
        let cancelAction = UIAlertAction(title: "Cancel", style: .Cancel, handler: nil)
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
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as! UITableViewCell

        // Configure the cell...

        return cell
    }
    */

    
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return NO if you do not want the specified item to be editable.
        return true
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        
        
        if (indexPath.row == 0 && indexPath.section == 0 && trip != nil)
        {
            lblCity.hidden = true
            txtCity.hidden = true
            return 0.0
        }
        else {
            return super.tableView(tableView, heightForRowAtIndexPath: indexPath)
        }
        
        //return super.tableView(tableView, heightForRowAtIndexPath: indexPath)
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

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
        //performSegueWithIdentifier("toTripList", sender: self)
        if let t = trip {
            //lblTitle.title = t.destination
            //lblCountry.text =
            t.destination = txtCity.text
            t.country = txtCountry.text
            t.from = convertDate(btnFrom.titleLabel?.text)
            t.to = convertDate(btnTo.titleLabel?.text)
            t.flight1 = txtFlight1.text
            t.flight2 = txtFlight2.text
            t.hotel = txtHotel.text
            t.sights[0] = txtSight1.text
            t.sights[1] = txtSight2.text
            t.sights[2] = txtSight3.text
            t.sights[3] = txtSight4.text
            t.sights[4] = txtSight5.text
            t.note = txtNote.text
            tableView.reloadData()
        }
        else {
            var newTrip = Trip()
            newTrip.destination = txtCity.text
            newTrip.country = txtCountry.text
            newTrip.from = convertDate(btnFrom.titleLabel?.text)
            newTrip.to = convertDate(btnTo.titleLabel?.text)
            newTrip.flight1 = txtFlight1.text
            newTrip.flight2 = txtFlight2.text
            newTrip.hotel = txtHotel.text
            newTrip.sights[0] = txtSight1.text
            newTrip.sights[1] = txtSight2.text
            newTrip.sights[2] = txtSight3.text
            newTrip.sights[3] = txtSight4.text
            newTrip.sights[4] = txtSight5.text
            newTrip.note = txtNote.text
            trips.insert(newTrip, atIndex: 0)
            //tableView.reloadData()
            
        }
        /*
        if let destVC = segue.destinationViewController as? TripListViewController {
            //self.hidesBottomBarWhenPushed = false
            //if let tab = self.parentViewController!.tabBarController {
            //    tab.
            //}
            
            //destVC.hidesBottomBarWhenPushed = false
            destVC.tabBarController?.selectedIndex = 0
        }*/
    }
   

}
