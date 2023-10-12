package com.github.johnnysc.practicetdd

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Also check out one more MediatorTest in test package (junit test)
 */
@RunWith(AndroidJUnit4::class)
class MediatorTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test() {
        onView(withId(R.id.firstChoiceButton)).check(matches(isEnabled()))
        onView(withId(R.id.secondChoiceButton)).check(matches(isEnabled()))
        onView(withId(R.id.thirdChoiceButton)).check(matches(isEnabled()))
        onView(withId(R.id.saveButton)).check(matches(not(isEnabled())))

        onView(withId(R.id.firstChoiceButton)).perform(click())
        onView(withId(R.id.firstChoiceButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.secondChoiceButton)).check(matches(isEnabled()))
        onView(withId(R.id.thirdChoiceButton)).check(matches(isEnabled()))
        onView(withId(R.id.saveButton)).check(matches(isEnabled()))

        onView(withId(R.id.secondChoiceButton)).perform(click())
        onView(withId(R.id.secondChoiceButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.firstChoiceButton)).check(matches(isEnabled()))
        onView(withId(R.id.thirdChoiceButton)).check(matches(isEnabled()))
        onView(withId(R.id.saveButton)).check(matches(isEnabled()))

        onView(withId(R.id.thirdChoiceButton)).perform(click())
        onView(withId(R.id.thirdChoiceButton)).check(matches(not(isEnabled())))
        onView(withId(R.id.firstChoiceButton)).check(matches(isEnabled()))
        onView(withId(R.id.secondChoiceButton)).check(matches(isEnabled()))
        onView(withId(R.id.saveButton)).check(matches(isEnabled()))
    }
}