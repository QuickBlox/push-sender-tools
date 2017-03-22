//
//  QBPushNotificationsViewController.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBPushNotificationsViewController.h"

#import "QBColor.h"
#import "QBFieldTableView.h"
#import "QBFieldCell.h"
#import "QBPushTableDataSource.h"
#import "QBAppleBasedPushModel.h"
#import "QBUniversalPushModel.h"
#import "QBInfoHeaderView.h"
#import "QBCore.h"
#import "SVProgressHUD.h"

static NSString * const kQBTitleText = @"Push Sender Tools";
static NSString * const kQBSendingPushText = @"Sending push";
static NSString * const kQBReceivedPushText = @"Received push notification";

typedef NS_ENUM(NSUInteger, SegmentedControlPushType) {
    SegmentedControlPushTypeAppleBased = 0,
    SegmentedControlPushTypeUniversal = 1
};

@interface QBPushNotificationsViewController () <UITableViewDelegate, QBCoreDelegate>

@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (weak, nonatomic) IBOutlet QBFieldTableView *tableView;
@property (weak, nonatomic) IBOutlet UITextView *textView;
@property (weak, nonatomic) IBOutlet QBInfoHeaderView *infoHeaderView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *receivedPushHeightConstraint;

@property (strong, nonatomic) QBPushTableDataSource *appleBasedDataSource;
@property (strong, nonatomic) QBPushTableDataSource *universalDataSource;

@property (strong, nonatomic) QBCore *core;

@end

@implementation QBPushNotificationsViewController

// MARK: Life cycle

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // initial controller setup
    [QBFieldCell registerForReuseInTableView:self.tableView];
    self.tableView.delegate = self;
    
    self.appleBasedDataSource = [[QBPushTableDataSource alloc] initWithModel:[[QBAppleBasedPushModel alloc] init]];
    self.universalDataSource = [[QBPushTableDataSource alloc] initWithModel:[[QBUniversalPushModel alloc] init]];
    
    self.core = [[QBCore alloc] init];
    self.core.delegate = self;
    [self.core login];
    
    self.navigationItem.rightBarButtonItem.enabled = NO;
    
    // setup initial data source
    self.tableView.dataSource = self.appleBasedDataSource;
    
    // notifications
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(pushDidReceive:)
                                                 name:@"kPushDidReceive"
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidShow:)
                                                 name:UIKeyboardDidShowNotification
                                               object:nil];
}

// MARK: UITableViewDelegate

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return [QBFieldCell height];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    [self.view endEditing:NO];
}

// MARK: Actions

- (IBAction)didPressSendButton:(UIBarButtonItem *)sender {
    [self.view endEditing:NO];
    
    NSDictionary *dict = nil;
    __weak __typeof(self)weakSelf = self;
    void (^startPushSending)() = ^void() {
        [weakSelf.infoHeaderView startActivityIndicator];
        weakSelf.infoHeaderView.title = kQBSendingPushText;
    };
    if (self.segmentedControl.selectedSegmentIndex == SegmentedControlPushTypeAppleBased) {
        
        dict = [self.appleBasedDataSource.model payload];
        if (dict.count > 0) {
            startPushSending();
            [self.core sendAppleBasedPushNotificationWithPayload:dict];
        }
        else {
            [SVProgressHUD showErrorWithStatus:@"All fields are empty"];
        }
    }
    else if (self.segmentedControl.selectedSegmentIndex == SegmentedControlPushTypeUniversal) {
        
        dict = self.universalDataSource.model.pushNotificationDictionary;
        if (dict.count > 0) {
            startPushSending();
            [self.core sendUniversalPushNotificationWithDict:dict];
        }
        else {
            [SVProgressHUD showErrorWithStatus:@"All fields are empty"];
        }
    }
    else {
        NSAssert(nil, @"Unsupported segmented control selection.");
    }
    startPushSending = nil;
}

- (IBAction)didPressClearButton:(UIBarButtonItem *)sender {
    [self.view endEditing:NO];
    [self.appleBasedDataSource.model.pushNotificationDictionary removeAllObjects];
    [self.universalDataSource.model.pushNotificationDictionary removeAllObjects];
    [self.tableView reloadData];
    self.textView.text = kQBReceivedPushText;
}

- (IBAction)segmentedControlValueChanged:(UISegmentedControl *)sender {
    switch (sender.selectedSegmentIndex) {
        case SegmentedControlPushTypeAppleBased:
            self.tableView.dataSource = self.appleBasedDataSource;
            break;
            
        case SegmentedControlPushTypeUniversal:
            self.tableView.dataSource = self.universalDataSource;
            break;
            
        default:
            NSAssert(nil, @"Unsupported segmented control value.");
            break;
    }
    
    [self.tableView reloadData];
}

- (void)pushDidReceive:(NSNotification *)notification {
    NSDictionary *userInfo = [notification userInfo];
    self.textView.text = [NSString stringWithFormat:@"%@", userInfo];
}

- (void)keyboardDidShow:(NSNotification *)notification {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        NSDictionary *keyboardInfo = [notification userInfo];
        NSValue *keyboardFrameBegin = [keyboardInfo valueForKey:UIKeyboardFrameBeginUserInfoKey];
        CGRect keyboardFrameBeginRect = [keyboardFrameBegin CGRectValue];
        self.receivedPushHeightConstraint.constant = CGRectGetHeight(keyboardFrameBeginRect);
    });
}

// MARK: QBCoreDelegate

- (void)coreDidLogin:(QBCore *)core {
    [self.core registerForRemoteNotifications];
    [self stopIndicatorAndShowTitle];
    self.navigationItem.rightBarButtonItem.enabled = YES;
}

- (void)core:(QBCore *)core didFailToLoginWithReason:(NSString *)reason {
    [self stopIndicatorAndShowTitle];
    [SVProgressHUD showErrorWithStatus:reason];
}

- (void)coreDidSendPushNotification:(QBCore *)core {
    [self stopIndicatorAndShowTitle];
    [SVProgressHUD showSuccessWithStatus:@"Sent"];
}

- (void)core:(QBCore *)core didFailToSendPushNotificationWithReason:(NSString *)reason {
    [self stopIndicatorAndShowTitle];
    [SVProgressHUD showErrorWithStatus:reason];
}

// MARK: Helpers

- (void)stopIndicatorAndShowTitle {
    [self.infoHeaderView stopActivityIndicator];
    self.infoHeaderView.title = kQBTitleText;
}

@end
