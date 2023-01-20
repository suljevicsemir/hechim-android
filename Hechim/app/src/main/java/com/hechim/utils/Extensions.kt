package com.hechim.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.navOptions
import com.google.android.material.textfield.TextInputLayout
import com.hechim.R
import com.hechim.models.local.AlertDialogConfig
import com.hechim.utils.Extensions.openAppSystemSettings
import com.hechim.utils.Extensions.themeColor


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

    /**
     * Opens application settings directly from the app
     */
    fun Context.openAppSystemSettings() {
        startActivity(Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        })
    }

    /**
     * Retrieves theme color for the passed argument id (attrRes)
     */
    @ColorInt
    fun Context.themeColor(@AttrRes attrRes: Int): Int = TypedValue()
        .apply { theme.resolveAttribute (attrRes, this, true) }
        .data

    /**
     * Shows alert dialog with argument
     */
    fun FragmentActivity.showDialog(config: AlertDialogConfig) {
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(config.positiveButtonText) { _, _ ->
                   config.positiveButtonCallback.invoke()
                }
                setNegativeButton(config.negativeButtonText) { _, _ ->
                    config.negativeButtonCallback.invoke()
                }
            }
            //apply dialog title and message
            builder.apply {
                setTitle(config.title)
                setMessage(config.description)
            }
            // Set other dialog properties
            // Create the AlertDialog
            builder.create()
        }
        //set button text color
        alertDialog?.setOnShowListener {
            val buttonColor = themeColor(com.google.android.material.R.attr.colorSecondary)
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)!!.setTextColor(buttonColor)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)!!.setTextColor(buttonColor)
        }

        alertDialog?.show()
    }

}