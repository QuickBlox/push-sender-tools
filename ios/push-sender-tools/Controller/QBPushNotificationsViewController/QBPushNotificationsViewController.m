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

typedef NS_ENUM(NSUInteger, SegmentedControlPushType) {
    SegmentedControlPushTypeAppleBased = 0,
    SegmentedControlPushTypeUniversal = 1
};

@interface QBPushNotificationsViewController () <UITableViewDelegate, QBCoreDelegate>

@property (weak, nonatomic) IBOutlet UISegmentedControl *segmentedControl;
@property (weak, nonatomic) IBOutlet QBFieldTableView *tableView;
@property (weak, nonatomic) IBOutlet UITextView *textView;
@property (weak, nonatomic) IBOutlet QBInfoHeaderView *infoHeaderView;

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
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(pushDidReceive:)
                                                 name:@"kPushDidReceive"
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
    if (self.segmentedControl.selectedSegmentIndex == SegmentedControlPushTypeAppleBased) {
        
        dict = [self.appleBasedDataSource.model payload];
        if (dict.count > 0) {
            
            [self.core sendAppleBasedPushNotificationWithPayload:dict];
        }
    }
    else if (self.segmentedControl.selectedSegmentIndex == SegmentedControlPushTypeUniversal) {
        
        dict = self.universalDataSource.model.pushNotificationDictionary;
        if (dict.count > 0) {
            
            [self.core sendUniversalPushNotificationWithDict:dict];
        }
    }
    else {
        NSAssert(nil, @"Unsupported segmented control selection.");
    }
}

- (IBAction)didPressClearButton:(UIBarButtonItem *)sender {
    [self.view endEditing:NO];
    [self.appleBasedDataSource.model.pushNotificationDictionary removeAllObjects];
    [self.universalDataSource.model.pushNotificationDictionary removeAllObjects];
    [self.tableView reloadData];
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

// MARK: QBCoreDelegate

- (void)coreDidLogin:(QBCore *)core {
    [self.core registerForRemoteNotifications];
    [self.infoHeaderView stopActivityIndicator];
    self.navigationItem.rightBarButtonItem.enabled = YES;
    self.infoHeaderView.title = kQBTitleText;
}

- (void)core:(QBCore *)core didFailToLoginWithReason:(NSString *)reason {
    [SVProgressHUD showErrorWithStatus:reason];
}

- (void)core:(QBCore *)core didFailToSendPushNotificationWithReason:(NSString *)reason {
    [SVProgressHUD showErrorWithStatus:reason];
}

@end
