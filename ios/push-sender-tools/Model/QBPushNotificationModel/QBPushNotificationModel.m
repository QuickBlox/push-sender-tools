//
//  QBPushNotificationModel.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBPushNotificationModel.h"

@implementation QBPushNotificationModel

// MARK: Construction

- (instancetype)init {
    self = [super init];
    if (self != nil) {
        _pushNotificationDictionary = [[NSMutableDictionary alloc] init];
    }
    return self;
}

// MARK: Model

- (NSArray *)model {
    NSAssert(nil, @"Required to be implemented by subclass.");
    return nil;
}

@end
