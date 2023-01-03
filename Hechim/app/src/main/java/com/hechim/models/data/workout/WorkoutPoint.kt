package com.hechim.models.data.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WorkoutPoints")
data class WorkoutPoint(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val workoutId: Int,

    val latitude: Double,
    val longitude: Double,
    //meters per second
    val speed: Float,
    //sea level altitude
    val altitude: Double
)
