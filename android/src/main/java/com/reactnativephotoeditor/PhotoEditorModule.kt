package com.reactnativephotoeditor

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.*
import com.reactnativephotoeditor.activity.PhotoEditorActivity
import com.reactnativephotoeditor.activity.constant.ResponseCode
import android.net.Uri;

import com.facebook.react.bridge.*
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImage.ActivityResult
import com.canhub.cropper.CropImageView


enum class ERROR_CODE {

}

class PhotoEditorModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  private val context = reactApplicationContext;

  private val EDIT_SUCCESSFUL = 1
  private val CROP_IMAGE_ACTIVITY_REQUEST_CODE = 204
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
      if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
        val result = ActivityResult( 
          originalUri = binding.cropImageView.imageUri,
          uriContent = intent.extras?.getString("path"),
      error = error(message),
      cropPoints = binding.cropImageView.cropPoints,
      cropRect = CropImageView.cropRect;
      rotation = CropImageView.rotatedDegrees;
      wholeImageRect = CropImageView.wholeImageRect;
      sampleSize = 40;
      )
        if (resultCode == RESULT_OK) {
            val resultUri = result?.uriContent
            // використовуй результат
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            val error = result?.error
        }
    }
    }
  }

}
