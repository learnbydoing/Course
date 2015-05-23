//
//  Data.swift
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

//continents, image size: 80x80
var continents = [
    Continent(key: "C1", name: "Asia", locations: 8, image: "Asia"),
    Continent(key: "C2", name: "Europe", locations: 0, image: "Europe"),
    Continent(key: "C3", name: "Oceania", locations: 0, image: "Oceania"),
    Continent(key: "C4", name: "Africa", locations: 0, image: "Africa"),
    Continent(key: "C5", name: "North America", locations: 4, image: "NorthAmerica"),
    Continent(key: "C6", name: "South America", locations: 0, image: "SouthAmerica"),
    Continent(key: "C7", name: "Antarctica", locations: 0, image: "Antarctica"),
]

class Continent {
    var key: String = ""
    var name: String = ""
    var locations: Int = 0
    var image: String = ""
    
    init () {
        
    }

    init(key: String, name: String, locations: Int, image: String) {
        self.key = key
        self.name = name
        self.locations = locations
        self.image = image
    }
}

//countries, image size: 300x300
var countries = [
    //Asia
    Country(continent: "C1", key: "CNTY11", name: "China", locations: 5, image: "China"),
    Country(continent: "C1", key: "CNTY12", name: "Japan", locations: 0, image: "Japan"),
    Country(continent: "C1", key: "CNTY13", name: "South Korea", locations: 0, image: "SouthKorea"),
    Country(continent: "C1", key: "CNTY14", name: "Singapore", locations: 0, image: "Singapore"),
    Country(continent: "C1", key: "CNTY15", name: "Thailand", locations: 0, image: "Thailand"),
    Country(continent: "C1", key: "CNTY16", name: "India", locations: 0, image: "India"),
    Country(continent: "C1", key: "CNTY17", name: "Mongolia", locations: 0, image: "Mongolia"),
    Country(continent: "C1", key: "CNTY18", name: "Saudi Arabia", locations: 0, image: "SaudiArabia"),
    //North America
    Country(continent: "C5", key: "CNTY51", name: "United States", locations: 5, image: "UnitedStates"),
    Country(continent: "C5", key: "CNTY52", name: "Canada", locations: 0, image: "Canada"),
    Country(continent: "C5", key: "CNTY53", name: "Mexico", locations: 0, image: "Mexico"),
    Country(continent: "C5", key: "CNTY54", name: "Cuba", locations: 0, image: "Cuba"),
]

class Country {
    var continent: String = ""
    var key: String = ""
    var name: String = ""
    var locations: Int = 0
    var image: String = ""
    
    init () {
        
    }
    
    init(continent: String, key: String, name: String, locations: Int, image: String) {
        self.continent = continent
        self.key = key
        self.name = name
        self.locations = locations
        self.image = image
    }
}

func getCountryList(continent: String) -> Array<Country> {
    if continent.isEmpty {
        return Array()
    }
    else {
        var list = Array<Country>()
        for country in countries {
            if country.continent == continent {
                list.append(country)
            }
        }
        return list
    }
}

