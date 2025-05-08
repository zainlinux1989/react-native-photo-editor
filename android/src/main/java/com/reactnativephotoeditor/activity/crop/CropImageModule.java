ackage com.reactnativephotoeditor.activity.crop; 

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.facebook.react.bridge.*;
import com.canhub.cropper.*;

import javax.annotation.Nullable;


import com.reactnativephotoeditor.R;
import java.util.*;

public class CropImageModule 
// extends ReactContextBaseJavaModule 
// implements ActivityEventListener 
{
    private static final int CROP_IMAGE_ACTIVITY_REQUEST_CODE = 203;
    private Promise cropPromise;

    // public CropImageModule(ReactApplicationContext reactContext) {
    //     super(reactContext);
    //     reactContext.addActivityEventListener(this);
    // }

    // @NonNull
    // @Override
    // public String getName() {
    //     return "CropImageModule";
    // }

    // @ReactMethod
    public void cropImage(String imagePath, Promise promise) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject("NO_ACTIVITY", "No activity found");
            return;
        }

        Uri imageUri = Uri.parse(imagePath);
        cropPromise = promise;

        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(currentActivity);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUriContent();
                if (cropPromise != null) {
                    cropPromise.resolve(resultUri.toString());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                if (cropPromise != null) {
                    cropPromise.reject("CROP_ERROR", error.getMessage());
                }
            }
            cropPromise = null;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {}
}