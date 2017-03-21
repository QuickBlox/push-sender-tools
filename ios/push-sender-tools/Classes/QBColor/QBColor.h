//
//  QBColor.h
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#ifdef __cplusplus
extern "C" {
#endif
    
    UIColor *QBColorWithHex(int hex);
    UIColor *QBTableViewBackgroundColor();
    
#ifdef __cplusplus
}
#endif
