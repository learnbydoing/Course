//
//  TabMeViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/26/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit
import MapKit

class TabMeViewController: UITableViewController {

    var nextTrip: Trip?
    var lblNextTrip: UILabel = UILabel()  //Description of the next trip
    
    @IBOutlet weak var btnImage: UIButton!
    @IBOutlet weak var nextTripCell: UITableViewCell!
    @IBOutlet weak var imageViewNextTrip: UIImageView!
    @IBOutlet weak var lblMyTrips: UILabel!
    @IBOutlet weak var lblMyFavorites: UILabel!
    
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
    
    override func viewWillAppear(animated: Bool) {
        
        super.viewWillAppear(animated)
        
        //get the next trip each time when the view is load, even in return back case
        nextTrip = getNextTrip()
        if nextTrip == nil {
            imageViewNextTrip.image = getImage("")
            btnImage.enabled = false
            lblNextTrip.text = "There is no upcoming trip."
        }
        else {
            imageViewNextTrip.image = getImage(nextTrip!.destination)
            //The button is transparent and cover the image, just response to the onclick event
            btnImage.enabled = true
            btnImage.frame = imageViewNextTrip.frame //make the same size with image control.
            lblNextTrip.text = "Next Trip\nTo:     \(nextTrip!.destination) (\(nextTrip!.country))\nDate: \(nextTrip!.from.formatted)"
        }

        lblMyTrips.text = "My Trips (\(trips.count))"
        lblMyFavorites.text = "My Favorites (\(favorites.count))"
    }   
    
    /*
    override func tableView(tableView: UITableView,
        accessoryButtonTappedForRowWithIndexPath indexPath: NSIndexPath) {

    }*/
    
    /*
    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        return 0
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        return 0
    }
    */
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        
        if indexPath.row == 0 {
            let nexttripFrame = CGRect(x: 10, y: imageViewNextTrip.frame.height-60, width: imageViewNextTrip.frame.width, height: 60)
            lblNextTrip = UILabel(frame: nexttripFrame)
            lblNextTrip.font = UIFont.boldSystemFontOfSize(15.0)
            lblNextTrip.textColor = UIColor.whiteColor()
            lblNextTrip.textAlignment = .Left
            lblNextTrip.lineBreakMode = .ByWordWrapping // or NSLineBreakMode.ByWordWrapping
            lblNextTrip.numberOfLines = 0
            lblNextTrip.text = "Next Trip\nTo:     \(nextTrip!.destination) (\(nextTrip!.country))\nDate: \(nextTrip!.from.formatted)"
            //lblNextTrip.layer.borderColor = UIColor.greenColor().CGColor
            //lblNextTrip.layer.borderWidth = 1.0;
            nextTripCell.addSubview(lblNextTrip)
            return nextTripCell
        }
        else {
            return super.tableView(tableView, cellForRowAtIndexPath: indexPath)
        }
    }

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return NO if you do not want the specified item to be editable.
        return true
    }
    */

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
        if segue.identifier == "nextTrip" {
            if nextTrip != nil {
                if let tripDetailViewController = segue.destinationViewController as? TripDetailViewController {
                    tripDetailViewController.viewTitle = "Next Trip"
                    tripDetailViewController.trip = nextTrip
                }
            }
        }
        else if segue.identifier == "newTrip" {
            if let tripDetailViewController = segue.destinationViewController as? TripDetailViewController {
                tripDetailViewController.viewTitle = "New Trip"
            }
        }
    }
}
