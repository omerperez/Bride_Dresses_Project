package com.example.bride_dresses_project.Utils;
import android.text.Editable;
import android.util.Patterns;
import androidx.annotation.Nullable;

import android.text.Editable;

import androidx.annotation.Nullable;

public class InputValidator {

    public InputValidator() {
    }

    public static boolean isDressTypeValid(@Nullable Editable text) {
        return !isFieldEmpty(text);
    }

    private static boolean isFieldEmpty(Editable text) {
        return (text == null || text.length() == 0);

    }

    public static boolean isDressPriceValid(@Nullable Editable text) {
        return !isFieldEmpty(text);
    }
}