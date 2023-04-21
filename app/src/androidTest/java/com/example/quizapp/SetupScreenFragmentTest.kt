package com.example.quizapp

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SetupScreenFragmentTest{
    @get:Rule
    val mainActivitySecnario = activityScenarioRule<MainActivity>()
    @Test
    fun isSetupScreenVisible() {
        launchFragmentInContainer<SetupScreenFragment>()
        onView(withId(R.id.setup_screen_parent)).check(matches(isDisplayed()))
    }
    @Test
    fun start_button_click() {
        mainActivitySecnario.scenario.onActivity {
                activity -> activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SetupScreenFragment()).commit()
        }
        onView(withId(R.id.button_start)).perform(click())
        onView(withId(R.id.list_screen_parent)).check(matches(isDisplayed()))
    }
}