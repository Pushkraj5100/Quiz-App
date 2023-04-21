package com.example.quizapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuestionsListScreenFragmentTest{
    @get:Rule
    val mainActivityScenario = activityScenarioRule<MainActivity>()
    @Test
    fun is_recycle_view_visible() {
        mainActivityScenario.scenario.onActivity { activity ->
            activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, QuestionsListScreenFragment()).commit()
        }
        onView(withId(R.id.recyclerview_question)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview_question)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(9))
    }
}