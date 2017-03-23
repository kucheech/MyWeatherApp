package sg.per.wku.myweatherapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testGetTimeString() throws Exception {
        MainActivity mainActivity = new MainActivity();
        String actual = mainActivity.getTimeString(0);
        String expected = "1970-01-01 07.30.00";
        assertEquals("getTimeString fails", actual, expected);
    }

    @Test
    public void testGetTimeString2() throws Exception {
        MainActivity mainActivity = new MainActivity();
        String actual = mainActivity.getTimeString2(0);
        String expected = "1970-01-01 07 AM";
        assertEquals("getTimeString2 fails", actual, expected);
    }

    @Test
    public void testLat() throws Exception {
        MainActivity mainActivity = new MainActivity();
        double actual = mainActivity.lat;
        double expected = 0;
        assertEquals("testLat fails", actual, expected, 0.0001);
    }

    @Test
    public void testLon() throws Exception {
        MainActivity mainActivity = new MainActivity();
        double actual = mainActivity.lon;
        double expected = 0;
        assertEquals("testLon fails", actual, expected, 0.0001);
    }

    @Test
    public void testCurrentHour() throws Exception {
        MainActivity mainActivity = new MainActivity();
        boolean actual = mainActivity.currentHour >= 0;
        boolean expected = true;
        assertEquals("testCurrentHour fails", actual, expected);
    }
}