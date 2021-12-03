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

//Integration tests can be either unit tests (local) or instrumented tests (on real device), since they test
//multiple classes integrated together or a feature, it's very situational whether they're unit or instrumented tests

//End-to-End (E2E) tests large portion of the app and test that it works as a whole, they're high fidelity
//and are almost always instrumented tests since they simulate real usage.

//when you're writing tests it's recommended that proportion of each test is
//about 70% unit test and, 20% integration tests and 10% end-to-end test

//For unit tests you can test Viewmodels, Repository and DAO for example
//For integration tests you can test integration between fragments and their viewmodels, or test all parts of the database code
//End-to-End test will test the entire app

class ExampleUnitTest {

    ////test methods naming convention: subjectUnderTest_actionOrInput_resultState

    //Given/When/Then testing mnemonic for structuring test with comments in a "Given X, When Y, then Z" format.
    //Another name for this convention is Arrange, Act, Assert (AAA) format

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        assertEquals(3, 1 + 2)
    }
}
