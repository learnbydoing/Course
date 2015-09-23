//
//  EntryTabBarController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/20/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

class EntryTabBarController: UITabBarController {
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        self.hidesBottomBarWhenPushed = false
    }
    
    override func viewWillAppear(animated: Bool) {
        super.viewWillAppear(animated)
        
        self.tabBarController?.tabBar.hidden = false
    }
}
