//
//  CityViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/20/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

//let reuseIdentifier = "Cell"

class CityViewController: UICollectionViewController {

    var country: Country?
    var selectedCell: NSIndexPath?
    var cityList = Array<City>()
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let cnty = country {
            navigationTitle.title = cnty.name
            cityList = getCityList(cnty.key)
            // Uncomment the following line to preserve selection between presentations
            // self.clearsSelectionOnViewWillAppear = false

            // Register cell classes
            //self.collectionView!.registerClass(UICollectionViewCell.self, forCellWithReuseIdentifier: reuseIdentifier)

            // Do any additional setup after loading the view.
            let layout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
            layout.sectionInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
            let imagewidth = (self.view.frame.width - 40)/3 - 1
            layout.itemSize = CGSize(width: imagewidth, height: imagewidth)
            collectionView = UICollectionView(frame: self.view.frame, collectionViewLayout: layout)
            collectionView!.dataSource = self
            collectionView!.delegate = self
            collectionView!.registerClass(CityViewCell.self, forCellWithReuseIdentifier: "CityViewCell")
            collectionView!.backgroundColor = UIColor.whiteColor()
            self.view.addSubview(collectionView!)
        }
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
        if segue.identifier == "toCityDetail" {
            if let cityDetailViewController = segue.destinationViewController as? CityDetailViewController {
                cityDetailViewController.city = cityList[selectedCell!.item]
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
        return cityList.count
    }

    override func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        let city = cityList[indexPath.row]
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier("CityViewCell", forIndexPath: indexPath) as! CityViewCell
        //cell.backgroundColor = UIColorFromRGB(0x209624)
        cell.textLabel.text = city.name
        cell.imageView.image = UIImage(named: city.image)
        
        return cell

    }
    
    override func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        selectedCell = indexPath
        var nametext = cityList[indexPath.item].name
        println(nametext)
        performSegueWithIdentifier("toCityDetail", sender: self)
    }

    // MARK: UICollectionViewDelegate

    /*
    // Uncomment this method to specify if the specified item should be highlighted during tracking
    override func collectionView(collectionView: UICollectionView, shouldHighlightItemAtIndexPath indexPath: NSIndexPath) -> Bool {
        return true
    }
    */

    /*
    // Uncomment this method to specify if the specified item should be selected
    override func collectionView(collectionView: UICollectionView, shouldSelectItemAtIndexPath indexPath: NSIndexPath) -> Bool {
        return true
    }
    */

    /*
    // Uncomment these methods to specify if an action menu should be displayed for the specified item, and react to actions performed on the item
    override func collectionView(collectionView: UICollectionView, shouldShowMenuForItemAtIndexPath indexPath: NSIndexPath) -> Bool {
        return false
    }

    override func collectionView(collectionView: UICollectionView, canPerformAction action: Selector, forItemAtIndexPath indexPath: NSIndexPath, withSender sender: AnyObject?) -> Bool {
        return false
    }

    override func collectionView(collectionView: UICollectionView, performAction action: Selector, forItemAtIndexPath indexPath: NSIndexPath, withSender sender: AnyObject?) {
    
    }
    */

}
