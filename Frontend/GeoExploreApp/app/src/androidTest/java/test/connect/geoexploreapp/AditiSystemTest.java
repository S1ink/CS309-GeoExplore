package test.connect.geoexploreapp;


import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;




public class AditiSystemTest {
    private boolean needLogin = false;
    private String firstName ="A";
    private String lastName ="N";
    private String emailId = "AN@gmail.com";
    private String password = "pass";
    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityScenarioRule<LoginSignUpActivity> activityRule = new ActivityScenarioRule<>(LoginSignUpActivity.class);

    @Before
    public void setUp() throws Exception {
        if (needLogin) {
            onView(withId(R.id.login_email)).perform(typeText(emailId));
            onView(withId(R.id.login_password)).perform(typeText(password));
            onView(withId(R.id.login_button)).perform(click());

            try {
                Thread.sleep(SIMULATED_DELAY_MS);
            } catch (InterruptedException e) {
            }

            // Verify that volley returned the correct value
            onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()));

        }
    }

    @Test
    public void SignInTest(){
        needLogin = false;
        onView(allOf(withText("Signup"), isDescendantOfA(withId(R.id.tab_layout))))
                .perform(click());

        onView(withId(R.id.firstName)).perform(typeText(firstName));
        onView(withId(R.id.lastName)).perform(typeText(lastName));
        onView(withId(R.id.signup_email)).perform(typeText(emailId));
        onView(withId(R.id.signup_password)).perform(typeText(password));
        onView(withId(R.id.signup_confirm)).perform(typeText(password));
        onView(withId(R.id.SignUpScrollView)).perform(swipeUp());
        onView(withId(R.id.signup_button)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that volley returned the correct value
         onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()));

    }

    @Test
    public void CreateObservation(){
        needLogin = true;

        onView(withId(R.id.FragmentMaps)).perform(longClick());
        onView(withText("What do you want to create?"))
                .inRoot(isDialog()) //
                .check(matches(isDisplayed()));

        onView(allOf(withId(android.R.id.text1), withText("Observation")))
                .inRoot(isDialog())
                .perform(click());


        onView(withId(R.id.editTextTitle)).perform(typeText("Lion Running"));
        onView(withId(R.id.editTextDescription)).perform(typeText("Saw a lion running. Be careful."));
        onView(withText("Create")).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that volley returned the correct value
        onView(withId(R.id.FragmentMaps)).check(matches(isDisplayed()));

    }

}

