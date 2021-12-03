package com.example.android.architecture.blueprints.todoapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

//InstrumentedTest is a "Integration Test", these tests run on real or emulated Android devices
//so these tests reflect what will happen on the real world, but they're a bit slower than Unit Tests
//these tests have much more fidelity, but the tradeoff is that they're slow
//"fidelity" term in programming and testing jargon means how "real world" a thing is

//Integration tests can be either unit tests (local) or instrumented tests (on real device), since they test
//multiple classes integrated together or a feature, it's very situational whether they're unit or instrumented tests

//End-to-End (E2E) tests large portion of the app and test that it works as a whole, they're high fidelity
//and are almost always instrumented tests since they simulate real usage.

//when you're writing tests it's recommended that proportion of each test is
//about 70% unit test and, 20% integration tests and 10% end-to-end test

//For unit tests you can test Viewmodels, Repository and DAO for example
//For integration tests you can test integration between fragments and their viewmodels, or test all parts of the database code
//End-to-End test will test the entire app

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    ////test methods naming convention: subjectUnderTest_actionOrInput_resultState

    //Given/When/Then testing mnemonic for structuring test with comments in a "Given X, When Y, then Z" format.
    //Another name for this convention is Arrange, Act, Assert (AAA) format

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.android.architecture.blueprints.reactive",
            appContext.packageName)
    }
}
