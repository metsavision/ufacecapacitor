//
//  MKGAmango.h
//  MKGAmango
//
//  Created by 송인목 on 2023/03/21.
//

#import <Foundation/Foundation.h>

static int SUCCESS = 0;
static int ERR_NULL_HANDLE = -31001;
static int ERR_NULL_PARAMETER = -31002;
static int ERR_OUT_OF_MEMORY = -31003;
static int ERR_MACHINE_NUMBER_NOT_SET = -31004;
static int ERR_CAN_NOT_GET_MACHINE_NUMBER = -31005;
static int ERR_LICENSE_KEY_NOT_SET = -31006;
static int ERR_INVALID_LICENSE_KEY = -31007;
static int ERR_INVALID_PARAMETER_TYPE = -31008;
static int ERR_INVALID_PARAMETER = -31009;
static int ERR_INVALID_IMAGE_FORMAT = -31010;
static int ERR_IMAGE_PROCESS_FAIL = -31011;
static int ERR_INVALID_FACE_INDEX = -31012;
static int ERR_GET_LANDMARKS_FAIL = -31013;
static int ERR_GET_FEATURE_FAIL = -31014;
static int ERR_COMPARE_FEATURES_FAIL = -31015;
static int ERR_LOADING_MODEL_FAILURE = -31016;

static int ATTACK_NORMAL = 0;
static int ATTACK_ATTACK = 1;
static int ATTACK_PERSON = 2;
static int ATTACK_MASKFACE = 3;
static int ATTACK_HANDFINGER = 4;
static int ATTACK_PRINT = 5;

@interface MKGAmango : NSObject

-(int) MKGAsetModelDirectory:(NSString*)modelDirectory;
-(int) MKGAinit:(NSString*)licensekey modelDirectory:(NSString*)modelDirectory;

// Detect faces, and return number of faces
-(int) MKGAdetectPNG:(NSData*)image scale:(float)scale;
-(int) MKGAdetectedPNG:(NSData*)image x:(int)x y:(int)y width:(int)width height:(int)height;
-(int) MKGAgetMaskStatusAlter:(int)index;
// 0:x, 1:y, 2:width, 3:height
-(NSMutableArray*) MKGAgetRect:(int)index;
-(NSData*) MKGAgetThumbnail:(int)index;
-(NSMutableArray*) MKGAgetPitchYawRoll:(int)index frameWidth:(int)width frameHeight:(int)height;
-(void) MKGASetDebugDirectory:(NSString*)debugPath;

-(int) MKPPdetectAttackInit;
/*
 * array[0] : 1 = attack, 2 = person, 3 = mask-face, 4 = hand or finger, 5 = print, 0 = no detected.
 *      when [0] > 0, the indices [1] = x, [2] = y, [3] = width, [4] = height
 */
-(NSMutableArray*)MKPPdetectAttackPNG:(NSData*)png;

-(int) MKPPeyeBlinkInit;
-(int) MKPPstartEyeBlink;
-(int) MKPPnextEyeBlinkPNGWithBox:(NSData*)png x:(int)x y:(int)y w:(int)with h:(int)height;
-(int) MKPPgetEyeStatusPNGWithBox:(NSData*)png x:(int)x y:(int)y w:(int)with h:(int)height;

-(int) MKPPdetectSpoofInit;
-(int) MKPPdetectSpoofPNGWithBox:(NSData*)png x:(int)x y:(int)y w:(int)with h:(int)height threshold:(float)threshold;
-(float) MKPPgetSpoofScore;
-(float) MKPPvarianceOfLaplacianPNG:(NSData*)png;

-(int) MKGAMbfInit;
-(NSData*) MKGAMbfExtract:(int) index;
-(float) MKGAMbfVerify:(NSData*) srcFeature targetFeature:(NSData*) targetFeature;

@end
