package test.connect.geoexploreapp;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;



import android.support.test.rule.ActivityTestRule;

// Mock the RequestServerForService class
@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time
public class AditiSystemTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Start the server and run this test
     */
    @Test
    public void reverseString(){
        String testString = "hello";
        String resultString = "olleh";
        // Type in testString and send request
//        onView(withId(R.id.stringEntry))
//                .perform(typeText(testString), closeSoftKeyboard());
//        onView(withId(R.id.submit)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Verify that volley returned the correct value
       // onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));

    }

    /**
     * Start the server and run this test
     */
    @Test
    public void capitalizeString(){
        String testString = "hello";
        String resultString = "HELLO";
        // Type in testString and send request
//        onView(withId(R.id.stringEntry))
//                .perform(typeText(testString), closeSoftKeyboard());
//        onView(withId(R.id.submit2)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        //onView(withId(R.id.text_simple)).check(matches(withText("Hello Espresso!")));

        // Verify that volley returned the correct value
        //onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));
    }
}

