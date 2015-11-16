//
//  CountryViewCell.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/19/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class CountryViewCell: UICollectionViewCell {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    var txtName: UILabel = UILabel()
    var txtDetails: UILabel = UILabel()
    var imageView: UIImageView = UIImageView()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: frame.size.width, height: frame.size.height))
        imageView.contentMode = UIViewContentMode.ScaleAspectFit
        contentView.addSubview(imageView)
        
        //Country Name
        let nameFrame = CGRect(x: 5, y: frame.size.height-20, width: frame.size.width, height: 20)
        txtName = UILabel(frame: nameFrame)
        txtName.font = UIFont.boldSystemFontOfSize(12.0)
        txtName.textColor = UIColor.whiteColor()
        txtName.textAlignment = .Left
        contentView.addSubview(txtName)
        
        //Count of cities with this country
        let detailsFrame = CGRect(x: frame.size.width-10, y: frame.size.height-20, width: frame.size.width, height: 20)
        txtDetails = UILabel(frame: detailsFrame)
        txtDetails.font = UIFont.boldSystemFontOfSize(12.0)
        txtDetails.textColor = UIColor.whiteColor()
        txtDetails.textAlignment = .Left
        contentView.addSubview(txtDetails)
    }
    
    /*
    func updateLayout(frame: CGRect) {
        imageView.frame = CGRect(x: 0, y: 0, width: frame.size.width, height: frame.size.height)
        txtName.frame = CGRect(x: 5, y: frame.size.height-20, width: frame.size.width, height: 20)
        txtDetails.frame = CGRect(x: frame.size.width-10, y: frame.size.height-20, width: frame.size.width, height: 20)
    }*/
}
