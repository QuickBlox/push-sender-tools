//
//  QBAppleBasedPushModel.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBAppleBasedPushModel.h"

@implementation QBAppleBasedPushModel

// MARK: Overrides

- (NSArray *)model {
    static NSArray *model = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        model = @[
                  @"aps.alert.title",
                  @"aps.alert.body",
                  @"aps.alert.title-loc-key",
                  @"aps.alert.title-loc-args",
                  @"aps.alert.action-loc-key",
                  @"aps.alert.loc-key",
                  @"aps.alert.loc-args",
                  @"aps.alert.launch-image",
                  @"aps.badge",
                  @"aps.sound",
                  @"aps.content-available",
                  @"aps.category",
                  @"aps.thread-id",
                  @"aps.mutable-content",
                  @"custom_param1",
                  @"custom_param2",
                  ];
    });
    return model;
}

// MARK: Public

- (NSDictionary *)payload {
    NSMutableDictionary *payload = [[NSMutableDictionary alloc] init];
    [self.pushNotificationDictionary enumerateKeysAndObjectsUsingBlock:^(NSString *key, NSString *obj, BOOL * _Nonnull stop) {
        
        NSArray *comp = [key componentsSeparatedByString:@"."];
        if (comp.count > 1) {
            // creating full dict
            NSDictionary *dict = @{comp.lastObject : obj};
            for (NSInteger i = comp.count - 2; i >= 0; --i) {
                dict = @{comp[i] : dict};
            }
            // merging dict into payload
            NSDictionary *mergeDict = payload[comp.firstObject];
            if (mergeDict == nil) {
                [payload addEntriesFromDictionary:dict];
            }
            else {
                payload[comp.firstObject] = mergeDictionaries(dict[comp.firstObject], mergeDict);
            }
        }
        else {
            payload[key] = obj;
        }
    }];
    
    return [payload copy];
}

// MARK: Helpers

static void iterateDictKeys(NSDictionary *dict, NSMutableDictionary *combinedDict) {
    NSArray *keys = [dict allKeys];
    for (id key in keys) {
        
        id obj = [dict objectForKey:key];
        
        if ([obj isKindOfClass:[NSDictionary class]]) {
            NSMutableDictionary *subDict = [combinedDict objectForKey:key];
            if (!subDict) {
                subDict = [[NSMutableDictionary alloc] init];
                [combinedDict setObject:subDict forKey:key];
            }
            
            [subDict addEntriesFromDictionary:obj];
        }
        else {
            [combinedDict setObject:obj forKey:key];
        }
    }
}

static NSDictionary *mergeDictionaries(NSDictionary *dict1, NSDictionary *dict2) {
    
    NSMutableDictionary *combinedDictionary = [NSMutableDictionary dictionary];
    
    iterateDictKeys(dict1, combinedDictionary);
    iterateDictKeys(dict2, combinedDictionary);
    
    return [combinedDictionary copy];
}

@end
