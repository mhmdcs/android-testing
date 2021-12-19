package com.example.android.architecture.blueprints.todoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import com.example.android.architecture.blueprints.todoapp.util.DataBindingIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//end-to-end black-box tests are instrumented tests, since it's an end-to-end test class it should be at the root of the testAndroid source set

@RunWith(AndroidJUnit4::class) //Annotate the class with @RunWith(AndroidJUnit4::class) because you're using AndroidX test code.
@LargeTest //Annotate the class with @LargeTest, which signifies these are end-to-end tests, testing a large portion of the code.
class TasksActivityTest {

    private lateinit var repository: TasksRepository

    @Before //Create a @Before method and initialize the repository using the ServiceLocator's provideTasksRepository method; use getApplicationContext to get the application context.
    fun init() {
        //In the @Before method, delete all the tasks in the repository, to ensure it's completely cleared out before each and every test run.
        repository = ServiceLocator.provideTasksRepository(getApplicationContext())  //in end-to-end tests we use the real repository, not fakes or any other test-doubles
        runBlocking {
            repository.deleteAllTasks() //make sure that there's no previous state from any of my other tests, clean up the repo by deleting all tasks
        }
    }

    @After //Create an @After method that calls the ServiceLocator's resetRepository() method.
    fun reset() {
        ServiceLocator.resetRepository() //this does additional cleanup to the repo
    }

    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }


    @Test
    fun editTask() = runBlocking {
        // Set initial state.
        repository.saveTask(Task("TITLE1", "DESCRIPTION"))

        // Start up Tasks screen.
        //ActivityScenario is like FragmentScenario, t's an AndroidX Testing library that wraps around an activity and gives  you direct control over activity's lifecycle for testing
        //when you're using ActivityScenario it's important to remember to call activityScenario.close() at the end of your tests, it's very important to call this before doing any sort of reset with the database
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario) // use dataBindingIdlingResource's monitorActivity extension function to associate the activity with the dataBindingIdlingResource


        // Espresso code bellow:
        // Click on the task on the list and verify that all the data is correct.
        onView(withText("TITLE1")).perform(click())
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("TITLE1")))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("DESCRIPTION")))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        // Click on the edit button, edit, and save.
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("NEW TITLE"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("NEW DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Verify task is displayed on screen in the task list.
        onView(withText("NEW TITLE")).check(matches(isDisplayed()))
        // Verify previous task is not displayed.
        onView(withText("TITLE1")).check(doesNotExist())
        // Make sure the activity is closed before resetting the db.



        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun createOneTask_deleteTask() {

        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Add active task
        onView(withId(R.id.add_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text))
                .perform(typeText("TITLE1"), closeSoftKeyboard())
        onView(withId(R.id.add_task_description_edit_text)).perform(typeText("DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Open it in details view
        onView(withText("TITLE1")).perform(click())
        // Click delete task in menu
        onView(withId(R.id.menu_delete)).perform(click())

        // Verify it was deleted
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
        onView(withText("TITLE1")).check(doesNotExist())
        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

}