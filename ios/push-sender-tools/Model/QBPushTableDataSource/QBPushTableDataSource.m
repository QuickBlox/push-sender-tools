//
//  QBPushTableDataSource.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBPushTableDataSource.h"

#import "QBPushNotificationModel.h"
#import "QBFieldCell.h"

@interface QBPushTableDataSource () <QBFieldCellDelegate>

@end

@implementation QBPushTableDataSource

// MARK: Construction

- (instancetype)initWithModel:(QBPushNotificationModel *)model {
    
    self = [super init];
    if (self != nil) {
        
        _model = model;
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(appWillTerminate:)
                                                     name:UIApplicationWillTerminateNotification
                                                   object:nil];
    }
    
    return self;
}

- (void)appWillTerminate:(NSNotification *)notification {
    [_model save];
}

// MARK: UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [_model model].count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    QBFieldCell *cell = [tableView dequeueReusableCellWithIdentifier:[QBFieldCell cellIdentifier]];
    NSString *pushKey = [_model model][indexPath.row];
    cell.placeholderText = pushKey;
    cell.text = _model.pushNotificationDictionary[pushKey];
    cell.delegate = self;
    
    return cell;
}

// MARK: QBFieldCellDelegate protocol

- (void)fieldCell:(QBFieldCell *)fieldCell didEndEditingWithText:(NSString *)text {
    if (text == nil) {
        [_model.pushNotificationDictionary removeObjectForKey:text];
    }
    else {
        _model.pushNotificationDictionary[fieldCell.placeholderText] = text;
    }
}

@end
