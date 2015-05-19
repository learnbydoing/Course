//
//  CountryViewCell.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/19/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class CountryViewCell: UICollectionViewCell {
    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    var textLabel: UILabel = UILabel()
    var imageView: UIImageView = UIImageView()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        imageView = UIImageView(frame: CGRect(x: 60, y: 10, width: frame.size.width/2, height: frame.size.height*1/3))
        imageView.contentMode = UIViewContentMode.ScaleAspectFit
        contentView.addSubview(imageView)
        
        let textFrame = CGRect(x: 10, y: frame.size.height-70, width: frame.size.width, height: frame.size.height)
        textLabel = UILabel(frame: textFrame)
        textLabel.font = UIFont.systemFontOfSize(UIFont.smallSystemFontSize())
        textLabel.textAlignment = .Left
        //textLabel.sizeToFit()
        contentView.addSubview(textLabel)
    }
}
