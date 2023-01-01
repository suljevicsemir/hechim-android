package com.hechim.models.local

import android.location.Location

data class WorkoutNotificationSettings(
    var time: Long,
    var distance: Float,
    var paused: Boolean,
    var totalSteps: Int = -1,
    var workoutSteps: Int,
    var initialPosition: Location = Location("")
)
