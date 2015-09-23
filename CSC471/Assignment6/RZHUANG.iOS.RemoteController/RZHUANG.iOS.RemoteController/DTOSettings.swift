//
//  DTOSettings.swift
//  RZHUANG.iOS.RemoteController
//
//  Created by Johnny on 5/12/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import Foundation

class Channel
{
    private var id: String = ""
    private var title: String = ""
    private var number: Int? = 0
    
    var Id: String {
        get {
            return id
        }
        set(id) {
            self.id = id
        }
    }
    
    var Title: String {
        get {
            return title
        }
        set(title) {
            self.title = title
        }
    }
    
    var Number: Int? {
        get {
            return number
        }
        set(number) {
            self.number = number
        }
    }
    
    /*init (_ id: String) {
        channelid = id
    }*/
}

