//
//  CityDetailViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/20/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class CityDetailViewController: UITableViewController {

    var city: City?
    @IBOutlet weak var navigationTitle: UINavigationItem!
    
    @IBOutlet weak var imageCity: UIImageView!
    @IBOutlet weak var lblTitle: UILabel!
    //@IBOutlet weak var txtviewOthers: UITextView!
    @IBOutlet weak var lblDescription: UILabel!
 
    @IBOutlet weak var lblArea: UILabel!
    @IBOutlet weak var lblFounded: UILabel!    
    @IBOutlet weak var lblLocalTime: UILabel!
    @IBOutlet weak var lblWeather: UILabel!
    @IBOutlet weak var lblPopulation: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationTitle.title = city?.name
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        if let c = city {
            imageCity.image = UIImage(named: c.image)
            lblTitle.text = c.title
            //txtviewDescription.text = c.description
            lblDescription.numberOfLines = 0;
            lblDescription.text = c.description
            self.lblDescription.sizeToFit()
             //var frame = txtviewDescription.frame;
            //frame.size.height = txtviewDescription.contentSize.height
            //txtviewDescription.frame = frame
            /*txtviewOthers.text = "Area: " + "\(c.area)" + "\n"
                                + "Founded: " + "\(c.founded)" + "\n"
           + "Local time: " + "\(c.localtime)" + "\n"
            + "Weather: " + "\(c.weather)" + "\n"
            + "Population: " + "\(c.population)"*/
            lblArea.text = c.area
            lblFounded.text = c.founded
            lblLocalTime.text = c.localtime
            lblWeather.text = c.weather
            lblPopulation.text = c.population
        }
    }

    override func viewDidLayoutSubviews() {
        
        super.viewDidLayoutSubviews()
        
        
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

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
    }*/

    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as! UITableViewCell

        // Configure the cell...

        return cell
    }
    */
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        
        
        if (indexPath.row == 2 && indexPath.section == 0)
        {
            //var height: CGFloat = 0.0
            //println("self.txtviewDescripton.frame.height:" + "\(self.txtviewDescription.frame.height)");
            //println(self.txtviewDescription.text)
            
            //let contentSize = self.txtviewDescription.sizeThatFits(self.txtviewDescription.bounds.size)
            //var frame = self.txtviewDescription.frame
            //frame.size.height = contentSize.height
            //frame.size.height = txtviewDescription.contentSize.height
            //println("frame.size.height:" + "\(frame.size.height)")
            //self.txtviewDescription.frame = frame
            
            //let aspectRatioTextViewConstraint = NSLayoutConstraint(item: self.txtviewDescription, attribute: .Height, relatedBy: .Equal, toItem: self.txtviewDescription, attribute: .Width, multiplier: txtviewDescription.bounds.height/txtviewDescription.bounds.width, constant: 1)
            //self.txtviewDescription.addConstraint(aspectRatioTextViewConstraint)
            
            //var height = self.txtviewDescription.frame.height + 2;
            println("self.lblDescription.frame.height:" + "\(self.lblDescription.frame.height)")
            //lblDescription.numberOfLines = 0;
            //self.lblDescription.sizeToFit()
            println("self.lblDescription.frame.height:" + "\(self.lblDescription.frame.height)")
            return self.lblDescription.frame.height * (2.3)

        }
        else {
            return super.tableView(tableView, heightForRowAtIndexPath: indexPath)
        }
        
        //return super.tableView(tableView, heightForRowAtIndexPath: indexPath)
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
        if let tripItemViewController = segue.destinationViewController as? TripItemViewController {
            tripItemViewController.city = self.city
            //tripItemViewController.tabBarController?.selectedIndex = 0
        }
    }    

}
