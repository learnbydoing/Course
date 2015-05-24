//
//  CityDetailViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/20/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit
import MapKit

class CityDetailViewController: UITableViewController {

    var city: City?
    @IBOutlet weak var navigationTitle: UINavigationItem!
    
    @IBOutlet weak var imageCity: UIImageView!
    @IBOutlet weak var lblTitle: UILabel!
    //@IBOutlet weak var txtviewOthers: UITextView!
    @IBOutlet weak var lblDescription: UILabel!
 
    @IBOutlet weak var lblArea: UILabel!
    @IBOutlet weak var lblFounded: UILabel!
    @IBOutlet weak var lblLocalTime: UILabel!
    @IBOutlet weak var lblWeather: UILabel!
    @IBOutlet weak var lblPopulation: UILabel!
    @IBOutlet weak var mapView: MKMapView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationTitle.title = city?.name
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        if let c = city {
            imageCity.image = UIImage(named: c.image)
            lblTitle.text = c.title
            //txtviewDescription.text = c.description
            lblDescription.numberOfLines = 0;
            lblDescription.text = c.description
            self.lblDescription.sizeToFit()
             //var frame = txtviewDescription.frame;
            //frame.size.height = txtviewDescription.contentSize.height
            //txtviewDescription.frame = frame
            /*txtviewOthers.text = "Area: " + "\(c.area)" + "\n"
                                + "Founded: " + "\(c.founded)" + "\n"
           + "Local time: " + "\(c.localtime)" + "\n"
            + "Weather: " + "\(c.weather)" + "\n"
            + "Population: " + "\(c.population)"*/
            lblArea.text = c.area
            lblFounded.text = c.founded
            //lblLocalTime.text = c.localtime
            //lblWeather.text = c.weather
            lblPopulation.text = c.population
            
            // 1
            /*let location = CLLocationCoordinate2D(
                latitude: 51.50007773,
                longitude: -0.1246402
            )*/
            let location = CLLocationCoordinate2D(
                latitude: c.latitude,
                longitude: c.longitude
            )
            // 2
            let span = MKCoordinateSpanMake(0.95, 0.95)
            let region = MKCoordinateRegion(center: location, span: span)
            mapView.setRegion(region, animated: true)
            
            //3
            let annotation = MKPointAnnotation()
            annotation.coordinate = location
            annotation.title = "Big Ben"
            annotation.subtitle = "London"
            mapView.addAnnotation(annotation)
            
            //get localtime
            getTimezoneInfo("http://api.geonames.org/timezoneJSON?lat=\(c.latitude)&lng=\(c.longitude)&username=demo")
            
            //get weather
            var escapedParams = c.name.stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.URLHostAllowedCharacterSet())
            if let encodedAddress = escapedParams {
                let urlpath = "http://api.openweathermap.org/data/2.5/weather?q=" + encodedAddress
                getWeatherInfo(urlpath)
            }
            
        }
    }
    
    /*------------------------------------------------------------------------*/
    //Weather API, refer to following links
    //https://www.youtube.com/watch?v=r-LZs0De7_U
    //http://openweathermap.org/current
    //http://openweathermap.org/weather-data#current
    func getWeatherInfo(urlString: String) {
        let url = NSURL(string: urlString)
        let task = NSURLSession.sharedSession().dataTaskWithURL(url!) {
            (data, response, error) in
                dispatch_async(dispatch_get_main_queue(), {
                    self.setLabels(data)
                })
        }
        
        task.resume()
        
    }
    
    func setLabels(weatherData: NSData) {
        var jsonError: NSError?
        
        let json = NSJSONSerialization.JSONObjectWithData(weatherData, options: nil, error: &jsonError) as! NSDictionary
        
        var temp: Double = 0.0
        var humidity: Double = 0.0
        
        if let main = json["main"] as? NSDictionary {
            if let tempvalue = main["temp"] as? Double {
                temp = tempvalue
            }
            if let humidityvalue = main["humidity"] as? Double {
                humidity = humidityvalue
            }
        }
        
        lblWeather.text = formatWeather(temp, humidity: humidity)
    }
    
    func formatWeather(temp: Double, humidity: Double) -> String{
        let celsius = temp - 273.15
        let fahrenheit = celsius * 1.8 + 32
        //let labelstr: String = "\(fahrenheit)°F (\(celsius)°C), \(humidity)% Humidity"
        let labelstr: String = "%.0f°F (%.0f°C), %.0f".format(fahrenheit, celsius, humidity) + "% Humidity"

        return labelstr
    }
    
    /*---------------------------------------------------------------------*/
    //Timezone API, get local time by latitude and longitude
    //http://stackoverflow.com/questions/16086962/how-to-get-a-time-zone-from-a-location-using-latitude-and-longitude-coordinates
    //http://www.geonames.org/export/web-services.html#timezone
    //http://api.geonames.org/timezoneJSON?lat=47.01&lng=10.2&username=demo
    func getTimezoneInfo(urlString: String) {
        let url = NSURL(string: urlString)
        let task = NSURLSession.sharedSession().dataTaskWithURL(url!) {
            (data, response, error) in
            dispatch_async(dispatch_get_main_queue(), {
                self.setLocalTimeLabel(data)
            })
        }
        
        task.resume()
        
    }
    
    func setLocalTimeLabel(timeData: NSData) {
        var jsonError: NSError?
        
        let json = NSJSONSerialization.JSONObjectWithData(timeData, options: nil, error: &jsonError) as! NSDictionary
        
       
        var timezoneid: String = ""
        var time: String = ""
        
        if let timezoneidvalue = json["timezoneId"] as? String {
            timezoneid = timezoneidvalue
        }
        if let timevalue = json["time"] as? String {
            time = timevalue
        }
        
        if !timezoneid.isEmpty && !time.isEmpty {        
            lblLocalTime.text = convertDateTime(timezoneid, time: time)
        }
    }
    
    /*
    func formatDateTime(timezoneid: String, time: String) -> String{
        let dateString = "17:33"
        let formatter = NSDateFormatter()
        formatter.dateFormat = "HH:mm"
        formatter.timeZone = NSTimeZone(name: timezoneid)
        
        let date = formatter.dateFromString(dateString)
        formatter.timeStyle = .ShortStyle
        println("date: \(date!)") // date: 2014-10-09 14:22:00 +0000
        
        formatter.dateFormat = "H:mm"
        formatter.timeStyle = .ShortStyle
        formatter.timeZone = NSTimeZone(name: timezoneid)
        let formattedDateString = formatter.stringFromDate(date!)
        println("formattedDateString: \(formattedDateString)") // formattedDateString: 8:22 AM
        
        return formattedDateString
    }*/
    
    func convertDateTime(timezoneid: String, time: String) -> String {
        //let currenttime = NSDate()
        //var dateff = NSDateFormatter()
        //dateff.dateFormat = "YYYY-MM-dd' 'HH:mm'"
        //let str = dateff.stringFromDate(currenttime)
        var dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "YYYY-MM-dd' 'HH:mm'"
        dateFormatter.timeZone = NSTimeZone(name: timezoneid)
        let date = dateFormatter.dateFromString(time)
        dateFormatter.dateFormat = "hh:mm a"
        let formattedTimeString = dateFormatter.stringFromDate(date!)
        dateFormatter.dateFormat = "EEEE"
        let formattedDayNameString = dateFormatter.stringFromDate(date!)
        return formattedDayNameString + " " + formattedTimeString
    }
    
    

    override func viewDidLayoutSubviews() {
        
        super.viewDidLayoutSubviews()
        
        
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    /*
    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Potentially incomplete method implementation.
        // Return the number of sections.
        return 0
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete method implementation.
        // Return the number of rows in the section.
        return 0
    }*/

    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as! UITableViewCell

        // Configure the cell...

        return cell
    }
    */
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        
        
        if (indexPath.row == 2 && indexPath.section == 0)
        {
            //var height: CGFloat = 0.0
            //println("self.txtviewDescripton.frame.height:" + "\(self.txtviewDescription.frame.height)");
            //println(self.txtviewDescription.text)
            
            //let contentSize = self.txtviewDescription.sizeThatFits(self.txtviewDescription.bounds.size)
            //var frame = self.txtviewDescription.frame
            //frame.size.height = contentSize.height
            //frame.size.height = txtviewDescription.contentSize.height
            //println("frame.size.height:" + "\(frame.size.height)")
            //self.txtviewDescription.frame = frame
            
            //let aspectRatioTextViewConstraint = NSLayoutConstraint(item: self.txtviewDescription, attribute: .Height, relatedBy: .Equal, toItem: self.txtviewDescription, attribute: .Width, multiplier: txtviewDescription.bounds.height/txtviewDescription.bounds.width, constant: 1)
            //self.txtviewDescription.addConstraint(aspectRatioTextViewConstraint)
            
            //var height = self.txtviewDescription.frame.height + 2;
            println("self.lblDescription.frame.height:" + "\(self.lblDescription.frame.height)")
            //lblDescription.numberOfLines = 0;
            //self.lblDescription.sizeToFit()
            println("self.lblDescription.frame.height:" + "\(self.lblDescription.frame.height)")
            return self.lblDescription.frame.height * (2.3)

        }
        else {
            return super.tableView(tableView, heightForRowAtIndexPath: indexPath)
        }
        
        //return super.tableView(tableView, heightForRowAtIndexPath: indexPath)
    }

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return NO if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return NO if you do not want the item to be re-orderable.
        return true
    }
    */

    
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
        if let tripItemViewController = segue.destinationViewController as? TripItemViewController {
            tripItemViewController.city = self.city
            //tripItemViewController.tabBarController?.selectedIndex = 0
        }
    }    

}
