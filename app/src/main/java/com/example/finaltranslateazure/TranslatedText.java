package com.example.finaltranslateazure;

import java.util.ArrayList;


class Translation {
    String text;
    String to;
}

class TranslatedText {
   ArrayList<Translation> translations;
    @Override
    public String toString() {
        String trans = "";
        for (Translation t: translations){
            trans += "translated text: " + t.text;
        }
        // перечень языков объединяем в одну строку

        return trans;
    }
}