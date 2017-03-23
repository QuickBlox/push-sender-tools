//
//  QBUniversalPushModel.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBUniversalPushModel.h"

@implementation QBUniversalPushModel

- (NSArray *)model {
    static NSArray *model = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        model = @[
                  @"message",
                  @"sound",
                  @"ios_sound",
                  @"ios_badge",
                  @"category_or_click_action",
                  @"ios_category",
                  @"ios_content_available",
                  @"body_loc_key",
                  @"body_loc_args",
                  @"title",
                  @"title_loc_key",
                  @"title_loc_args",
                  @"ios_action_loc_key",
                  @"ios_thread_id",
                  @"ios_launch_image",
                  @"android_color",
                  @"android_tag",
                  @"android_icon",
                  @"custom_param1",
                  @"custop_param2",
                  ];
    });
    return model;
}

@end
