package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


//Add the AndoirdJUnit4 test runner
@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {


    //create LiveDataTestUtil.kt file class so you can use the getOrAwaitValue Kotlin extension function

    //Two main things that you should do to test LiveData
    //First is to use architecture component InstantTaskExecutorRule
    //Second is to make sure that LiveData is observed

    //First: the InstantTaskExecutorRule is a JUnit rule, JUnit rules are classes that allow you to define some code before and after each test runs
    //This rule below runs all architecture component-related background jobs in the same thread
    //So it ensures that the test results happen 1. synchronously and 2. in a repeatable order. Two things that are pretty important for tests
    //basically when you're testing LiveData you should also include this rule
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    //Second: Testing LiveData is tricky because LiveData should be observed, the fact that LiveData is observed is important, when LiveData isn't observed it doesn't do much
    //You need active observers to trigger any onChange events, and to trigger any LiveData transformations
    //In short, to get the expected behavior for your LiveData, you need to observer the LiveData

    @Test
    fun addNewTask_setNewTaskEventBoilerplateCodeWay(){
        //we need to observe the newTask Event LiveData here, but this poses a problem, in TasksViewModelTest, we don't have an activity or a fragment or any lifecycle-owners with which to observe this LiveData
        //so to get around this, there's a observeForever method, what it does is that it ensures that LiveData is constantly observed without needing a lifecycle-owner
        //when you observeForever, you also need to remember to call removeObserver method, to make sure that you don't have an observer memory leak



        //GIVEN a fresh TasksViewModel
        val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        //create observer dummy - no need for it to do anything!
        val observer  = Observer<Event<Unit>> {}
        try {

            //observe LiveData forever
            taskViewModel.newTaskEvent.observeForever(observer)

            //WHEN adding a new task
            taskViewModel.addNewTask()


            //THEN the new task event is triggered
            //now because newTaskEvent is being observed, I can now get the value from it, and write an assert statement
            val value = taskViewModel.newTaskEvent.value
            assertThat(value?.getContentIfNotHandled(),not(nullValue())) //because we're testing an Event, we use getContentIfNotHandled in the assert statement

        } finally {
            //to make sure the removal of the observer absolutely happens, we put it in a finally block, after the try block
        taskViewModel.newTaskEvent.removeObserver(observer)
        }

    }



    //this is the same as above but better since it doesn't have boilerplate code thanks to the LiveDataTestUtil.kt class and getOrAwaitValue Kotlin extension function
    @Test
    fun addNewTask_setNewTaskEvent(){

        //GIVEN a fresh TasksViewModel
        val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        //WHEN adding a new task
        taskViewModel.addNewTask()


        //THEN the new task event is triggered
        val value = taskViewModel.newTaskEvent.getOrAwaitValue()//Observe and get the LiveData value for newTaskEvent using getOrAwaitValue extension function
        assertThat(value.getContentIfNotHandled(),not(nullValue())) //because we're testing an Event, we use getContentIfNotHandled in the assert statement

    }


}