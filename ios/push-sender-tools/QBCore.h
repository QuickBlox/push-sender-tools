//
//  QBCore.h
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import <Foundation/Foundation.h>

@class QBCore;

NS_ASSUME_NONNULL_BEGIN

@protocol QBCoreDelegate <NSObject>
@required
- (void)coreDidLogin:(QBCore *)core;
- (void)core:(QBCore *)core didFailToLoginWithReason:(NSString *)reason;
- (void)coreDidSendPushNotification:(QBCore *)core;
- (void)core:(QBCore *)core didFailToSendPushNotificationWithReason:(NSString *)reason;

@end

typedef NS_ENUM(NSUInteger, QBCoreState) {
    QBCoreStateNone,
    QBCoreStateConnecting,
    QBCoreStateLoggedIn
};

@interface QBCore : NSObject

@property (weak, nonatomic) id<QBCoreDelegate> delegate;

@property (assign, nonatomic) QBCoreState state;

- (void)login;
- (void)registerForRemoteNotifications;
- (void)sendAppleBasedPushNotificationWithPayload:(NSDictionary *)payload;
- (void)sendUniversalPushNotificationWithDict:(NSDictionary *)dict;

@end

NS_ASSUME_NONNULL_END
