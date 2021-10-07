/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.windowmanagersample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.window.layout.FoldingFeature.Orientation.Companion.HORIZONTAL
import androidx.window.layout.FoldingFeature.Orientation.Companion.VERTICAL
import androidx.window.layout.FoldingFeature.State.Companion.FLAT
import androidx.window.layout.FoldingFeature.State.Companion.HALF_OPENED
import androidx.window.layout.WindowInfoRepository.Companion.windowInfoRepository
import androidx.window.testing.layout.FoldingFeature
import androidx.window.testing.layout.TestWindowLayoutInfo
import androidx.window.testing.layout.WindowLayoutInfoPublisherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DisplayFeaturesActivityTest {
    private val activityRule = ActivityScenarioRule(DisplayFeaturesActivity::class.java)
    private val publisherRule = WindowLayoutInfoPublisherRule()

    @get:Rule
    val testRule: TestRule

    init {
        testRule = RuleChain.outerRule(publisherRule).around(activityRule)
    }

    @Test
    fun testDeviceOpen_Flat() {
        activityRule.scenario.onActivity { activity ->
            val feature =
                FoldingFeature(activity = activity, state = FLAT, orientation = HORIZONTAL)
            val expected = TestWindowLayoutInfo(listOf(feature))

            val value = TestCoroutineScope().async {
                activity.windowInfoRepository().windowLayoutInfo.first()
            }
            publisherRule.overrideWindowLayoutInfo(expected)
            runBlockingTest {
                assertEquals(
                    expected,
                    value.await()
                )
            }
        }
        onView(withSubstring("state = FLAT")).check(matches(isDisplayed()))
        onView(withSubstring("is not separated")).check(matches(isDisplayed()))
        onView(withSubstring("Hinge is horizontal")).check(matches(isDisplayed()))
    }

    @Test
    fun testDeviceOpen_TableTop() {
        activityRule.scenario.onActivity { activity ->
            val feature =
                FoldingFeature(activity = activity, state = HALF_OPENED, orientation = HORIZONTAL)
            val expected = TestWindowLayoutInfo(listOf(feature))

            val value = TestCoroutineScope().async {
                activity.windowInfoRepository().windowLayoutInfo.first()
            }
            publisherRule.overrideWindowLayoutInfo(expected)
            runBlockingTest {
                assertEquals(
                    expected,
                    value.await()
                )
            }
        }
        onView(withSubstring("state = HALF_OPENED")).check(matches(isDisplayed()))
        onView(withSubstring("are separated")).check(matches(isDisplayed()))
        onView(withSubstring("Hinge is horizontal")).check(matches(isDisplayed()))
    }

    @Test
    fun testDeviceOpen_Book() {
        activityRule.scenario.onActivity { activity ->
            val feature =
                FoldingFeature(activity = activity, state = HALF_OPENED, orientation = VERTICAL)
            val expected = TestWindowLayoutInfo(listOf(feature))

            val value = TestCoroutineScope().async {
                activity.windowInfoRepository().windowLayoutInfo.first()
            }
            publisherRule.overrideWindowLayoutInfo(expected)
            runBlockingTest {
                assertEquals(
                    expected,
                    value.await()
                )
            }
        }
        onView(withSubstring("state = HALF_OPENED")).check(matches(isDisplayed()))
        onView(withSubstring("are separated")).check(matches(isDisplayed()))
        onView(withSubstring("Hinge is vertical")).check(matches(isDisplayed()))
    }
}
