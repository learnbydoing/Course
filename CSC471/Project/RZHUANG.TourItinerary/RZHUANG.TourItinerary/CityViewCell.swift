//
//  CityViewCell.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/20/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class CityViewCell: UICollectionViewCell {
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
    var textLabel: UILabel = UILabel()
    var imageView: UIImageView = UIImageView()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: frame.size.width, height: frame.size.height))
        imageView.contentMode = UIViewContentMode.ScaleAspectFit
        contentView.addSubview(imageView)
        
        let textFrame = CGRect(x: 5, y: frame.size.height-20, width: frame.size.width, height: 20)
        textLabel = UILabel(frame: textFrame)
        textLabel.font = UIFont.boldSystemFontOfSize(12.0)
        textLabel.textColor = UIColor.whiteColor()
        textLabel.textAlignment = .Left
        contentView.addSubview(textLabel)
    }
}
