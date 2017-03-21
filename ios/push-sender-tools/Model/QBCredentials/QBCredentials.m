//
//  QBCredentials.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBCredentials.h"

// archtype
static NSString * const kQBPlistType = @"plist";

// fields
static NSString * const kQBApplicationID = @"app_id";
static NSString * const kQBAuthKey = @"auth_key";
static NSString * const kQBAuthSecret = @"auth_secret";
static NSString * const kQBAccountKey = @"account_key";
static NSString * const kQBApiEndpoint = @"api_endpoint";
static NSString * const kQBUserLogin = @"user_login";
static NSString * const kQBUserPassword = @"user_password";

@interface QBCredentials ()
{
    NSDictionary *_dict;
}

@end

@implementation QBCredentials

// MARK: Construction

- (instancetype)initWithPlistName:(NSString *)plistName bundle:(NSBundle *)bundle {
    
    self = [super init];
    if (self != nil) {
        
        NSString *pathForResource = [bundle pathForResource:plistName ofType:kQBPlistType];
        if (pathForResource == nil) {
            return nil;
        }
        
        _dict = [[NSDictionary alloc] initWithContentsOfFile:pathForResource];
        if (_dict == nil) {
            return nil;
        }
    }
    
    return self;
}

// MARK: Getters

- (NSUInteger)applicationID {
    return [_dict[kQBApplicationID] unsignedIntegerValue];
}

- (NSString *)authKey {
    return _dict[kQBAuthKey];
}

- (NSString *)authSecret {
    return _dict[kQBAuthSecret];
}

- (NSString *)accountKey {
    return _dict[kQBAccountKey];
}

- (NSString *)apiEndpoint {
    return _dict[kQBApiEndpoint];
}

- (NSString *)userLogin {
    return _dict[kQBUserLogin];
}

- (NSString *)userPassword {
    return _dict[kQBUserPassword];
}

@end
