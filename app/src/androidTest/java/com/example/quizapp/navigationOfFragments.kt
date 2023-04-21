package com.example.quizapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class navigationOfFragments {
    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun navigationOfFragments() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SetupScreenFragment()).commit()
        }
        onView(withId(R.id.button_start)).perform(click())
        onView(withId(R.id.list_screen_parent)).check(matches(isDisplayed()))
        onView(withId(R.id.button_submit)).perform(click())
        onView(withText("Are you sure you want to Submit the test ?")).check(matches(isDisplayed()))
        onView(withText("Yes")).perform(click())
        onView(withId(R.id.summary_screen_parent)).check(matches(isDisplayed()))
        onView(withId(R.id.button_reset)).perform(click())
        onView(withId(R.id.setup_screen_parent)).check(matches(isDisplayed()))
    }
}