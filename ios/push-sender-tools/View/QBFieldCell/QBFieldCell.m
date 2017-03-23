//
//  QBFieldCell.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBFieldCell.h"

#import "QBColor.h"

@interface QBFieldCell ()

@property (weak, nonatomic) IBOutlet UITextField *textField;

@end

@implementation QBFieldCell

// MARK: Static methods

+ (void)registerForReuseInTableView:(UITableView *)tableView {
    
    NSString *nibName = NSStringFromClass([self class]);
    UINib *nib = [UINib nibWithNibName:nibName bundle:[NSBundle mainBundle]];
    NSParameterAssert(nib);
    
    NSString *cellIdentifier = [self cellIdentifier];
    NSParameterAssert(cellIdentifier);
    
    [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
}

+ (NSString *)cellIdentifier {
    
    return NSStringFromClass([self class]);
}

+ (CGFloat)height {
    return 44.0f;
}

// MARK: Lifecycle

- (void)awakeFromNib {
    [super awakeFromNib];
    self.backgroundColor = QBTableViewBackgroundColor();
    _textField.placeholder = nil;
    _textField.text = nil;
}

// MARK: Overrides

- (void)setPlaceholderText:(NSString *)placeholderText {
    if (![_placeholderText isEqualToString:placeholderText]) {
        _placeholderText = [placeholderText copy];
        _textField.placeholder = placeholderText;
    }
}

- (NSString *)text {
    return _textField.text;
}

- (void)setText:(NSString *)text {
    if (![_textField.text isEqualToString:text]) {
        _textField.text = text;
    }
}

// MARK: Actions

- (IBAction)textViewEditingDidEnd:(UITextField *)sender {
    [_delegate fieldCell:self didEndEditingWithText:sender.text];
}

@end
