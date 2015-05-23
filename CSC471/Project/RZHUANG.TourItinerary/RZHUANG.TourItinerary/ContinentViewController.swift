//
//  ContinentViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/14/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//  Component controller: http://www.brianjcoleman.com/tutorial-collection-view-using-swift/

import UIKit

let reuseIdentifier = "Cell"

class ContinentViewController: UICollectionViewController {

    var selectedCell: NSIndexPath?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Register cell classes
        //self.collectionView!.registerClass(UICollectionViewCell.self, forCellWithReuseIdentifier: reuseIdentifier)
        let layout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        layout.sectionInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
        layout.itemSize = CGSize(width: (self.view.frame.width - 30)/2, height: 60)
        collectionView = UICollectionView(frame: self.view.frame, collectionViewLayout: layout)
        collectionView!.dataSource = self
        collectionView!.delegate = self
        collectionView!.registerClass(ContinentViewCell.self, forCellWithReuseIdentifier: "ContinentViewCell")
        collectionView!.backgroundColor = UIColor.whiteColor()
        self.view.addSubview(collectionView!)
        
        
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
        if segue.identifier == "toCountry" {
            if let countryViewController = segue.destinationViewController as? CountryViewController {
                //let cell = sender as! UICollectionViewCell
                //if let indexPath = self.collectionView!.indexPathForCell(cell) {
                    countryViewController.continent = continents[selectedCell!.item]
                //}
            }
        }
    }
    

    // MARK: UICollectionViewDataSource

    override func numberOfSectionsInCollectionView(collectionView: UICollectionView) -> Int {
        //#warning Incomplete method implementation -- Return the number of sections
        return 1
    }


    override func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        //#warning Incomplete method implementation -- Return the number of items in the section
        return 7
    }

    override func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        //let cell = collectionView.dequeueReusableCellWithReuseIdentifier(reuseIdentifier, forIndexPath: indexPath) as! UICollectionViewCell
    
        // Configure the cell
        //let cell = collectionView.dequeueReusableCellWithReuseIdentifier("ContinentViewCell", forIndexPath: indexPath) as! ContinentViewCell
        
        //cell.textLabel.text = "\(indexPath.section):\(indexPath.row)"
        //cell.imageView.image = UIImage(named: "circle")
        //return cell
        
        let continent = continents[indexPath.row]
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier("ContinentViewCell", forIndexPath: indexPath) as! ContinentViewCell
        cell.backgroundColor = UIColorFromRGB(0x209624)
        cell.txtName.text = continent.name
        cell.txtDetails.text = "\(continent.locations) countries/areas"

        //cell.textLabel.alignmentRectForFrame(CGRect(x: 0, y: 0, width: cell.frame.size.width/3, height: cell.frame.size.height*2/3))
        //cell.detailTextLabel?.text = "\(continent.locations) countries or areas"
        cell.imageView.image = UIImage(named: continent.image)
        
        return cell

        
        //return cell
    }

    // MARK: UICollectionViewDelegate

    /*
    // Uncomment this method to specify if the specified item should be highlighted during tracking
    override func collectionView(collectionView: UICollectionView, shouldHighlightItemAtIndexPath indexPath: NSIndexPath) -> Bool {
        return false
    }
    

    
    // Uncomment this method to specify if the specified item should be selected
    override func collectionView(collectionView: UICollectionView, shouldSelectItemAtIndexPath indexPath: NSIndexPath) -> Bool {
        return false
    }*/
    

    
    // Uncomment these methods to specify if an action menu should be displayed for the specified item, and react to actions performed on the item
    /*override func collectionView(collectionView: UICollectionView, shouldShowMenuForItemAtIndexPath indexPath: NSIndexPath) -> Bool {
        return true
    }

    override func collectionView(collectionView: UICollectionView, canPerformAction action: Selector, forItemAtIndexPath indexPath: NSIndexPath, withSender sender: AnyObject?) -> Bool {
        return true
    }*/
/*
    override func collectionView(collectionView: UICollectionView, performAction action: Selector, forItemAtIndexPath indexPath: NSIndexPath, withSender sender: AnyObject?) {
        
        var nametext = continents[indexPath.row].name
        println(nametext)
        
    
    }*/
    
    override func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        selectedCell = indexPath
        var nametext = continents[indexPath.item].name
        println(nametext)
        performSegueWithIdentifier("toCountry", sender: self)
    }
    /*
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
        /*if segue.identifier == "showZoomController" {
            let zoomVC = segue.destinationViewController as PhotoZoomViewController
            let cell = sender as UICollectionViewCell
            let indexPath = self.collectionView!.indexPathForCell(cell)
            let userPost  = self.timeLineData.objectAtIndex(indexPath.row) as PFObject
            zoomVC.post = userPost
        }*/
        if segue.identifier == "toCountry" {
            if let countryViewController = segue.destinationViewController as? CountryViewController {
                let cell = sender as! UICollectionViewCell
                if let indexPath = self.collectionView!.indexPathForCell(cell) {
                    countryViewController.country = countries[indexPath.item]
                }
            }
        }
    }*/
    

}
