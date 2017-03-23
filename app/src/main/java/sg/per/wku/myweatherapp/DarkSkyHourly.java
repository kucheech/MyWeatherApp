package sg.per.wku.myweatherapp;

/**
 * Created by wku on 23-Mar-17.
 */

public class DarkSkyHourly {
    String summary;
    String icon;
    DarkSkyData[] data;

    public DarkSkyHourly(String summary, String icon, DarkSkyData[] data) {
        this.summary = summary;
        this.icon = icon;
        this.data = data;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public DarkSkyData[] getData() {
        return data;
    }
}
