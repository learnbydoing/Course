//
//  TripListViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/14/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class TripListViewController: UITableViewController {
    
    var isEditMode = false
    @IBOutlet weak var btnEdit: UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        return trips.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let trip = trips[indexPath.row]
        let cell = tableView.dequeueReusableCellWithIdentifier("basic", forIndexPath: indexPath) as! UITableViewCell
        
        // Configure the cell...
        
        cell.textLabel?.text = trip.destination
        
        cell.detailTextLabel?.text = trip.destination
        
        return cell
    }
    
    override func tableView(tableView: UITableView,
        accessoryButtonTappedForRowWithIndexPath indexPath: NSIndexPath) {
            let trip = trips[indexPath.row]
            let title = trip.destination
            let message = trip.from.formatted + "\n" + trip.to.formatted
            let alertController = UIAlertController(title: title, message: message, preferredStyle: .ActionSheet)
            let okayAction = UIAlertAction(title: "Okay", style: .Default, handler: nil)
            alertController.addAction(okayAction)
            presentViewController(alertController, animated: true, completion: nil)
            tableView.deselectRowAtIndexPath(indexPath, animated: true)
    }
    
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
        
        if let tripViewController = segue.destinationViewController as? TripItemViewController {
            if let indexPath = self.tableView.indexPathForSelectedRow() {
                tripViewController.trip = trips[indexPath.row]
            }
        }
    }
    
    @IBAction func unwindToTripList(segue : UIStoryboardSegue) {
        if let from = segue.sourceViewController as? TripItemViewController {
            //message = "Unwind from GreenViewController"
            //if !from.textField.text.isEmpty {
            //    message += "\nMessage: \(from.textField.text)"
            //}
            if let t = from.trip {
                //lblTitle.title = t.destination
                //lblCountry.text = 
                t.destination = from.txtCity.text
                t.country = from.txtCountry.text
                t.from = convertDate(from.btnFrom.titleLabel?.text)
                t.to = convertDate(from.btnTo.titleLabel?.text)
                t.flight1 = from.txtFlight1.text
                t.flight2 = from.txtFlight2.text
                t.hotel = from.txtHotel.text
                t.sights[0] = from.txtSight1.text
                t.sights[1] = from.txtSight2.text
                t.sights[2] = from.txtSight3.text
                t.sights[3] = from.txtSight4.text
                t.sights[4] = from.txtSight5.text
                t.note = from.txtNote.text
                tableView.reloadData()
            }
            else {
                var newTrip = Trip()
                newTrip.destination = from.txtCity.text
                newTrip.country = from.txtCountry.text
                newTrip.from = convertDate(from.btnFrom.titleLabel?.text)
                newTrip.to = convertDate(from.btnTo.titleLabel?.text)
                newTrip.flight1 = from.txtFlight1.text
                newTrip.flight2 = from.txtFlight2.text
                newTrip.hotel = from.txtHotel.text
                newTrip.sights[0] = from.txtSight1.text
                newTrip.sights[1] = from.txtSight2.text
                newTrip.sights[2] = from.txtSight3.text
                newTrip.sights[3] = from.txtSight4.text
                newTrip.sights[4] = from.txtSight5.text
                newTrip.note = from.txtNote.text
                trips.insert(newTrip, atIndex: 0)
                tableView.reloadData()

            }
        }
        
    }
    
    @IBAction func switchEditMode(sender: UIBarButtonItem) {
        if isEditMode==false {
            tableView.setEditing(true, animated: true)
            btnEdit.title = "Done"
            isEditMode = true
        }
        else {
            tableView.setEditing(false, animated: true)
            btnEdit.title = "Edit"
            isEditMode = false
        }
    }
    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as! UITableViewCell

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return NO if you do not want the specified item to be editable.
        return true
    }
    */

    
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            trips.removeAtIndex(indexPath.row)
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    

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
