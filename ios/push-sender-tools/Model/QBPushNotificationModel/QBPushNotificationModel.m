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
        NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
        _pushNotificationDictionary = [[defaults objectForKey:NSStringFromClass([self class])] mutableCopy];
        if (_pushNotificationDictionary == nil) {
            _pushNotificationDictionary = [[NSMutableDictionary alloc] init];
        }
    }
    return self;
}

// MARK: Model

- (NSArray *)model {
    NSAssert(nil, @"Required to be implemented by subclass.");
    return nil;
}

// MARK: Public

- (void)save {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:_pushNotificationDictionary forKey:NSStringFromClass([self class])];
    [defaults synchronize];
}

@end
