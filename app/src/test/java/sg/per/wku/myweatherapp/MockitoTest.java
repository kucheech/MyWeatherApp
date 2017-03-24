package sg.per.wku.myweatherapp;

/**
 * Created by wku on 24-Mar-17.
 */

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockitoTest {

    @Test
    public void test_DarkSkyData_getPrecipIntensity()  {
        //  create mock
        DarkSkyData test = Mockito.mock(DarkSkyData.class);

        when(test.getPrecipIntensity()).thenReturn(0.0152);

        // use mock in test....
        assertEquals(test.getPrecipIntensity(), 0.015, 0.01);
    }

    @Test
    public void test_DarkSkyData_getPrecipProbability()  {
        //  create mock
        DarkSkyData test = Mockito.mock(DarkSkyData.class);

        when(test.getPrecipProbability()).thenReturn(0.057);

        // use mock in test....
        assertEquals(test.getPrecipIntensity() >= 0 && test.getPrecipIntensity() <= 1, true);
    }

    @Test
    public void test_DarkSkyData_toString()  {
        //  create mock
        DarkSkyData test = Mockito.mock(DarkSkyData.class);

        when(test.toString()).thenReturn("");

        // use mock in test....
        assertEquals(test.toString().length() > 0, true);
    }

}


