package com.example.finaltranslateazure;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Экземпляр библиотеки и интерфейса можно использовать для всех обращений к сервису
    // формируем экземпляр библиотеки
    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // ответ от сервера в виде строки
            .baseUrl(AzureTranslationAPI.API_URL) // адрес API сервера
            .build();

    AzureTranslationAPI api = retrofit.create(AzureTranslationAPI.class);

    Spinner spinner;
    ArrayAdapter adapter;
    String[] str;
    String item;
    String currLang;

    EditText input;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Call<LanguagesResponse> call = api.getLanguages(); // создаём объект-вызов
        call.enqueue(new LanguagesCallback());

        input = findViewById(R.id.editTextTo);
        spinner = findViewById(R.id.spinner);
        textview = findViewById(R.id.textView2);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранный объект
                item = (String)parent.getItemAtPosition(position);
                currLang = item.split("-")[0];
                Log.d("tag uwu", item + " " + currLang);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "nothing is selected", Toast.LENGTH_SHORT).show();
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

    }

    public void setSpinner() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        spinner.setAdapter(adapter);
    }

    // TODO: создать аналогичным образом класс для ответа сервера при переводе текста
    class LanguagesCallback implements Callback<LanguagesResponse> {

        @Override
        public void onResponse(Call<LanguagesResponse> call, Response<LanguagesResponse> response) {
            if (response.isSuccessful()) {
                String languages = response.body().toString();
                str = languages.split(":");

                setSpinner();
                // описываем, какие функции реализованы
                Log.d("mytag", "response: ");
            } else {
                Log.d("mytag", "response: failure");
            }
        }

        @Override
        public void onFailure(Call<LanguagesResponse> call, Throwable t) {
            Toast.makeText(MainActivity.this, "Failed to get languages", Toast.LENGTH_LONG).show();
        }
    }

    class TranslatedCallback implements Callback<TranslatedText[]> {

        @Override
        public void onResponse(Call<TranslatedText[]> call, Response<TranslatedText[]> response) {
            if (response.isSuccessful()) {
                String transtext = response.body()[0].toString();
                textview.setText(transtext);
                Log.d("mytag", "response: " + transtext);
            } else {
                Log.d("mytag", "response: failure on response");
            }
        }
        @Override
        public void onFailure(Call<TranslatedText[]> call, Throwable t) {
            Log.d("mytag", "response: failure on failure");
        }

    }
    class Text{
       String Text;
    }

    public void onClick(View v){
        String text = String.valueOf(input.getText());
        Log.d("mytag", text + "\t" +item);

        Text body = new Text();
        body.Text = text;

        Text[] array = {body};
        Call<TranslatedText[]> call = api.translate(currLang, array );
        Log.d("mytag", text);
        call.enqueue(new TranslatedCallback());


    }

}