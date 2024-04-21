package test.connect.geoexploreapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.CoreMatchers.allOf;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class YusefSystemTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityTestRule<LoginSignUpActivity> activityRule = new ActivityTestRule<>(LoginSignUpActivity.class);


    // checks if the login was successful
    @Test
    public void successLogin(){
        onView(withId(R.id.login_email)).perform(typeText("yharb@iastate.edu"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_password)).perform(typeText("password"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // made it to the home screen
        onView(withId(R.id.bottomNavigationView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void successSignUp(){
        onView(allOf(withText("Signup"), isDescendantOfA(withId(R.id.tab_layout))))
                .perform(click());

        onView(withId(R.id.firstName)).perform(typeText("Testing"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.lastName)).perform(typeText("Yusef"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.signup_email)).perform(typeText("yusef@test.com"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.signup_password)).perform(typeText("pass"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.signup_confirm)).perform(typeText("pass"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.SignUpScrollView)).perform(swipeUp());

        onView(withId(R.id.is_admin)).perform(click());

        onView(withId(R.id.signup_button)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }


        //made it to homescreen. Meaning success
        onView(withId(R.id.bottomNavigationView))
                .check(matches(isDisplayed()));



    }

    @Test
    public void failedLogin() {
        onView(withId(R.id.login_email)).perform(typeText("invalidemail@test.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_password)).perform(typeText("wrongpassword"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withText("Invalid Credentials!")).check(matches(isDisplayed()));


        onView(withId(R.id.bottomNavigationView)).check(doesNotExist());
    }


    @Test
    public void passworkMismatchSignUpTest(){
        onView(allOf(withText("Signup"), isDescendantOfA(withId(R.id.tab_layout))))
                .perform(click());

        onView(withId(R.id.firstName)).perform(typeText("Testing"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.lastName)).perform(typeText("Yusef"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.signup_email)).perform(typeText("yusef@test.com"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.signup_password)).perform(typeText("pass"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.signup_confirm)).perform(typeText("password"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.SignUpScrollView)).perform(swipeUp());


        onView(withId(R.id.is_admin)).perform(click());

        onView(withId(R.id.signup_button)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withText("Password does not match confirmPassword!")).check(matches(isDisplayed()));

        onView(withId(R.id.bottomNavigationView)).check(doesNotExist());

    }


}
