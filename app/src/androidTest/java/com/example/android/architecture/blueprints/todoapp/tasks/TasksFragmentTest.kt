package com.example.android.architecture.blueprints.todoapp.tasks

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest {

    private lateinit var repository: TasksRepository

    @Before //set up the fake repo before each test
    fun initRepository(){
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After //tear down the fake repo after each test
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    /*
    Mockito is a library for making test doubles of mock type, stub type, and spies type
    Fragments navigation is the perfect use case for using mock test doubles from the Mockito's library
    because the navigation controller NavController API is an API that's highly unlikely to change unlike my own code which could change drastically during  dev
    now if the NavController API did change it could break its mock tests, and this is why you should assess things before deciding to use a mock, and well, you've assessed the situation and decided to use a mock to test navigation :)
     */

      /*To test navigation with Mockito:
        Use Mockito to create a NavController mock.
        Attach that mocked NavController to the fragment.
        Verify that navigate was called with the correct action and parameter(s).*/

    @Test //since you're calling methods from the repository,  you're going to be calling suspend functions so wrap this test in a runBlockingTest to allow you to call suspend functions
    fun clickTask_navigateToDetailFragmentOne() = runBlockingTest {

        repository.saveTask(Task("TITLE1","DESC1",false,"id1"))
        repository.saveTask(Task("TITLE2","DESC2",true,"id2"))

        //GIVEN - On home screen
        //create your fragment of type TasksFragment using FragmentScenario's launchFragmentInContainer and provide it an empty arguments bundle using Bundle() and the AppTheme
        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)

        //Use Mockito's mock function to create a mock test double of the NavController class
        val navController = mock(NavController::class.java)

        //set your new mock as the fragment's NavController using FragmentScenario's onFragment
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController) //set the TasksFragment's view navController to the mocked navController
        }

        //WHEN - Click on the first list item
        //the code to click on the item in the RecyclerView that has the text "TITLE1". You will be using Espresso library to perform the click and Espresso contrib library to interact with the RecyclerView ViewHolder
        onView(withId(R.id.tasks_list))
            .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText("TITLE1")), click()))

        //THEN - Verify (assert) that we navigate to the first detail screen
        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment("id1")
        )

    }


    @Test
    fun clickAddTaskButton_navigateToAddEditFragment() = runBlockingTest {

        //GIVEN - On home screen
        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        //WHEN - Click on Add Tasks FAB button
        onView(withId(R.id.add_task_fab))
            .perform(click())

        //THEN - Verify(assert) that we navigate to the AddEditFragment
        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                null, getApplicationContext<Context>().getString(R.string.add_task)
            )
        )

    }

}