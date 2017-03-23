//
//  QBFieldCell.h
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import <UIKit/UIKit.h>

@class QBFieldCell;

NS_ASSUME_NONNULL_BEGIN

/**
 *  QBFieldCellDelegate protocol interface.
 */
@protocol QBFieldCellDelegate <NSObject>

/**
 *  Protocol methods down below are required to be implemented.
 */
@required

/**
 *  Notifies when cell did end editing on text field.
 *
 *  @param fieldCell QBFieldCell instance
 *  @param text text after editing did end
 */
- (void)fieldCell:(QBFieldCell *)fieldCell didEndEditingWithText:(nullable NSString *)text;

@end

@interface QBFieldCell : UITableViewCell

/**
 *  Delegate that conforms to QBFieldCellDelegate protocol.
 */
@property (weak, nonatomic, nullable) id<QBFieldCellDelegate> delegate;

/**
 *  Text field placeholder text.
 */
@property (copy, nonatomic, nullable) NSString *placeholderText;

/**
 *  Text field text.
 */
@property (copy, nonatomic, nullable) NSString *text;

/**
 *  Register nib in table view.
 *
 *  @param tableView table view to register nib into
 */
+ (void)registerForReuseInTableView:(UITableView *)tableView;

/**
 *  Cell reuse identifier.
 *
 *  @return cell class identifier
 */
+ (NSString *)cellIdentifier;

/**
 *  Cell height.
 *
 *  @return cell height
 */
+ (CGFloat)height;

@end

NS_ASSUME_NONNULL_END
