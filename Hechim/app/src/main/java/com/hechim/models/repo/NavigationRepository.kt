package com.hechim.models.repo

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import com.hechim.utils.Extensions.animatedNavigate
import dagger.hilt.android.scopes.ActivityRetainedScoped

@ActivityRetainedScoped
class NavigationRepository(val context: Context) {

    private var mainNavController: NavController? = null

    /**
     * sets navigation controller to be used throughout
     * app via dependency injection
     * can be injected in ViewModels, other repos
     * most importantly, in http interceptor
     */
    fun setController(navController: NavController) {
        mainNavController = navController
    }

    /**
     * navigate for a given route
     */
    fun navigate(route: String) {
        if(mainNavController == null) {
            return
        }
        mainNavController!!.navigate(route = route)
    }

    /**
     * navigates to given route and removes
     * all other routes from the back stack
     * used in cases like logout, finishing on boarding
     */
    fun navigateAndRemove(route: Int, navDirections: NavDirections? = null) {
        if(navDirections != null) {
            mainNavController!!.animatedNavigate(
                directions = navDirections
            )
            return
        }


        mainNavController!!.navigate(
            route,
            args = null,
            navOptions {
                popUpTo(mainNavController!!.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        )
    }

    fun startNavigate(loggedIn: Boolean, firstStart:Boolean) {

    }
}