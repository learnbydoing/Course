//
//  AppDelegate.swift
//  RZHUANG.TourItinerary
//
//  Created by Johnny on 5/13/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

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

func convertDate(datestr: String?) -> NSDate {
    if let str = datestr {
        var dateFormatter = NSDateFormatter()
        dateFormatter.dateFormat = "YYYY MMMM dd' 'hh:mm a'"
        dateFormatter.locale = NSLocale(localeIdentifier: "en_US")
        var date = dateFormatter.dateFromString(str)
        return date!
    }
    else {
        return NSDate()
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

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?


    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
        // Override point for customization after application launch.
        return true
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
    }


}

