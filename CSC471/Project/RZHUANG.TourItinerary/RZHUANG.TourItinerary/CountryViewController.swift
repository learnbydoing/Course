//
//  CountryViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/19/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

//let reuseIdentifier = "Cell"

class CountryViewController: UICollectionViewController {

    var continent: Continent?
    var selectedCell: NSIndexPath?
    var countryList = Array<Country>()
    var cellwidth: CGFloat = 0.0
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if let cnt = continent {
            navigationTitle.title = cnt.name
            countryList = getCountryList(cnt.key)

            // Uncomment the following line to preserve selection between presentations
            // self.clearsSelectionOnViewWillAppear = false

            // Register cell classes
            //self.collectionView!.registerClass(UICollectionViewCell.self, forCellWithReuseIdentifier: reuseIdentifier)

            // Do any additional setup after loading the view.
            let layout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
            layout.sectionInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
            cellwidth = (self.view.frame.width - 40)/3 - 1
            layout.itemSize = CGSize(width: cellwidth, height: cellwidth)
            collectionView = UICollectionView(frame: self.view.frame, collectionViewLayout: layout)
            //collectionView!.dataSource = self
            //collectionView!.delegate = self
            collectionView!.registerClass(CountryViewCell.self, forCellWithReuseIdentifier: "CountryViewCell")
            collectionView!.backgroundColor = UIColor.whiteColor()
            self.view.addSubview(collectionView!)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    /*
    override func viewWillAppear(animated: Bool) {
        
        super.viewWillAppear(animated)
        
        let layout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        layout.sectionInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
        let imagewidth = (self.view.frame.width - 40)/3 - 1
        layout.itemSize = CGSize(width: imagewidth, height: imagewidth)
        collectionView = UICollectionView(frame: self.view.frame, collectionViewLayout: layout)
        //collectionView!.dataSource = self
        //collectionView!.delegate = self
        collectionView!.registerClass(CountryViewCell.self, forCellWithReuseIdentifier: "CountryViewCell")
        collectionView!.backgroundColor = UIColor.whiteColor()
        self.view.addSubview(collectionView!)

    }*/
    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
        if segue.identifier == "toCity" {
            if let cityViewController = segue.destinationViewController as? CityViewController {
                //let cell = sender as! UICollectionViewCell
                //if let indexPath = self.collectionView!.indexPathForCell(cell) {
                cityViewController.country = countryList[selectedCell!.item]
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
        
        return countryList.count
    }

    override func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell {
        //let cell = collectionView.dequeueReusableCellWithReuseIdentifier(reuseIdentifier, forIndexPath: indexPath) as! UICollectionViewCell
    
        // Configure the cell
    
        //return cell
        let country = countryList[indexPath.row]
        let cell = collectionView.dequeueReusableCellWithReuseIdentifier("CountryViewCell", forIndexPath: indexPath) as! CountryViewCell
        //cell.backgroundColor = UIColorFromRGB(0x209624)
        cell.txtName.text = country.name
        cell.txtDetails.text = "\(country.locations)"
        
        //cell.textLabel.alignmentRectForFrame(CGRect(x: 0, y: 0, width: cell.frame.size.width/3, height: cell.frame.size.height*2/3))
        //cell.detailTextLabel?.text = "\(continent.locations) countries or areas"
        cell.imageView.image = UIImage(named: country.image)
        //println("cell.width=\(cell.frame.size.width)")
        //println("cell.height=\(cell.frame.size.height)")
        //cell.updateLayout(cell.frame)
        return cell
    }
    
    override func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath) {
        selectedCell = indexPath
        //var nametext = countryList[indexPath.item].name
        //println(nametext)
        performSegueWithIdentifier("toCity", sender: self)
    }
/*
    override func viewWillTransitionToSize(size: CGSize, withTransitionCoordinator coordinator: UIViewControllerTransitionCoordinator) {
        coordinator.animateAlongsideTransition(nil) { context in
            //self.resizePhotosCollectionView()
            self.view.setNeedsLayout()
            self.view.layoutIfNeeded()
        }
        super.viewWillTransitionToSize(size, withTransitionCoordinator: coordinator)
    }*/
    /*
    override func viewWillTransitionToSize(size: CGSize, withTransitionCoordinator coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransitionToSize(size, withTransitionCoordinator: coordinator)
        
        println("width=\(size.width)")
        println("height=\(size.height)")
        if let layout = collectionView?.collectionViewLayout as? UICollectionViewFlowLayout {            
            let imagewidth = (size.width - 40)/3 - 1
            layout.itemSize = CGSize(width: imagewidth, height: imagewidth)
            layout.invalidateLayout()
            collectionView?.reloadInputViews()
        }
    }*/
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
