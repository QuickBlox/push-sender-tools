//
//  QBAppDelegate.h
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import <UIKit/UIKit.h>

@class QBCredentials;

@interface QBAppDelegate : UIResponder <UIApplicationDelegate>

/**
 *  Main window.
 */
@property (strong, nonatomic) UIWindow *window;

/**
 *  Quickblox based credentials.
 */
@property (strong, nonatomic) QBCredentials *credentials;

@end

