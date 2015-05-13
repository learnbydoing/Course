//
//  AppDelegate.swift
//  RZHUANG.iOS.RemoteController
//
//  Created by Johnny on 5/12/15.
//  Copyright (c) 2015 CDM of DePaul University. All rights reserved.
//

import UIKit

var arrayChannels = Array<Channel>()

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?    

    
    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
        // Override point for customization after application launch.
      
        var channel1 = Channel();
        channel1.Id = "1"
        channel1.Title = "ABC"
        channel1.Number = 33
        arrayChannels.append(channel1)
        var channel2 = Channel();
        channel2.Id = "2"
        channel2.Title = "NBC"
        channel2.Number = 44
        arrayChannels.append(channel2)
        var channel3 = Channel();
        channel3.Id = "3"
        channel3.Title = "CBS"
        channel3.Number = 55
        arrayChannels.append(channel3)
        var channel4 = Channel();
        channel4.Id = "4"
        channel4.Title = "FOX"
        channel4.Number = 66
        arrayChannels.append(channel4)
        
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

