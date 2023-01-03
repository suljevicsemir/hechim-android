package com.hechim.models.interfaces.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hechim.models.data.workout.Workout
import com.hechim.models.data.workout.WorkoutPoint


@Dao
interface WorkoutPointDao {

    @Query("SELECT * FROM workoutpoints  ")
    fun getWorkoutPoints()

    @Insert
    suspend fun insertWorkoutPoints(workoutPoints: List<WorkoutPoint>)
}