package com.example.quizapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuestionDetailScreenFragmentTest{
    @get:Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun is_detail_fragment_working() {
        activityScenarioRule.scenario.onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, QuestionDetailScreenFragment()).commit()
        }
        onView(withId(R.id.button_next)).perform(click())
        onView(withId(R.id.button_submit_in_detail_fragment)).perform(click())
        onView(ViewMatchers.withText("Are you sure you want to Submit the test ?")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText("Yes")).perform(click())
        onView(withId(R.id.summary_screen_parent)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}