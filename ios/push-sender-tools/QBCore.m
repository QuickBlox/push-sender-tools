//
//  QBCore.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBCore.h"

#import "QBAppDelegate.h"
#import "QBCredentials.h"

#import <Quickblox/Quickblox.h>

@implementation QBCore

// MARK: Public

- (void)login {
    
    if (self.state != QBCoreStateNone) {
        // already in progress
        return;
    }
    
    QBCredentials *credentials = [(QBAppDelegate *)[UIApplication sharedApplication].delegate credentials];
    
    self.state = QBCoreStateConnecting;
    
    __weak __typeof(self)weakSelf = self;
    [QBRequest logInWithUserLogin:credentials.userLogin password:credentials.userPassword successBlock:^(QBResponse * _Nonnull response, QBUUser * _Nullable user) {
        
        weakSelf.state = QBCoreStateLoggedIn;
        [weakSelf.delegate coreDidLogin:weakSelf];
        
    } errorBlock:^(QBResponse * _Nonnull response) {
        
        weakSelf.state = QBCoreStateNone;
        [weakSelf.delegate core:weakSelf didFailToLoginWithReason:[NSString stringWithFormat:@"%@", response.error.reasons]];
    }];
}

- (void)registerForRemoteNotifications {
    
    UIUserNotificationSettings *settings =
    [UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeSound |
                                                  UIUserNotificationTypeAlert |
                                                  UIUserNotificationTypeBadge)
                                      categories:nil];
    [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
    [[UIApplication sharedApplication] registerForRemoteNotifications];
}

- (void)sendAppleBasedPushNotificationWithPayload:(NSDictionary *)payload {
    NSString *users = [NSString stringWithFormat:@"%tu", [QBSession currentSession].currentUser.ID];
    QBMPushMessage *pushMessage = [[QBMPushMessage alloc] initWithPayload:payload];
    __weak __typeof(self)weakSelf = self;
    [QBRequest sendPush:pushMessage toUsers:users successBlock:^(QBResponse * _Nonnull response, QBMEvent * _Nullable event) {
        
        
    } errorBlock:^(QBError * _Nonnull error) {
        
        [weakSelf.delegate core:weakSelf didFailToSendPushNotificationWithReason:[NSString stringWithFormat:@"%@", error.reasons]];
    }];
}

- (void)sendUniversalPushNotificationWithDict:(NSDictionary *)dict {
    NSString *users = [NSString stringWithFormat:@"%tu", [QBSession currentSession].currentUser.ID];
    QBMEvent *event = [QBMEvent event];
    event.notificationType = QBMNotificationTypePush;
    event.usersIDs = users;
    event.type = QBMEventTypeOneShot;
    
    NSError *error = nil;
    NSData *sendData = [NSJSONSerialization dataWithJSONObject:dict options:NSJSONWritingPrettyPrinted error:&error];
    NSString *jsonString = [[NSString alloc] initWithData:sendData encoding:NSUTF8StringEncoding];
    
    event.message = jsonString;
    
    __weak __typeof(self)weakSelf = self;
    [QBRequest createEvent:event successBlock:^(QBResponse * _Nonnull response, NSArray<QBMEvent *> * _Nullable events) {
        
    } errorBlock:^(QBResponse * _Nonnull response) {
        
        [weakSelf.delegate core:weakSelf didFailToSendPushNotificationWithReason:[NSString stringWithFormat:@"%@", response.error.reasons]];
    }];
}

@end
