package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest {

    ////test methods naming convention: subjectUnderTest_actionOrInput_resultState

    //Given/When/Then testing mnemonic for structuring test with comments in a "Given X, When Y, then Z" format.
    //Another name for this convention is Arrange, Act, Assert (AAA) format

    //If there's no completed task and one active task
    //then there are 100% active tasks and 0% completed tasks
    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsZeroHundred(){
        //GIVEN sets up objects and the state of the app you need for your test
        //GIVEN this list of tasks and their isCompleted state

        //GIVEN  a list of tasks with a single, active task
        val tasks = listOf<Task>(
            Task("title", "desc", isCompleted = false)
        )


        //WHEN is when you do the actual action or make changes to the subject under test
        //WHEN here is calling getActiveAndCompletedStats

        //WHEN you call getActiveAndCompletedStats
        val result = getActiveAndCompletedStats(tasks)


        //THEN is how you know whether your test has passed or failed, it's usually a bunch of assertion calls
        //THEN here is these two assertEqual calls

        //THEN there are 0% completed tasks and 100% active tasks
        assertThat(result.completedTasksPercent, `is`(0f)) //using Hamcrest. "is" is a reserved keyword in Kotlin so use back ticks `is`, and the expected hardcoded result is the second argument
        assertEquals(100f, result.activeTasksPercent)  //using JUnit

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


    // When there are no tasks
    @Test
    fun getActiveAndCompletedStats_empty_returnsZeros(){
        val tasks = emptyList<Task>()

        val result = getActiveAndCompletedStats(tasks)

//        assertEquals(0f, result.completedTasksPercent)
//        assertEquals(0f, result.activeTasksPercent)

        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))

    }


    // When there's an error loading stats
    @Test
    fun getActiveAndCompletedStats_error_returnsZeros(){
        val tasks = null

        val result = getActiveAndCompletedStats(tasks)

//        assertEquals(0f, result.completedTasksPercent)
//        assertEquals(0f, result.activeTasksPercent)


        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))

    }

}