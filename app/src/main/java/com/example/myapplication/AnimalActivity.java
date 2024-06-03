package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class AnimalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("ID_EXTRA")) finish();

        String animalId = intent.getStringExtra("ID_EXTRA");

        try {
            String jsonStr = loadJsonFromAsset(this, "AnimalsData.json");

            Log.i("JSONSTR", jsonStr);

            JSONArray animalsArray = new JSONArray(jsonStr);

            JSONObject animalInfo = null;
            for (int i = 0; i < animalsArray.length(); i++) {
                JSONObject animal = animalsArray.getJSONObject(i);
                if (animal.getString("id").equals(animalId)) {
                    animalInfo = animal;
                    break;
                }
            }

            if (animalInfo != null) Log.i("ANIMAL", "NOT NULL");

            if (animalInfo != null) {
                String animalType = animalInfo.getString("animal_type");
                String animalName = animalInfo.getString("animal_name");
                String animalColor = animalInfo.getString("animal_color");
                String ownerName = animalInfo.getString("owner_name");
                String ownerPhone = animalInfo.getString("owner_phone");
                // Добавьте обработку animal_picture, если нужно

                ImageView animalImageView = findViewById(R.id.animal_image);

                try {
                    InputStream inputStream = getAssets().open(animalId + ".jpg");
                    Drawable drawable = Drawable.createFromStream(inputStream, null);
                    animalImageView.setImageDrawable(drawable);
                } catch (IOException e) {
                    e.printStackTrace(); // Выводим стек вызовов исключения в журнал
                }

                TextView animalInfoTextView = findViewById(R.id.animal_info);
                animalInfoTextView.setText("Тип: " + animalType + "\n" +
                        "Имя: " + animalName + "\n" +
                        "Цвет: " + animalColor + "\n" +
                        "Владелец: " + ownerName + "\n" +
                        "Телефон владельца: " + ownerPhone);
            }

            Button backButton = findViewById(R.id.back_button);
            backButton.setOnClickListener(v -> onBackPressed());
        } catch (JSONException e) {
            Log.i("JSONException", e.getMessage());
        }
    }

    // Метод для чтения содержимого JSON-файла из assets
    private String loadJsonFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
