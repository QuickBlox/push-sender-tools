//
//  QBAppDelegate.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBAppDelegate.h"

#import "QBCredentials.h"
#import "SVProgressHUD.h"

#import <Quickblox/Quickblox.h>

@implementation QBAppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // initial credentials setup
    _credentials = [[QBCredentials alloc] initWithPlistName:@"Credentials" bundle:[NSBundle mainBundle]];
    
    // Quickblox app setup
    [QBSettings setApplicationID:_credentials.applicationID];
    [QBSettings setAuthKey:_credentials.authKey];
    [QBSettings setAuthSecret:_credentials.authSecret];
    [QBSettings setAccountKey:_credentials.accountKey];
    [QBSettings setApiEndpoint:_credentials.apiEndpoint
                  chatEndpoint:nil
                forServiceZone:QBConnectionZoneTypeProduction];
    [QBSettings setServiceZone:QBConnectionZoneTypeProduction];
    
    return YES;
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    NSString *deviceIdentifier = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
    
    QBMSubscription *subscription = [QBMSubscription subscription];
    subscription.notificationChannel = QBMNotificationChannelAPNS;
    subscription.deviceUDID = deviceIdentifier;
    subscription.deviceToken = deviceToken;
    
    [QBRequest createSubscription:subscription successBlock:^(QBResponse *response, NSArray *objects) {
        
    } errorBlock:^(QBResponse *response) {
        [SVProgressHUD showErrorWithStatus:[NSString stringWithFormat:@"%@", response.error.reasons]];
    }];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    NSLog(@"didReceiveRemoteNotification userInfo=%@", userInfo);
    
    // Get push alert
    [self postPushNotificationWithUserInfo:userInfo];
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    NSLog(@"didReceiveRemoteNotification userInfo=%@ completionHandler", userInfo);
    [self postPushNotificationWithUserInfo:userInfo];
    
    completionHandler(UIBackgroundFetchResultNoData);
}

- (void)postPushNotificationWithUserInfo:(NSDictionary *)userInfo {
    [[NSNotificationCenter defaultCenter] postNotificationName:@"kPushDidReceive" object:nil userInfo:userInfo];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
    [SVProgressHUD showErrorWithStatus:error.localizedDescription];
}

@end
