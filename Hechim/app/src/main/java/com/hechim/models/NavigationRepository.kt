package com.hechim.models

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import dagger.hilt.android.scopes.ActivityRetainedScoped

@ActivityRetainedScoped
class NavigationRepository(val context: Context) {


    private var mainNavController: NavController? = null

    fun setController(navController: NavController) {
        mainNavController = navController
        println("SETTING MAIN NAV CONTROLLER")
        if(mainNavController != null) {
            println("MAIN NAV CONTROLLER NOT NULL")
        }
        else {
            println("MAIN NAV CONTROLLER IS NULL")
        }
    }

    fun navigate(route: String) {
        mainNavController!!.navigate(route = route)
    }

    fun navigateAndRemove(route: Int) {
//        mainNavController!!.navigate(route) {
//            popUpTo(mainNavController!!.graph.findStartDestination().id) {
//                inclusive = true
//            }
//        }

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
}