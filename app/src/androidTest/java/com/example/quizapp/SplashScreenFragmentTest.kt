package com.example.quizapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashScreenFragmentTest {
    @Rule
    @JvmField
    val mainActivitySecnario = activityScenarioRule<MainActivity>()
    @Test
    fun is_splashScreen_visible() {
        launchFragmentInContainer<SplashScreenFragment>()
        onView(withId(R.id.splash_parent)).check(matches(isDisplayed()))
        onView(withId(R.id.app_name)).check(matches(withText("Quiz App")))
    }
}