//
//  QBPushNotificationModel.h
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface QBPushNotificationModel : NSObject

@property (strong, nonatomic) NSMutableDictionary *pushNotificationDictionary;

- (NSArray *)model;
// perform save to user defaults
// will always load in init
- (void)save;

@end
