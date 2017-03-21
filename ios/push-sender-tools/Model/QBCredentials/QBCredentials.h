//
//  QBCredentials.h
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

/**
 *  QBCredentials class interface.
 *  This class represents readable fealds of credentils, stored in credentials plist.
 */
@interface QBCredentials : NSObject

/**
 *  Application ID.
 */
@property (nonatomic, readonly) NSUInteger applicationID;

/**
 *  Authorization key.
 */
@property (nonatomic, readonly, nullable) NSString *authKey;

/**
 *  Authorization secret.
 */
@property (nonatomic, readonly, nullable) NSString *authSecret;

/**
 *  Account key.
 */
@property (nonatomic, readonly, nullable) NSString *accountKey;

/**
 *  Quickblox API Endpoint.
 */
@property (nonatomic, readonly, nullable) NSString *apiEndpoint;

/**
 *  User login.
 */
@property (nonatomic, readonly, nullable) NSString *userLogin;

/**
 *  User password.
 */
@property (nonatomic, readonly, nullable) NSString *userPassword;

// unavailable initializers
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)new NS_UNAVAILABLE;

/**
 *  Init with plist name and plist bundle location.
 *
 *  @param plistName credentials plist name
 *  @param bundle bundle with suggersted plist
 *
 *  @return QBCredentials class instance if everything is correct
 */
- (nullable instancetype)initWithPlistName:(NSString *)plistName bundle:(NSBundle *)bundle;

@end

NS_ASSUME_NONNULL_END