//cities, image size: 300x300
var cities = [
    //China
    City(country: "CNTY11", key:"CITY111", name: "Shanghai", image: "Shanghai", area: "2,448 mi² (6,340 km²)", founded: "1291", localtime: "Sunday 3:18 AM", weather: "70°F (21°C), 83% Humidity", population: "14.35 million (2000)", title: "Capital of China", description: "Enormous Shanghai, on China’s central coast, is the country's biggest city and a global financial hub. Its heart is the Bund, a famed waterfront promenade lined with colonial-era buildings. Across the Huangpu River rises Pudong’s futuristic skyline, including 632m Shanghai Tower and the Oriental Pearl TV Tower, with distinctive pink spheres. Sprawling Yuyuan Garden has traditional pavilions, towers and ponds."),
    City(country: "CNTY11", key:"CITY112", name: "Beijing", image: "Beijing", area: "6,487 mi² (16,801 km²)", founded: "907", localtime: "Sunday 3:18 AM", weather: "68°F (20°C), 68% Humidity", population: "11.51 million (2000)", title: "City in China", description: "Beijing, China’s massive capital, has history stretching back 3 millennia. Yet it’s known as much for its modern architecture as its ancient sites such as the grand Forbidden City complex, the imperial palace during the Ming and Qing dynasties. Nearby, the massive Tiananmen Square pedestrian plaza is site of Mao Zedong’s mausoleum and the National Museum of China, displaying a vast collection of cultural relics."),
    City(country: "CNTY11", key:"CITY113", name: "Hangzhou", image: "Hangzhou", area: "5,362 mi² (13,887 km²)", founded: "220BC", localtime: "Sunday 3:18 AM", weather: "70°F (21°C), 78% Humidity", population: "2.451 million (2000)", title: "City in China", description: "Hangzhou, formerly romanised as Hangchow, is the capital and largest city of Zhejiang Province in Eastern China. It sits at the head of Hangzhou Bay on China's coast between Shanghai and Ningbo."),
    City(country: "CNTY11", key:"CITY114", name: "Sanya", image: "Sanya", area: "740 mi² (1,919 km²)", founded: "1905", localtime: "Sunday 3:18 AM", weather: "84°F (29°C), 89% Humidity", population: "482,296 (2000)", title: "City in China", description: "Sanya, a city on the southern end of China’s Hainan Island, has several bays with large beach resorts. Yalong Bay is known for upscale hotels, while Wuzhizhou Island and its coral reefs are destinations for scuba diving, surfing and other water sports. At the city's expansive Nanshan Temple complex, a 108m-high Guan Yin bronze statue rises on a man-made island."),
    City(country: "CNTY11", key:"CITY115", name: "Lhasa", image: "Lhasa", area: "11,396 mi² (29,518 km²)", founded: "633", localtime: "Sunday 3:18 AM", weather: "51°F (11°C), 50% Humidity", population: "223,001 (2000)", title: "City in China", description: "Lhasa is a city and administrative capital of the Tibet Autonomous Region of the People's Republic of China."),

    //United States
    City(country: "CNTY51", key:"CITY511", name: "Chicago", image: "Chicago", area: "233 mi² (606 km²)", founded: "1837", localtime: "Saturday 2:47 PM", weather: "77°F (25°C), 28% Humidity", population: "2.719 million (2013)", title: "City in Illinois", description: "Chicago, on Lake Michigan in Illinois, is among the largest cities in the U.S. Famed for its bold architecture, it has a skyline bristling with skyscrapers such as the iconic John Hancock Center, sleek, 1,451-ft. Willis Tower and neo-Gothic Tribune Tower. The city is also renowned for its museums, including the Art Institute and its expansive collections, including noted Impressionist works."),
    City(country: "CNTY51", key:"CITY512", name: "Honolulu", image: "Honolulu", area: "68.42 mi² (177.2 km²)", founded: "1778", localtime: "Saturday 9:52 AM", weather: "78°F (26°C), 54% Humidity", population: "374,658 (2009)", title: "City in Hawaii", description: "Honolulu, on Oahu’s south shore, is capital of Hawaii, and gateway to the U.S. island chain. The Waikiki neighborhood is its center for dining, nightlife and shopping, famed for its iconic crescent beach backed by palms and high-rise hotels, with volcanic Diamond Head looming in the distance. Sites relating to the World War II attack on Pearl Harbor include the USS Arizona Memorial."),
    City(country: "CNTY51", key:"CITY513", name: "Los Angeles", image: "LosAngeles", area: "498 mi² (1,290 km²)", founded: "1781", localtime: "Saturday 12:55 PM", weather: "68°F (20°C), 53% Humidity", population: "3.884 million (2013)", title: "City in California", description: "Los Angeles is a sprawling Southern California city famed as the center of the nation’s film and television industry. Not far from its iconic Hollywood sign, studios such as Paramount Pictures, Universal and Warner Brothers offer behind-the-scenes tours. On Hollywood Boulevard, TCL Chinese Theater displays celebrities’ hand- and footprints, the Walk of Fame honors thousands of luminaries and vendors sell maps to stars’ homes."),
    City(country: "CNTY51", key:"CITY514", name: "New York", image: "NewYork", area: "469 mi² (1,214 km²)", founded: "1492", localtime: "Saturday 4:00 PM", weather: "67°F (19°C), 21% Humidity", population: "8.406 million (2013)", title: "New York City", description: "Home to the Empire State Building, Times Square, Statue of Liberty and other iconic sites, New York City is a fast-paced, globally influential center of art, culture, fashion and finance. The city’s 5 boroughs sit where the Hudson River meets the Atlantic Ocean, with the island borough of Manhattan at the \"Big Apple's\" core."),
    City(country: "CNTY51", key:"CITY515", name: "Las Vegas", image: "LasVegas", area: "131 mi² (340 km²)", founded: "1905", localtime: "Saturday 1:03 PM", weather: "76°F (24°C), 27% Humidity", population: "603,488 (2013)", title: "City in Nevada", description: "Las Vegas, in Nevada’s Mojave Desert, is a resort town famed for its buzzing energy, 24-hour casinos and endless entertainment options. Its focal point is the Strip, just over 4 miles long and lined with elaborate theme hotels such as the pyramid-shaped Luxor and the Venetian, complete with Grand Canal; luxury resorts including the Bellagio, set behind iconic dancing fountains; and innumerable casinos."),
]

class City {
    var country: String = ""
    var key: String = ""
    var name: String = ""
    var image: String = ""
    var area: String = ""
    var founded: String = ""
    var localtime: String = ""
    var weather: String = ""
    var population: String = ""
    var title: String = ""
    var description: String = ""
    
    init () {
        
    }
    
    init(country: String, key: String, name: String, image: String, area: String, founded: String, localtime: String, weather: String, population: String, title: String, description: String) {
        self.country = country
        self.key = key
        self.name = name
        self.image = image
        self.area = area
        self.founded = founded
        self.localtime = localtime
        self.weather = weather
        self.population = population
        self.title = title
        self.description = description
    }
}

func getCityList(country: String) -> Array<City> {
    if country.isEmpty {
        return Array()
    }
    else {
        var list = Array<City>()
        for city in cities {
            if city.country == country {
                list.append(city)
            }
        }
        return list
    }
}