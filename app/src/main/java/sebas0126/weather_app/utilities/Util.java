package sebas0126.weather_app.utilities;

public class Util {

    public static String toPercentage(double value) {
        return "" + Math.round(value * 100);
    }

    public static String convertTemperature(double value) {
        return "" + Math.round((value - 32) / 1.8);
    }

}


