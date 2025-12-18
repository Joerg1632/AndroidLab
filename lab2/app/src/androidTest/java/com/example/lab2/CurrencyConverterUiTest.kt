package com.example.lab2

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyConverterUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun input_valid_amount_and_convert() {
        onView(withId(R.id.etFrom))
            .perform(typeText("100"), closeSoftKeyboard())

        onView(withId(R.id.btnConvert))
            .perform(click())

        onView(withId(R.id.tvResult))
            .check(matches(isDisplayed()))
    }

    @Test
    fun input_invalid_amount_shows_error() {
        onView(withId(R.id.etFrom))
            .perform(clearText(), closeSoftKeyboard())

        onView(withId(R.id.btnConvert))
            .perform(click())

        onView(withId(R.id.tvResult))
            .check(matches(isDisplayed()))
            .check(matches(not(withText(""))))
            .check(matches(not(withText(containsString("=")))))
    }

    @Test
    fun change_currency_from() {
        onView(withId(R.id.tvFromCurrency))
            .perform(click())

        onView(withText("EUR"))
            .perform(click())

        onView(withId(R.id.tvFromCurrency))
            .check(matches(withText("EUR")))
    }

    @Test
    fun swap_currencies() {
        val originalFrom = "USD"
        val originalTo = "RUB"

        onView(withId(R.id.btnSwap))
            .perform(click())

        onView(withId(R.id.tvFromCurrency))
            .check(matches(withText(originalTo)))

        onView(withId(R.id.tvToCurrency))
            .check(matches(withText(originalFrom)))
    }
}