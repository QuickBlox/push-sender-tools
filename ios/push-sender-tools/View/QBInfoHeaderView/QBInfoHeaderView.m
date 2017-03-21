//
//  QBInfoHeaderView.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBInfoHeaderView.h"

@interface QBInfoHeaderView ()

@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *activityIndicator;
@property (weak, nonatomic) IBOutlet UILabel *label;

@end

@implementation QBInfoHeaderView

// MARK: Layout

- (void)layoutSubviews {
    [super layoutSubviews];
    
    [self.label sizeToFit];
}

// MARK: Public

- (void)startActivityIndicator {
    self.activityIndicator.hidden = NO;
    [self.activityIndicator startAnimating];
}

- (void)stopActivityIndicator {
    [self.activityIndicator stopAnimating];
    self.activityIndicator.hidden = YES;
}

- (void)setTitle:(NSString *)title {
    if (![_title isEqualToString:title]) {
        _title = [title copy];
        self.label.text = title;
        [self.label sizeToFit];
    }
}

@end
