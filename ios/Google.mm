#import "Google.h"
#import <GoogleSignIn/GoogleSignIn.h>

@implementation Google
- (NSNumber *)multiply:(double)a b:(double)b {
    NSNumber *result = @(a * b);

    return result;
}

- (void)signOut:(RCTPromiseResolveBlock)resolve
           rejecter:(RCTPromiseRejectBlock)reject {
    @try {
        [[GIDSignIn sharedInstance] signOutWithCompletion:^{
            resolve(nil);
        }];
    } @catch (NSException *exception) {
        reject(@"SIGN_OUT_ERROR", exception.reason, nil);
    }
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeGoogleSpecJSI>(params);
}

+ (NSString *)moduleName
{
  return @"Google";
}

@end
