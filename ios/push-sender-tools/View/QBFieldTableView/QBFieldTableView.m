//
//  QBFieldTableView.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBFieldTableView.h"

#import "QBColor.h"

@implementation QBFieldTableView

- (void)awakeFromNib {
    [super awakeFromNib];
    // hide empty separators
    self.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    self.backgroundColor = QBTableViewBackgroundColor();
}

@end
