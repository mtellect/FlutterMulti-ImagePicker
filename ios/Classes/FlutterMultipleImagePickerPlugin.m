#import "FlutterMultipleImagePickerPlugin.h"
#import <flutter_multiple_image_picker/flutter_multiple_image_picker-Swift.h>

@implementation FlutterMultipleImagePickerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterMultipleImagePickerPlugin registerWithRegistrar:registrar];
}
@end
