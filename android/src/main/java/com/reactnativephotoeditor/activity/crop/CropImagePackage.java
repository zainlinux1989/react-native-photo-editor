package com.reactnativephotoeditor.activity.crop;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.uimanager.ViewManager;

import java.util.*;

public class CropImagePackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(
        com.facebook.react.bridge.ReactApplicationContext reactContext
    ) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new CropImageModule(reactContext));
        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(
        com.facebook.react.bridge.ReactApplicationContext reactContext
    ) {
        return Collections.emptyList();
    }
}