package com.example.android.architecture.blueprints.todoapp.util

import androidx.test.espresso.idling.CountingIdlingResource

//Espresso idling resources act as a synchronization mechanism for Espresso tests and your long running operations
//You'll make two types of Espresso idling resources, CountingIdlingResource and DatabindingIdlingResource
//CountingIdlingResource makes Espresso wait during long-running tasks involving the network and database operations
//DatabindingIdlingResource helps Espresso stay in-sync with databinding and views

//kotlin's object keyword to make a singleton class,
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() { //provide access to the outside world (i.e. other classes) to the CountingIdlingResource  increment method
        countingIdlingResource.increment() //when counter is greater than zero, app is considered running and is doing some work
    }

    fun decrement() { //provide access to the outside world (i.e. other classes) to the CountingIdlingResource decrement method
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement() //when counter is less than zero, app is considered idle
        }
    }
}

//this inline function helps reduce boilerplate code by constantly calling increment & decrement, instead, you just wrap your long-running methods in DefaultTasksRepository with a wrapEspressoIdlingResource{}
inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    // Espresso does not work well with coroutines yet. See
    // https://github.com/Kotlin/kotlinx.coroutines/issues/982
    EspressoIdlingResource.increment() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement() // Set app as idle.
    }
}