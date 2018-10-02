import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    //url http://api.openweathermap.org/data/2.5/weather?q=London&&units=metric&&appid=009b33a305b6cd775a3e5633569ca1c7
    public static String OPEN_WEATHER_KEY = "009b33a305b6cd775a3e5633569ca1c7";
    public static String getWeather(String cityName, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&&units=metric&&appid="+OPEN_WEATHER_KEY);
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while(in.hasNext()) {
            result += in.next();
        }

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));
        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));
        JSONArray jsonArray = object.getJSONArray("weather");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            model.setIcon((String)obj.get("icon"));
            model.setMain((String)obj.get("main"));
        }

        return "City: " + model.getName() + "\n"
                + "Temperature: " + model.getTemp() + "°C" + "\n"
                + "Humidity: " + model.getHumidity() + "%" + "\n"
                + "Main: " + model.getMain() + "\n"
                + "http://openweathermap.org/img/w/"+model.getIcon()+".png";
    }
}
