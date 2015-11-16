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
    var isFavorite = false
    var container = UIView()
    var pointsViews = [UIImageView]()
    var indexImage = 0
    var btnSwitch = UIButton()
    
    @IBOutlet weak var navigationTitle: UINavigationItem!
    
    @IBOutlet weak var imageCell: UITableViewCell!
    @IBOutlet weak var imageCity: UIImageView!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var btnFavorite: UIButton!
    @IBOutlet weak var lblArea: UILabel!
    @IBOutlet weak var lblFounded: UILabel!
    @IBOutlet weak var lblLocalTime: UILabel!
    @IBOutlet weak var lblWeather: UILabel!
    @IBOutlet weak var lblPopulation: UILabel!
    @IBOutlet weak var mapView: MKMapView!    
    @IBOutlet weak var pointsCell: UITableViewCell!
    @IBOutlet weak var lblPoint: UILabel!    
    @IBOutlet weak var mapCell: UITableViewCell!
    @IBOutlet weak var descriptionCell: UITableViewCell!
    
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
            lblArea.text = c.area
            lblFounded.text = c.founded
            lblPopulation.text = c.population
            
            let location = CLLocationCoordinate2D(latitude: c.latitude, longitude: c.longitude)
            let span = MKCoordinateSpanMake(0.95, 0.95)
            let region = MKCoordinateRegion(center: location, span: span)
            
            let annotation = MKPointAnnotation()
            annotation.coordinate = location
            
            //set favorite icon
            isFavorite = checkFavorite(c.key)
            if isFavorite {
                let image = UIImage(named: "Me_Favorite") as UIImage!
                btnFavorite.setImage(image, forState: .Normal)
            }
            else {
                let image = UIImage(named: "Me_Favorite_Black") as UIImage!
                btnFavorite.setImage(image, forState: .Normal)
            }
            
            //assign new pictures
            for i in 0..<city!.pointsofinterest.count {
                pointsViews.append(UIImageView(image: UIImage(named: city!.pointsofinterest[i])))
            }
            self.lblPoint.text = self.city!.pointsofinterest[0]
            
            if appSettings.onlyDownloadDataInWifiMode == true && networkStatus != NetworkStatus.Wifi{
                lblLocalTime.text = "[Fail to get the local time!]"
                lblWeather.text = "[Fail to get the weather!]"
                return
            }
            
            //locate position in map
            mapView.setRegion(region, animated: true)
            mapView.addAnnotation(annotation)
            
            //get localtime
            getTimezoneInfo("http://api.geonames.org/timezoneJSON?lat=\(c.latitude)&lng=\(c.longitude)&username=demo")
            
            //get weather
            let escapedParams = c.name.stringByAddingPercentEncodingWithAllowedCharacters(NSCharacterSet.URLHostAllowedCharacterSet())
            if let encodedAddress = escapedParams {
                let urlpath = "http://api.openweathermap.org/data/2.5/weather?q=" + encodedAddress
                getWeatherInfo(urlpath)
            }
            
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func addFavorite(sender: UIButton) {
        if isFavorite {
            removeFavorite(city!.key)
            isFavorite = false;
            let image = UIImage(named: "Me_Favorite_Black") as UIImage!
            btnFavorite.setImage(image, forState: .Normal)
        }
        else {
            addNewFavorite(city!.key)
            isFavorite = true;
            let image = UIImage(named: "Me_Favorite") as UIImage!
            btnFavorite.setImage(image, forState: .Normal)
        }
    }
    
    @IBAction func shareCity(sender: UIBarButtonItem) {
        let textToShare = "Hi, I found a beautiful city in Travel Note. I'd like to share it with you!\n\n\(city!.name):"
        let params = city!.name.stringByAddingPercentEncodingWithAllowedCharacters(.URLHostAllowedCharacterSet())
        if let myWebsite = NSURL(string: "https://www.google.com/?gws_rd=ssl#q=\(params!)")
        {
            let objectsToShare = [textToShare, myWebsite]
            let activityVC = UIActivityViewController(activityItems: objectsToShare, applicationActivities: nil)
            
            self.presentViewController(activityVC, animated: true, completion: nil)
        }
    }
    
    @IBAction func switchImage(sender: UIButton) {
        let transitionOptions = appSettings.transitionOptions
        let current = indexImage
        var next = current + 1
        if next >= 5 {
            next = 0
        }
        
        UIView.transitionWithView(self.container, duration: 1.0, options: transitionOptions, animations: {
            self.pointsViews[current].removeFromSuperview()
            self.container.addSubview(self.pointsViews[next])
            self.lblPoint.text = self.city!.pointsofinterest[next]
            }, completion: nil)
        
        indexImage++
        if indexImage >= 5 {
            indexImage = 0
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
                    if data != nil {
                        self.setLabels(data!)
                    }
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
                    lblWeather.text = formatWeather(temp, humidity: humidity)
                }
                else {
                    lblWeather.text = "[Fail to get the weather!]"
                }
                
                
            } else {
                print("not a dictionary")
                return
            }
        } catch let error as NSError {
            jsonError = error
            print("Could not parse JSON: \(jsonError!)")
            return
        }

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
                if data != nil {
                    self.setLocalTimeLabel(data!)
                }
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
                    var str = convertDateTime(timezoneid, time: time)
                    if str.isEmpty {
                        str = "[Fail to get the local time!]"
                    }
                    lblLocalTime.text = str
                }
                else {
                    lblLocalTime.text = "[Fail to get the local time!]"
                }

            } else {
                print("not a dictionary")
                lblLocalTime.text = "[Fail to get the local time!]"
                return
            }
        } catch var error as NSError {
            jsonError = error
            print("Could not parse JSON: \(jsonError!)")
            lblLocalTime.text = "[Fail to get the local time!]"
            return
        }
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
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            //city name in the left corner of the image
            let cityFrame = CGRect(x: 10, y: imageCity.frame.height-25, width: imageCity.frame.width-10, height: 20)
            let lblCity = UILabel(frame: cityFrame)
            lblCity.font = UIFont.boldSystemFontOfSize(17.0)
            lblCity.textColor = UIColor.whiteColor()
            lblCity.textAlignment = .Left
            lblCity.lineBreakMode = .ByWordWrapping // or NSLineBreakMode.ByWordWrapping
            lblCity.numberOfLines = 0
            lblCity.text = city!.name
            //lblCity.layer.borderColor = UIColor.greenColor().CGColor
            //lblCity.layer.borderWidth = 1.0;
            imageCell.addSubview(lblCity)
           
            return imageCell
        }
        else if indexPath.row == 2 {
            //set city descripton
            descriptionCell.textLabel!.lineBreakMode = .ByWordWrapping
            descriptionCell.textLabel!.numberOfLines = 0;
            descriptionCell.textLabel!.text = city!.description
            descriptionCell.textLabel!.font = UIFont(name: lblTitle.font.familyName, size: 14.0)
            descriptionCell.textLabel!.sizeToFit()
            return descriptionCell
        }
        else if indexPath.row == 3 {
            //Display location info in the left bottom of map view
            let locationFrame = CGRect(x: 4, y: mapView.frame.height-19, width: 175, height: 20)
            let lblLocation = UILabel(frame: locationFrame)
            lblLocation.backgroundColor = UIColor.darkGrayColor()
            lblLocation.font = UIFont.boldSystemFontOfSize(12.0)
            lblLocation.textColor = UIColor.whiteColor()
            lblLocation.textAlignment = .Left
            lblLocation.lineBreakMode = .ByWordWrapping // or NSLineBreakMode.ByWordWrapping
            lblLocation.numberOfLines = 0
            lblLocation.text = String(format: "  Lat: %.4f   Lon: %.4f", city!.latitude, city!.longitude)
            mapCell.addSubview(lblLocation)
            return mapCell
        }
        else if indexPath.row == 5 {
            //create images for the points of interest
            container.frame = CGRect(x: 0, y: 0, width: imageCity.frame.width, height: imageCity.frame.height)
            pointsCell.addSubview(container)
            
            for i in 0..<city!.pointsofinterest.count {
                pointsViews[i].frame = container.frame
                pointsViews[i].contentMode = .ScaleToFill
            }
            container.addSubview(pointsViews[0])
            
            //create a button upon the images to receive the on touch event
            let btnFrame = CGRect(x: 0, y: 0, width: imageCity.frame.width, height: imageCity.frame.height)
            btnSwitch = UIButton(frame: btnFrame)
            btnSwitch.addTarget(self, action: "switchImage:", forControlEvents: .TouchUpInside)
            pointsCell.addSubview(btnSwitch)
            return pointsCell
        }
        else {
            return super.tableView(tableView, cellForRowAtIndexPath: indexPath)
        }
    }

    override func tableView(tableView: UITableView, willDisplayCell cell: UITableViewCell, forRowAtIndexPath indexPath: NSIndexPath) {
        if (indexPath.row == 5)
        {
            //resize the image
            if container.frame.width != cell.frame.width {
                container.frame = CGRect(x: 0, y: 0, width: cell.frame.width, height: cell.frame.height)
                for i in 0..<city!.pointsofinterest.count {
                    pointsViews[i].frame = container.frame
                }
                btnSwitch.frame = container.frame
            }
        }
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        
        if (indexPath.row == 2 && indexPath.section == 0)
        {
            //enlarge the height of cell which shows the description of city
            let label = UILabel(frame: CGRect(x: 0, y: 0, width: tableView.frame.width-20, height: 10000))
            label.text = city!.description
            label.numberOfLines = 100
            label.font = UIFont(name: lblTitle.font.familyName, size: 14.0)
            label.sizeToFit()
            return label.frame.height + 25            //var height: CGFloat = 0.0
        }
        else {
            return super.tableView(tableView, heightForRowAtIndexPath: indexPath)
        }
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

}