//
//  MyLocationViewController.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/27/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit
import MapKit

class MyLocationViewController: UITableViewController {

    var strTime = ""
    var strWeather = ""
    var manager: OneShotLocationManager?
    var location = CLLocationCoordinate2D(latitude: 0.0, longitude: 0.0)

    @IBOutlet weak var txtLatitude: UITextField!
    @IBOutlet weak var txtLongitude: UITextField!
    @IBOutlet weak var lblLocalTime: UILabel!
    @IBOutlet weak var lblLocalWeather: UILabel!
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var activityIndicatorTime: UIActivityIndicatorView!
    @IBOutlet weak var activityIndicatorWeather: UIActivityIndicatorView!
  
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        lblLocalTime.text = ""
        lblLocalWeather.text = ""
        activityIndicatorTime.startAnimating()
        activityIndicatorWeather.startAnimating()
        
        getLocation()
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(animated: Bool) {
        //self.tabBarController?.tabBar.hidden = false
        //self.hidesBottomBarWhenPushed = false
        super.viewWillAppear(animated)
    }
    
    func getLocation() {
        manager = OneShotLocationManager()
        manager!.fetchWithCompletion {loc, error in
            // fetch location or an error
            if let l = loc {
                self.location = l.coordinate
                self.txtLatitude.text = String(format: "%.4f", self.location.latitude)
                self.txtLongitude.text = String(format: "%.4f", self.location.longitude)
                self.setMapView()
            } else if let err = error {
                self.txtLatitude.text = "0.0"
                self.txtLongitude.text = "0.0"
                self.lblLocalTime.text = "[Fail to get the local time!]"
                self.lblLocalWeather.text = "[Fail to get the weather!]"
                self.activityIndicatorTime.stopAnimating()
                self.activityIndicatorWeather.stopAnimating()
                print(err.localizedDescription)
            }
            self.manager = nil
        }
    }
    
    func setMapView() {
        if appSettings.onlyDownloadDataInWifiMode == true && networkStatus != NetworkStatus.Wifi{
            lblLocalTime.text = "[Fail to get the local time!]"
            lblLocalWeather.text = "[Fail to get the weather!]"
            self.activityIndicatorTime.stopAnimating()
            self.activityIndicatorWeather.stopAnimating()
            return
        }
        
        if txtLatitude.text!.isEmpty || txtLongitude.text!.isEmpty {
            let title = "Error"
            let alertController = UIAlertController(title: title, message: "Latitude and Longitude can't be empty!", preferredStyle: .Alert)
            
            // Create the action.
            let cancelAction = UIAlertAction(title: "OK", style: .Cancel, handler: nil)
            alertController.addAction(cancelAction)
            presentViewController(alertController, animated: true, completion: nil)
            return
        }
        
        let latitude = txtLatitude.text!.doubleValue
        let longitude = txtLongitude.text!.doubleValue
        if latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180 {
            let title = "Error"
            let alertController = UIAlertController(title: title, message: "Invalid latitude or longitude! Latitude must be between -90 to +90, and Longitude must be between -180 to +180.", preferredStyle: .Alert)
            
            // Create the action.
            let cancelAction = UIAlertAction(title: "OK", style: .Cancel, handler: nil)
            alertController.addAction(cancelAction)
            presentViewController(alertController, animated: true, completion: nil)
            return
        }
        
        location = CLLocationCoordinate2D(latitude: txtLatitude.text!.doubleValue, longitude: txtLongitude.text!.doubleValue)
            
        //get localtime
        getTimezoneInfo("http://api.geonames.org/timezoneJSON?lat=\(location.latitude)&lng=\(location.longitude)&username=demo")
        
        //get weather
        getWeatherInfo("http://api.openweathermap.org/data/2.5/weather?lat=\(location.latitude)&lon=\(location.longitude)")
        
        let span = MKCoordinateSpanMake(0.75, 0.75)
        let region = MKCoordinateRegion(center: location, span: span)
        mapView.setRegion(region, animated: true)
        
        //3
        let annotation = MKPointAnnotation()
        annotation.coordinate = location
        mapView.addAnnotation(annotation)
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
                self.setLabels(data!)
            })
        }
        
        task.resume()
    }
    
    func setLabels(weatherData: NSData) {
        var jsonError: NSError?
        
        do {
            let json: AnyObject = try NSJSONSerialization.JSONObjectWithData(weatherData, options: [])
            if let dict = json as? NSDictionary {
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
                
                activityIndicatorWeather.stopAnimating()
                lblLocalWeather.text = formatWeather(temp, humidity: humidity)
            } else {
                print("not a dictionary")
                lblLocalWeather.text = "[Fail to get the weather!]"
                self.activityIndicatorWeather.stopAnimating()
                return
            }
        } catch let error as NSError {
            jsonError = error
            print("Could not parse JSON: \(jsonError!)")
            lblLocalWeather.text = "[Fail to get the weather!]"
            self.activityIndicatorWeather.stopAnimating()
            return
        }
    }
    
    func formatWeather(temp: Double, humidity: Double) -> String{
        let celsius = temp - 273.15
        let fahrenheit = celsius * 1.8 + 32
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
                self.setLocalTimeLabel(data!)
            })
        }
        
        task.resume()
        
    }
    
    func setLocalTimeLabel(timeData: NSData) {
        var jsonError: NSError?
        
        do {
            let json: AnyObject = try NSJSONSerialization.JSONObjectWithData(timeData, options: [])
            if let dict = json as? NSDictionary {
                var timezoneid: String = ""
                var time: String = ""
                
                if let timezoneidvalue = json["timezoneId"] as? String {
                    timezoneid = timezoneidvalue
                }
                if let timevalue = json["time"] as? String {
                    time = timevalue
                }
                
                if !timezoneid.isEmpty && !time.isEmpty {
                    activityIndicatorTime.stopAnimating()
                    lblLocalTime.text = convertDateTime(timezoneid, time: time)
                }
                else {
                    lblLocalTime.text = "[Fail to get the local time!]"
                    self.activityIndicatorTime.stopAnimating()
                }
                
                
            } else {
                print("not a dictionary")
                lblLocalTime.text = "[Fail to get the local time!]"
                self.activityIndicatorTime.stopAnimating()
                return
            }
        } catch let error as NSError {
            jsonError = error
            print("Could not parse JSON: \(jsonError!)")
            lblLocalTime.text = "[Fail to get the local time!]"
            self.activityIndicatorTime.stopAnimating()
            return
        }
    }

    @IBAction func getCurrentLocation(sender: UIButton) {
        txtLatitude.resignFirstResponder()
        txtLongitude.resignFirstResponder()
        getLocation()
    }
    @IBAction func updateLocation(sender: UIButton) {
        txtLatitude.resignFirstResponder()
        txtLongitude.resignFirstResponder()
        setMapView()
    }
    @IBAction func editEnded(sender: UITextField) {
        sender.resignFirstResponder()
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
    }
    */

    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath) as! UITableViewCell

        // Configure the cell...

        return cell
    }
    */

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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using [segue destinationViewController].
        // Pass the selected object to the new view controller.
    }
    */

}
