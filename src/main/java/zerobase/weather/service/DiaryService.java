package zerobase.weather.service;


import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.entity.Diary;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@Transactional(readOnly = false)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;

    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);
    @Value("${openweathermap.key}")
    private String apiKey;

    public DiaryService(DiaryRepository diaryRepository,DateWeatherRepository dateWeatherRepository) {
        this.diaryRepository = diaryRepository;
        this.dateWeatherRepository = dateWeatherRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 1 1/1 * *")
    public void saveWeatherDate(){
        logger.info("data를 가져옴");
        dateWeatherRepository.save(getWeatherFromApi());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date,String text){
        logger.info("started created diary");
        //날씨 정보를 가져오기 db 에서
        DateWeather dateWeather = getDateWeather(date);

        Diary nowDiary= new Diary();
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);

        diaryRepository.save(nowDiary);
        logger.info("end to created diary");
    }

    private DateWeather getDateWeather(LocalDate date){
    List<DateWeather> dateWeathersFromDB= dateWeatherRepository.findAllByDate(date);
        if(dateWeathersFromDB.size() ==0){
            // 새로 api에서 날씨 정보 가져오기
          return  getWeatherFromApi();
        }else{
            return dateWeathersFromDB.get(0);
        }
    }
    private String getWeatherString(){
      String apiUrl =  "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid="+apiKey;
      try{

      URL url = new URL(apiUrl);
          HttpURLConnection connection =(HttpURLConnection)url.openConnection();
          connection.setRequestMethod("GET");
          int responseCode= connection.getResponseCode();
          BufferedReader br;
          if(responseCode == 200){
              br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
          }else{
              br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
          }
          String inputLine;
          StringBuilder response = new StringBuilder();
          while((inputLine= br.readLine()) != null){
              response.append(inputLine);
          }

      return response.toString();
      }  catch (Exception e){
          return"fail to get response";
      }
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));
        JSONArray weatherArray= (JSONArray)jsonObject.get("weather");
        JSONObject weather = (JSONObject) weatherArray.get(0);
        //weather 값이 jsonArray네

        resultMap.put("main", weather.get("main"));
        resultMap.put("icon", weather.get("icon"));

        return resultMap;


    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        logger.debug("read Diary");
       return  diaryRepository.findAllByDate(date);

    }

    @Transactional(readOnly = true)
    public List<Diary> readBetweenDiaries(LocalDate startDate, LocalDate endDate) {


       return diaryRepository.findAllByDateBetween(startDate,endDate);
    }

    public void updateDiary(LocalDate date, String text) {

       Diary nowDiary= diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);



    }

    public void deleteDiary(LocalDate date) {

        diaryRepository.deleteAllByDate(date);

    }

    private DateWeather getWeatherFromApi(){
        String weatherData =   getWeatherString();

        Map<String,Object>parseWeather = parseWeather(weatherData);

        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parseWeather.get("main").toString());
        dateWeather.setIcon(parseWeather.get("icon").toString());
        dateWeather.setTemperature((Double)parseWeather.get("temp"));


        return dateWeather;
    }
}
