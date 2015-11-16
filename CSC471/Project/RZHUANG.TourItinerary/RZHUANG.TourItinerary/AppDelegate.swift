//
//  AppDelegate.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/13/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

/*-----------------------------------------------------*/
//common methods
extension NSDate {
    var formatted: String {
        let formatter = NSDateFormatter()
        //formatter.dateFormat = "yyyy-MM-dd' 'hh:mm a'"
        //formatter.dateFormat = "EEE yyyy MMMM dd' 'hh:mm a'"
        formatter.dateFormat = "YYYY MMMM dd' 'hh:mm a'"
        //formatter.timeZone = NSTimeZone(forSecondsFromGMT: 0)
        //formatter.calendar = NSCalendar(calendarIdentifier: NSCalendarIdentifierISO8601)!
        formatter.locale = NSLocale(localeIdentifier: "en_US")
        return formatter.stringFromDate(self)
    }
}

extension String {
    func format(args: CVarArgType...) -> String {
        return NSString(format: self, arguments: getVaList(args)) as String
    }    
}
extension String {
    var doubleValue: Double {
        return (self as NSString).doubleValue
    }
}

func convertDate(datestr: String?) -> NSDate {
    if let str = datestr {
        let dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "YYYY MMMM dd' 'hh:mm a'"
        dateFormatter.locale = NSLocale(localeIdentifier: "en_US")
        let date = dateFormatter.dateFromString(str)
        return date!
    }
    else {
        return NSDate()
    }
}

func convertDateTime(timezoneid: String, time: String) -> String {
    //println(timezoneid)
    //println(time)
    //let timezoneid2 = "Asia/Shanghai"
    //let time2 = "2015-06-05 04:51"
    let dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = "YYYY-MM-dd HH:mm"
    dateFormatter.timeZone = NSTimeZone(name: timezoneid)
    
    if let date = dateFormatter.dateFromString(time) {
        dateFormatter.dateFormat = "EEE, YYYY MMM dd' 'hh:mm a'"
        return dateFormatter.stringFromDate(date)
    }
    else {
        return ""
    }
}

func UIColorFromRGB(rgbValue: UInt) -> UIColor {
    return UIColor(
        red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
        green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
        blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
        alpha: CGFloat(1.0)
    )
}

func getImage(imagename: String) -> UIImage {
    var image: UIImage? = UIImage(named: imagename)
    if image == nil {
        image = UIImage(named: "City_Default")
    }
    return image!
}

//Network connection status
enum NetworkStatus {
    case NoConnetion
    case Wifi
    case Cellular
}

var networkStatus = NetworkStatus.NoConnetion
var deviceOrientation = false //false: portrait; true: landscape

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    private var reachability:Reachability?;

    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
        // Override point for customization after application launch.
        sleep(1); //hold the launch screen, splash image.
        
        //Global settings
        appSettings.onlyDownloadDataInWifiMode = false

        //check the initial orientation
        handleRotationChanged()
        
        //detect rotation change
        NSNotificationCenter.defaultCenter().addObserver(self, selector: "handleRotationChanged", name: UIDeviceOrientationDidChangeNotification, object: nil)

        //detect network change
        NSNotificationCenter.defaultCenter().addObserver(self, selector:"handleNetworkConnectionNotification:", name: kReachabilityChangedNotification, object: nil);
        
        reachability = Reachability.reachabilityForInternetConnection();
        reachability?.startNotifier();
        if reachability != nil {
            networkStatus = getNetworkStatus(reachability!)
            showPopUp(networkStatus)
        }
        
        return true
    }
    
    func handleRotationChanged()
    {
        if(UIDeviceOrientationIsLandscape(UIDevice.currentDevice().orientation))
        {
            deviceOrientation = true
        }
        
        if(UIDeviceOrientationIsPortrait(UIDevice.currentDevice().orientation))
        {
            deviceOrientation = false
        }
        
    }
    
    //Handle the notification of connetion change
    func handleNetworkConnectionNotification(notification:NSNotification)
    {
        let networkReachability = notification.object as! Reachability;
        networkStatus = getNetworkStatus(networkReachability)
        showPopUp(networkStatus)
    }
    
    //Get the status
    func getNetworkStatus(currentReachability: Reachability) -> NetworkStatus{
        var connectionStatus = NetworkStatus.NoConnetion
        
        let reachablityStatus = currentReachability.currentReachabilityStatus()
        
        if (reachablityStatus.rawValue == NotReachable.rawValue)
        {
            connectionStatus = NetworkStatus.NoConnetion
        }
        else if (reachablityStatus.rawValue == ReachableViaWiFi.rawValue)
        {
            connectionStatus = NetworkStatus.Wifi
        }
        else if (reachablityStatus.rawValue == ReachableViaWWAN.rawValue)
        {
            connectionStatus = NetworkStatus.Cellular
        }
        else {
            connectionStatus = NetworkStatus.NoConnetion
        }
        
        return connectionStatus
    }

    //Show popup if there is no connection
    func showPopUp(status: NetworkStatus) {
        if (status == NetworkStatus.NoConnetion)
        {
            let alertController = UIAlertController(title: "Network unavailable", message: "Your device is not connected to intenet, you can turn on wifi or cellular data in Settings.", preferredStyle: .Alert)
            
            let callActionHandler = { (action:UIAlertAction!) -> Void in
                UIApplication.sharedApplication().openURL(NSURL(string:UIApplicationOpenSettingsURLString)!)
                //UIApplication.sharedApplication().openURL(NSURL(string:"prefs:root=General")!)
            }
            
            // Create the action.
            let settingsAction = UIAlertAction(title: "Settings", style: .Cancel, handler: callActionHandler)
            alertController.addAction(settingsAction)
            let cancelAction = UIAlertAction(title: "OK", style: .Default, handler: nil)
            alertController.addAction(cancelAction)
            self.window?.makeKeyAndVisible()
            self.window?.rootViewController?.presentViewController(alertController, animated: true, completion: nil)
        }
    }

    func applicationWillResignActive(application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(application: UIApplication) {
        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
        NSNotificationCenter.defaultCenter().removeObserver(self, name: kReachabilityChangedNotification, object: nil);
    }


}

