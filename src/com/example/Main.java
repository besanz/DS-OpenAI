package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    private static final String API_KEY = " "; //Tu API key

    private static final String API_URL = " "; //URl de la API del modelo a utilizar

    public static void main(String[] args) {

        try 
        {

            String prompt = " "; //Nuestra solicitud o mensaje que queremos que procese

            String generatedText = generateText(prompt); //Ejecuta el prompt en el modelo definido arriba
            
            System.out.println("Texto generado: " + prompt + generatedText); //Imprimimos el resultado por pantalla

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    private static String generateText(String prompt) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setDoOutput(true);

        String requestBody = String.format("{\"prompt\": \"%s\", \"max_tokens\": 50}", prompt);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(requestBody);
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();

        BufferedReader reader;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String jsonResponse = response.toString();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject choice = choices.getJSONObject(0);
            String generatedText = choice.getString("text");
            return generatedText;
        } else {
            throw new Exception("Error al llamar a la API de OpenAI: " + response.toString());
        }
    }
}

