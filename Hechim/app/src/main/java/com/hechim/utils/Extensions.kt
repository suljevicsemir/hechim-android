package com.hechim.utils

import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.navOptions
import com.google.android.material.textfield.TextInputLayout
import com.hechim.R


object Extensions {
    /**
       * Method to execute animated navigate from anywhere
       * NavDirections are generated using the auto generated directions via safeargs plugin
     */
    fun NavController.animatedNavigate(directions: NavDirections) {
        navigate(directions, navOptions {
            anim {
                //how the next fragment is going to appear
                //the entrance of the fragment we're navigating to
                enter = R.anim.from_right
                //how the current fragment is going to leave
                //the screen, the one we're navigating from
                exit = R.anim.to_left
                //how the fragment below the currently popped one
                //is going to appear
                popEnter = R.anim.from_left
                //how the fragment being popped is going to exit
                //the screen
                popExit = R.anim.to_right
            }
        })
    }

    /**
        Validates text input layout fields
        Returns boolean if the input is valid
        If input is invalid, error on the input layout is shown
     */
    fun TextInputLayout.validateInput(isValid: (String) -> Boolean, error: String):Boolean{
        if(this.editText == null) {
            return false
        }
        if(!isValid((this.editText as EditText).text.toString())) {
            val params: ViewGroup.LayoutParams = this.layoutParams
            this.error = error
            this.layoutParams = params
            return false
        }
        return true
    }

    /**
     Attaches a listener to text input layout and resets the error
     (if exists) when the user starts typing
     it will not impact the text itself
     */
    fun TextInputLayout.errorResetListener() {
        if(this.editText == null) {
            return
        }
        (this.editText as EditText).addTextChangedListener {
            if(it.toString().isNotEmpty()) {
                this.error = null
            }
        }

    }
}