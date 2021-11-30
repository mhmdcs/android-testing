package com.example.android.architecture.blueprints.todoapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

//UnitTest is a "Local  Test", they're called that because they're run on your local machine JVM, (in my case, my trusty MacBook Pro laptop)
//They run  on the JVM and they don't require an emulator or a physical device to run
//and because of that they tend to run very fast, but their drawback is that they have less fidelity
//fidelity in programming and testing jargon means how "real world" a thing is

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        assertEquals(3, 1 + 2)
    }
}
