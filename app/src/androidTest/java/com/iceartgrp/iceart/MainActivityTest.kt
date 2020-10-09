package com.iceartgrp.iceart

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iceartgrp.iceart.components.MainActivity
import com.iceartgrp.iceart.network.ApiConsumer
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Arrange
        val json = "{\"_id\": 5,\"info\": \"i\", \"title\": \"XXX\"}"
        val mockServer = MockWebServer()
        val mResponse = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(json)
        mockServer.enqueue(mResponse)
        mockServer.start()
        ApiConsumer.host = mockServer.url("").toString().dropLast(1)

        // Act
        onView(withId(R.id.mainbtn)).perform(click())

        // Assert
        onView(withId(R.id.maintxt)).check(matches(withText("XXX")))
    }
}
