package com.hechim.models.interfaces.dao

import androidx.room.Dao
import androidx.room.Query
import com.hechim.models.data.workout.Workout
import com.hechim.models.data.workout.WorkoutPoint

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM Workouts" +
        "JOIN WorkoutPoints ON Workouts.id = WorkoutPoints.workoutId"
    )
    fun loadWorkoutsAndPoints(): Map<Workout, List<WorkoutPoint>>
}