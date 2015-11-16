//
//  MyFavoritesViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/26/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class MyFavoritesViewController: UITableViewController {

    var isEditMode = false
    var favoriteCityList = Array<City>()
    
    @IBOutlet weak var btnEdit: UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        
        favoriteCityList = getFavoriteCityList()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        favoriteCityList = getFavoriteCityList()
        tableView.reloadData()
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
    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        return favoriteCityList.count
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let city = favoriteCityList[indexPath.row]
        let cell = tableView.dequeueReusableCellWithIdentifier("basic", forIndexPath: indexPath) 
        
        // Configure the cell...
        cell.textLabel?.text = city.name
        cell.detailTextLabel?.text = city.title
        cell.imageView?.image = UIImage(named: city.image)
        return cell
    }
    
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
            favorites.removeAtIndex(indexPath.row)
            favoriteCityList = getFavoriteCityList()
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

    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.        
        if let cityDetailViewController = segue.destinationViewController as? CityDetailViewController {
            if let indexPath = self.tableView.indexPathForSelectedRow {
                cityDetailViewController.city = favoriteCityList[indexPath.row]
            }
        }
    }
}
