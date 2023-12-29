package com.example.projectnailsschedule.presentation.main


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.projectnailsschedule.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class InsertAppointmentTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun insertAppointmentTest() {
        val recyclerView = onView(
            allOf(
                withId(R.id.calendarRecyclerView),
                childAtPosition(
                    withId(R.id.fragment_calendar),
                    2
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(4, click()))

        val floatingActionButton = onView(
            allOf(
                withId(R.id.go_into_date), withContentDescription("edit date"),
                childAtPosition(
                    allOf(
                        withId(R.id.fragment_calendar),
                        childAtPosition(
                            withId(R.id.nav_host_fragment_content_main),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val floatingActionButton2 = onView(
            allOf(
                withId(R.id.fragment_date_addButton), withContentDescription("Запись"),
                // TODO: поправить  ContentDescription
                childAtPosition(
                    allOf(
                        withId(R.id.fragment_date),
                        childAtPosition(
                            withId(R.id.nav_host_fragment_content_main),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        floatingActionButton2.perform(click())

        val appCompatTextView = onView(
            allOf(
                withId(R.id.time_edit_text),
                childAtPosition(
                    allOf(
                        withId(R.id.client_procedure_appointment_month_rv_child),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            3
                        )
                    ),
                    1
                )
            )
        )
        appCompatTextView.perform(scrollTo(), click())

        val appCompatButton = onView(
            allOf(
                withId(android.R.id.button1), withText("��"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.name_et),
                childAtPosition(
                    allOf(
                        withId(R.id.client_phone_appointment_month_rv_child),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            4
                        )
                    ),
                    1
                )
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("Bvz���"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.procedure_et),
                childAtPosition(
                    allOf(
                        withId(R.id.client_vk_appointment_month_rv_child),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            5
                        )
                    ),
                    1
                )
            )
        )
        appCompatEditText2.perform(scrollTo(), replaceText("������ "), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.phone_et),
                childAtPosition(
                    allOf(
                        withId(R.id.client_telegram_appointment_month_rv_child),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            6
                        )
                    ),
                    1
                )
            )
        )
        appCompatEditText3.perform(scrollTo(), replaceText("123456"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.client_vk_link_et),
                childAtPosition(
                    allOf(
                        withId(R.id.client_instagram_appointment_month_rv_child),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            7
                        )
                    ),
                    1
                )
            )
        )
        appCompatEditText4.perform(scrollTo(), replaceText("���"), closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.client_telegram_link_et),
                childAtPosition(
                    allOf(
                        withId(R.id.client_whatsapp_appointment_month_rv_child),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            8
                        )
                    ),
                    1
                )
            )
        )
        appCompatEditText5.perform(scrollTo(), replaceText("��"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.client_instagram_link_et),
                childAtPosition(
                    allOf(
                        withId(R.id.client_notes_appointment_month_rv_child),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            9
                        )
                    ),
                    1
                )
            )
        )
        appCompatEditText6.perform(scrollTo(), replaceText("����"), closeSoftKeyboard())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.client_whatsapp_link_et),
                childAtPosition(
                    allOf(
                        withId(R.id.client_notes_cl_date_appointment_rv_item),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            10
                        )
                    ),
                    1
                )
            )
        )
        appCompatEditText7.perform(scrollTo(), replaceText("���"), closeSoftKeyboard())

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.notes_et),
                childAtPosition(
                    allOf(
                        withId(R.id.constraintLayout10),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            11
                        )
                    ),
                    1
                )
            )
        )
        appCompatEditText8.perform(scrollTo(), replaceText("����"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.save_button), withText("���������"),
                childAtPosition(
                    allOf(
                        withId(R.id.constraintLayout11),
                        childAtPosition(
                            withId(R.id.fragment_add_row),
                            12
                        )
                    ),
                    0
                )
            )
        )
        appCompatButton2.perform(scrollTo(), click())

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("������� �����"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.action_bar),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.action_bar_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
