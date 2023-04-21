package com.example.quizapp

import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class SummaryScreenFragmentTest{
    @get:Rule
    val activityScenarioRule= activityScenarioRule<MainActivity>()

    @Test
    fun is_summary_screen_display() {
        launchFragmentInContainer<SummaryScreenFragment>()
        onView(withId(R.id.summary_screen_parent)).check(matches(isDisplayed()))
    }

    @Test
    fun reset_button_click() {
        activityScenarioRule.scenario.onActivity {
                activity -> activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SummaryScreenFragment()).commit()
        }
        onView(withId(R.id.button_reset)).perform(click())
        onView(withId(R.id.setup_screen_parent)).check(matches(isDisplayed()))
    }
}