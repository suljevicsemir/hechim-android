package com.hechim.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("textInputType")
fun TextInputLayout.inputType(type: String) {
    if(type == "password_toggle") {
        this.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
    }
}