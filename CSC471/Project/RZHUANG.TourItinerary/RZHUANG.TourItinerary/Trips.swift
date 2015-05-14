//
//  Trips.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/14/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import Foundation

let trips = [
    Trip(destination: "Chicago", from: NSDate(), to: NSDate()),
    Trip(destination: "New York", from: NSDate(), to: NSDate()),
    Trip(destination: "Shanghai", from: NSDate(), to: NSDate()),
    Trip(destination: "Sydney", from: NSDate(), to: NSDate()),
    Trip(destination: "London", from: NSDate(), to: NSDate()),   
]

class Trip {
    /*
    enum Type: String {
        case Red = "red"
        case White = "white"
        case Rosé = "rose"
        case Sparkling = "sparkling"
    }*/
    /*
    let date = NSDate()
    let calendar = NSCalendar.currentCalendar()
    let components = calendar.components(.CalendarUnitHour | .CalendarUnitMinute, fromDate: date)
    let hour = components.hour
    let minutes = components.minute*/
    
    var destination: String
    var from = NSDate()
    var to = NSDate()

    
    init(destination: String, from: NSDate, to: NSDate) {
        self.destination = destination
        self.from = from
        self.to = to
    }
    
}