package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest //Generally unit tests are annotated with @SmallTest, integration tests are annotated with @MediumTest, and end-to-end tests are annotated with @LargeTest, these annotations help you group and choose which size of test to run
@RunWith(AndroidJUnit4::class) ////Add AndroidJUnit4 test runner when you're using AndroidX Test library code
class TaskDetailFragmentTest{

    @Test
    fun activeTaskDetails_DisplayedInUi(){
        //basically we're going to be given one task, an active task, and then we're gonna make sure that it loads up properly in our fragment

        //GIVEN - Add active (aka incomplete) task to the DB
        val activeTask = Task("An Active Task","AndroidX Rocks", false)

        //WHEN - Details fragment launched to display task
        val bundle = TaskDetailFragmentArgs(activeTask.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme) //use FragmentScenario API's launchFragmentInContainer method. The reason we've explicitly given the app theme is because when you're using launchFragmentInContainer you launch the fragment in an empty activity, and because fragments inherit their theme from the activity, you want to make sure you've given the correct theme here
        Thread.sleep(3000) //let the execution of the current running thread sleep for 3 seconds to read the error more appropriately
    }

}