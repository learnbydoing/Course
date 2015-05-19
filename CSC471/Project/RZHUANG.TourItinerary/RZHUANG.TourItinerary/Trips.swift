//
//  Trips.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/14/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import Foundation

var sights1: [String] = ["Cloud Gate", "Shedd Aquarium", "Skydeck", "Field Museum", "Adler Planetarium"]

var trips = [
    Trip(destination: "Chicago", country: "USA", from: NSDate(), to: NSDate(), flight1: "AA999", flight2: "AA130", hotel: "Hilton Chicago O'Hare Airport",sights: sights1, note: "Summer will be better 1"),
    Trip(destination: "New York", country: "USA", from: NSDate(), to: NSDate(), flight1: "AA999", flight2: "AA130", hotel: "Hilton Chicago O'Hare Airport",sights: sights1, note: "Summer will be better 2"),
    Trip(destination: "Shanghai", country: "China", from: NSDate(), to: NSDate(), flight1: "AA999", flight2: "AA130", hotel: "Hilton Chicago O'Hare Airport",sights: sights1, note: "Summer will be better 3"),
    Trip(destination: "Sydney", country: "Australia", from: NSDate(), to: NSDate(), flight1: "AA999", flight2: "AA130", hotel: "Hilton Chicago O'Hare Airport",sights: sights1, note: "Summer will be better 4"),
    Trip(destination: "London", country: "Britain", from: NSDate(), to: NSDate(), flight1: "AA999", flight2: "AA130", hotel: "Hilton Chicago O'Hare Airport",sights: sights1, note: "Summer will be better 5"),
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
    var destination: String = ""
    var country: String = ""
    var from = NSDate()
    var to = NSDate()
    var flight1: String = ""
    var flight2: String = ""
    var hotel: String = ""
    var sights: [String] = ["Cloud Gate", "Shedd Aquarium", "Skydeck", "Field Museum", "Adler Planetarium"]
    var note: String = ""
    
    init() {
    }

    init(destination: String, country: String, from: NSDate, to: NSDate, flight1: String, flight2: String, hotel:String, sights:[String], note: String) {
        self.destination = destination
        self.country = country
        self.from = from
        self.to = to
        self.flight1 = flight1
        self.flight2 = flight2
        self.hotel = hotel
        self.sights = sights
        self.note = note
    }
}

//continents
var continents = [
    Continent(name: "Asia", locations: 45, image: "Asia80x80"),
    Continent(name: "Europe", locations: 51, image: "Europe80x80"),
    Continent(name: "Oceania", locations: 17, image: "Oceania80x80"),
    Continent(name: "Africa", locations: 55, image: "Africa80x80"),
    Continent(name: "North America", locations: 23, image: "NorthAmerica80x80"),
    Continent(name: "South America", locations: 12, image: "SouthAmerica80x80"),
    Continent(name: "Antarctica", locations: 0, image: "Antarctica80x80"),
]

class Continent {
    var name: String = ""
    var locations: Int = 0
    var image: String = ""
    
    init () {
        
    }

    init(name: String, locations: Int, image: String) {
        self.name = name
        self.locations = locations
        self.image = image
    }
}

//continents
var countries = [
    Country(name: "China", locations: 45, image: "China"),
    Country(name: "Japan", locations: 51, image: "Japan"),
    Country(name: "South Korea", locations: 17, image: "SouthKorea"),
    Country(name: "Singapore", locations: 55, image: "Singapore"),
    Country(name: "Thailand", locations: 23, image: "Thailand"),
    Country(name: "India", locations: 12, image: "India"),
    Country(name: "Mongolia", locations: 0, image: "Mongolia"),
    Country(name: "Saudi Arabia", locations: 0, image: "Saudi Arabia"),
]

class Country {
    var name: String = ""
    var locations: Int = 0
    var image: String = ""
    
    init () {
        
    }
    
    init(name: String, locations: Int, image: String) {
        self.name = name
        self.locations = locations
        self.image = image
    }
}