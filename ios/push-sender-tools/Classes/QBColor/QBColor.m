//
//  QBColor.m
//  push-sender-tools
//
//  Created by Vitaliy Gorbachov on 3/21/17.
//  Copyright © 2017 Quickblox. All rights reserved.
//

#import "QBColor.h"

UIColor *QBColorWithHex(int hex)
{
    return [[UIColor alloc] initWithRed:(((hex >> 16) & 0xff) / 255.0f)
                                  green:(((hex >> 8) & 0xff) / 255.0f)
                                   blue:(((hex) & 0xff) / 255.0f)
                                  alpha:1.0f];
}

UIColor *QBTableViewBackgroundColor()
{
    static UIColor *color = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^
                  {
                      color = QBColorWithHex(0xebebf1);
                  });
    return color;
}
