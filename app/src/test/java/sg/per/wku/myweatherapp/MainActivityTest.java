package sg.per.wku.myweatherapp;

import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by wku on 24-Mar-17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {
    private MainActivity activity;
    private TextView textViewCurrentlyHeader;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveCurrentlyHeader() throws Exception {
        assertNotNull(activity.findViewById(R.id.textViewCurrentlyHeader));
    }

    @Test
    public void shouldHaveCurrentlyData() throws Exception {
        assertNotNull(activity.findViewById(R.id.textViewCurrentlyData));
    }

    @Test
    public void shouldHaveHourlyHeader() throws Exception {
        assertNotNull(activity.findViewById(R.id.textViewHourlyHeader));
    }

    @Test
    public void shouldHaveHourlyData() throws Exception {
        assertNotNull(activity.findViewById(R.id.textViewHourlyData));
    }

    @Test
    public void shouldHaveProgressBar() throws Exception {
        assertNotNull(activity.findViewById(R.id.progressBar));
    }

    @Test
    public void shouldHaveWebViewCurrently() throws Exception {
        assertNotNull(activity.findViewById(R.id.webViewCurrently));
    }

    @Test
    public void shouldHaveWebViewHourly() throws Exception {
        assertNotNull(activity.findViewById(R.id.webViewHourly));
    }


    @Test
    public void shouldHaveResult() {
        textViewCurrentlyHeader = (TextView) activity.findViewById(R.id.textViewCurrentlyHeader);
        assertNotNull("TextView could not be found", textViewCurrentlyHeader);
        assertTrue(textViewCurrentlyHeader.getText().toString().length() > 0);
    }
}
