package com.hechim.models.data.workout

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

)
