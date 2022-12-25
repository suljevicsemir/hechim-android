package com.hechim.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.navOptions
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
}