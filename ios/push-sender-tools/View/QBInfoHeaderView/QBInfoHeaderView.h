//
//  QBInfoHeaderView.h
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface QBInfoHeaderView : UIView

@property (copy, nonatomic, nullable) NSString *title;

- (void)startActivityIndicator;
- (void)stopActivityIndicator;

@end

NS_ASSUME_NONNULL_END
