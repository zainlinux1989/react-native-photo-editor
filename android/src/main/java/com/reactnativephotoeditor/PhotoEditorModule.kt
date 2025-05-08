package com.reactnativephotoeditor

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.*
import com.reactnativephotoeditor.activity.PhotoEditorActivity
import com.reactnativephotoeditor.activity.constant.ResponseCode
import android.net.Uri;

import com.facebook.react.bridge.*;
import com.canhub.cropper.*;



enum class ERROR_CODE {

}

class PhotoEditorModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  private val context = reactApplicationContext;

  private val EDIT_SUCCESSFUL = 1
  private var promise: Promise? = null
  

  override fun getName(): String {
    return "PhotoEditor"
  }

  @ReactMethod
  fun open(options: ReadableMap?, promise: Promise): Unit {
    this.promise = promise
    val activity = currentActivity
    if (activity == null) {
      promise.reject("ACTIVITY_DOES_NOT_EXIST", "Activity doesn't exist");
      return;
    }
    val intent = Intent(context, PhotoEditorActivity::class.java)
    context.addActivityEventListener(mActivityEventListener)
    Log.d(
      "TEST_TAG",
      "Verbose: more verbose than DEBUG logs 22______[$context]_________" 
    )
    val path = options?.getString("path")
    val stickers = options?.getArray("stickers") as ReadableArray
    // val contextR1 = options?.getOrImplicitDefault("context") as ReactApplicationContext
    // val contextR = activity as ReactApplicationContext
    // val contextR1 = options?..Serializable("context") as ReactApplicationContext

    intent.putExtra("path", path)
    intent.putExtra("stickers", stickers.toArrayList())
    // intent.putExtra("context", contextR1)
    

    activity.startActivityForResult(intent, EDIT_SUCCESSFUL)
  }

  @ReactMethod
  fun cropImage(imagePath: String, promise: Promise) {
      val currentActivity: Activity = getCurrentActivity();

      if (currentActivity == null) {
          promise.reject("NO_ACTIVITY", "No activity found");
          return;
      }

      val imageUri: Uri = Uri.parse(imagePath);
      val cropPromise = promise;

      CropImage.activity(imageUri)
              .setGuidelines(CropImageView.Guidelines.ON)
              .start(currentActivity);
  }

  private val mActivityEventListener: ActivityEventListener = object : BaseActivityEventListener() {
    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent?) {
      if (requestCode == EDIT_SUCCESSFUL) {
        when (resultCode) {
          ResponseCode.RESULT_OK -> {
          val path = intent?.getStringExtra("path")
          promise?.resolve("file://$path")
          }
          ResponseCode.RESULT_CANCELED -> {
          promise?.reject("USER_CANCELLED", "User has cancelled", null)
          }
          ResponseCode.LOAD_IMAGE_FAILED -> {
            val path = intent?.getStringExtra("path")
            promise?.reject("LOAD_IMAGE_FAILED", "Load image failed: $path", null)
          }
          
        }
      }
    }
  }

  public fun onActivityResult( activity: Activity,  requestCode: Number, resultCode: Number,  data: Intent) {
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
        val cropPromise = null;
    }
}

@Override
public fun onNewIntent( intent: Intent) {}
}
