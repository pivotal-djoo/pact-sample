package com.example.todo

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ToDoAddAndDeleteTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun toDoAddAndDeleteTest() {
        val appCompatEditText = Espresso.onView(
            allOf(
                withId(R.id.new_to_do_edittext),
                childAtPosition(
                    allOf(
                        withId(R.id.add_to_do_container),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatEditText.perform(
            ViewActions.replaceText("Pick up milk"),
            ViewActions.closeSoftKeyboard()
        )
        val materialButton = Espresso.onView(
            allOf(
                withId(R.id.add_to_do_button), ViewMatchers.withText("Add new To Do"),
                childAtPosition(
                    allOf(
                        withId(R.id.add_to_do_container),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton.perform(ViewActions.click())
        val appCompatEditText2 = Espresso.onView(
            allOf(
                withId(R.id.new_to_do_edittext),
                childAtPosition(
                    allOf(
                        withId(R.id.add_to_do_container),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatEditText2.perform(
            ViewActions.replaceText("Pick up cereal"),
            ViewActions.closeSoftKeyboard()
        )
        val materialButton2 = Espresso.onView(
            allOf(
                withId(R.id.add_to_do_button), ViewMatchers.withText("Add new To Do"),
                childAtPosition(
                    allOf(
                        withId(R.id.add_to_do_container),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton2.perform(ViewActions.click())
        val appCompatEditText3 = Espresso.onView(
            allOf(
                withId(R.id.new_to_do_edittext),
                childAtPosition(
                    allOf(
                        withId(R.id.add_to_do_container),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatEditText3.perform(
            ViewActions.replaceText("Pick up bananas"),
            ViewActions.closeSoftKeyboard()
        )
        val materialButton3 = Espresso.onView(
            allOf(
                withId(R.id.add_to_do_button), ViewMatchers.withText("Add new To Do"),
                childAtPosition(
                    allOf(
                        withId(R.id.add_to_do_container),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialButton3.perform(ViewActions.click())
        val materialCheckBox = Espresso.onView(
            allOf(
                withId(R.id.checkbox),
                withIndex(
                    childAtPosition(
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("com.google.android.material.card.MaterialCardView")),
                            0
                        ),
                        1
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialCheckBox.perform(ViewActions.click())
        val materialCheckBox2 = Espresso.onView(
            allOf(
                withId(R.id.checkbox),
                withIndex(
                    childAtPosition(
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("com.google.android.material.card.MaterialCardView")),
                            0
                        ),
                        1
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        materialCheckBox2.perform(ViewActions.click())
        val appCompatImageView = Espresso.onView(
            allOf(
                withId(R.id.delete_image),
                withIndex(
                    childAtPosition(
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("com.google.android.material.card.MaterialCardView")),
                            0
                        ),
                        2
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatImageView.perform(ViewActions.click())
    }

    fun childAtPosition(
        parentMatcher: Matcher<View>,
        position: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return (parent is ViewGroup && parentMatcher.matches(parent)
                        && view == (parent as ViewGroup).getChildAt(position))
            }
        }
    }

    fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var currentIndex = 0
            override fun describeTo(description: Description) {
                description.appendText("with index: ")
                description.appendValue(index)
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                return matcher.matches(view) && currentIndex++ == index
            }
        }
    }
}