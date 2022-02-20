package com.androidfundamental.githubuserapp.ui.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.androidfundamental.githubuserapp.R
import org.junit.Test

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun changeMode() {
        onView(withId(R.id.switchMode)).perform(click())
    }
}