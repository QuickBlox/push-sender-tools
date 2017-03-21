//
//  QBPushTableDataSource.h
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@class QBPushNotificationModel;

NS_ASSUME_NONNULL_BEGIN

@interface QBPushTableDataSource : NSObject <UITableViewDataSource>

@property (strong, nonatomic) __kindof QBPushNotificationModel *model;

// unavailable initializers
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)new NS_UNAVAILABLE;

- (instancetype)initWithModel:(__kindof QBPushNotificationModel *)model;

@end

NS_ASSUME_NONNULL_END
