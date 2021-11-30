package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest {

    //If there's no completed task and one active task
    //then there are 100% active tasks and 0% completed tasks
    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsZeroHundred(){
        val tasks = listOf<Task>(
            Task("title", "desc", isCompleted = false)
        )

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f, result.completedTasksPercent)
        assertEquals(100f, result.activeTasksPercent)

    }

    //if there are 2 completed tasks and 3 active tasks
    //then there are 40% completed tasks and 60% active tasks
    @Test
    fun getActiveAndCompletedStats_both_returnsFortySixty(){
        val tasks = listOf<Task>(
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false)
        )

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)

    }


    @Test
    fun getActiveAndCompletedStats_empty_returnsZeros(){
        val tasks = emptyList<Task>()

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)

    }


    @Test
    fun getActiveAndCompletedStats_error_returnsZeros(){
        val tasks = null

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)

    }

}