package com.nestia.biometriclib;

import android.app.Activity;
import android.os.CancellationSignal;

import com.nestia.biometriclib.BiometricPromptManager;

import androidx.annotation.NonNull;

/**
 * Created by gaoyang on 2018/06/19.
 */
interface IBiometricPromptImpl {

    void authenticate(@NonNull CancellationSignal cancel,
                      @NonNull BiometricPromptManager.OnBiometricIdentifyCallback callback);

}
