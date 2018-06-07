package maugost.fluttermultipleimagepicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.tangxiaolv.telegramgallery.GalleryActivity;
import com.tangxiaolv.telegramgallery.GalleryConfig;

import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterMultipleImagePickerPlugin
 */
public class FlutterMultipleImagePickerPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener{
    private static final String MAX_IMAGES = "maxImages";
    private static final String IS_SINGLE = "isSingle";
    private static final int PICK_MULTI_IMAGES = 1000;
    private static final String PICK_UP_IMAGES = "pickUpImagesImage";
    private static final String CHANNEL_NAME = "flutter_multiple_image_picker";
    private final MethodChannel channel;
    private Activity activity;
    private Context context;
    private Result pendingResult;
    private MethodCall methodCall;
    private List<String> photos;

    /**
     * Plugin registration.
     */

    private FlutterMultipleImagePickerPlugin(Activity activity, Context context, MethodChannel channel) {
        this.activity = activity;
        this.context = context;
        this.channel = channel;
    }


    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_NAME);
        FlutterMultipleImagePickerPlugin instance = new FlutterMultipleImagePickerPlugin(registrar.activity(), registrar.context(), channel);
        registrar.addActivityResultListener(instance);
        channel.setMethodCallHandler(instance);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

        if (!setPendingMethodCallAndResult(methodCall, result)) {
            finishWithAlreadyActiveError();
            return;
        }

        if (PICK_UP_IMAGES.equals(call.method)) {
            openMulti_ImagePicker(call);
        } else {
            pendingResult.notImplemented();
        }
    }

    private void openMulti_ImagePicker(MethodCall call){

        if (call.hasArgument(MAX_IMAGES) && call.hasArgument(IS_SINGLE)) {
            int maxImages = call.argument(MAX_IMAGES);
            boolean isSingle = call.argument(IS_SINGLE);

            if(maxImages == 0){
                finishWithError("Max_Images","Please images cannot be 0");
                return;
            }

            GalleryConfig config = new GalleryConfig.Build()
                    .limitPickPhoto(maxImages)
                    .singlePhoto(isSingle)
                    .hintOfPick("Selected Maximum no of images")
                    .filterMimeTypes(new String[]{"image/jpeg"})
                    .build();
            GalleryActivity.openActivity(activity, PICK_MULTI_IMAGES, config);
        }else {
            finishWithError("Max_Images & Single_Photo","Please images cannot be set to 0.. ");

        }




    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_MULTI_IMAGES && resultCode == Activity.RESULT_OK) {
            photos = (List<String>) data.getSerializableExtra(GalleryActivity.PHOTOS);
            //Toast.makeText(activity, photos.toString(), Toast.LENGTH_SHORT).show();
            finishWithSuccess(photos);
            return true;
        }
        return false;
    }


    private void finishWithSuccess(List imagePathList) {
        pendingResult.success(imagePathList);
        clearMethodCallAndResult();
    }


    private void finishWithSuccess(String imagePath) {
        pendingResult.success(imagePath);
        clearMethodCallAndResult();
    }

    private void finishWithAlreadyActiveError() {
        finishWithError("already_active", "Image picker is already active");
    }

    private void finishWithError(String errorCode, String errorMessage) {
        pendingResult.error(errorCode, errorMessage, null);
        clearMethodCallAndResult();
    }

    private void clearMethodCallAndResult() {
        methodCall = null;
        pendingResult = null;
    }

    private boolean setPendingMethodCallAndResult(
            MethodCall methodCall, MethodChannel.Result result) {
        if (pendingResult != null) {
            return false;
        }

        this.methodCall = methodCall;
        pendingResult = result;
        return true;
    }

}
