package com.konopackipio1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//todo: Why program terinates without catchin interrupt in the main try-catch

class App {

    static final String name = "https://catfact.ninja/fact";
    static final String key = "fact";

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Thread asyncRESTGet = new Thread(new AsyncRESTGet(name, key));

        executorService.submit(asyncRESTGet);

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while((reader.readLine()) == null) {    
                Thread.sleep(1000); 
            }
            asyncRESTGet.interrupt();
        } catch (IOException e) {
            System.err.println("Problem with reading from console");
        } catch (InterruptedException e) {
            System.out.println("Program will finish");
        }
        
        executorService.shutdownNow();        
    }
}

public class AsyncRESTGet implements Runnable{

    private String apiName;
    private String key;

    public AsyncRESTGet(String apiName, String key) {
        this.apiName = apiName;
        this.key = key;
    }

    public static String getHttpResponse(URL url) throws IOException, ParseException {
        
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if(connection.getResponseCode() != 200) {
            return "";
        }

        String response = "";
        try (Scanner scanner = new Scanner(url.openStream())) {
            while(scanner.hasNext()) {
                response += scanner.nextLine();
            }
        }

        return response;
    }

    @Override
    public void run() {
        

        try {
            URL url = new URL(apiName);
            while(!Thread.interrupted()) {
                Thread.sleep(2000);                
                String response = getHttpResponse(url);
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(response);
    
                System.out.println(jsonObject.get(key));
            }

        } catch (MalformedURLException e) {
            System.err.println("Could not open URL: " + apiName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Could not parse to json");
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted");
            Stream.of(e.getSuppressed()).forEach(System.out::println);
        }

    }
        

    
}
