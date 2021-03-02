package com.example.flashcardz;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Register the parse model
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("p1D4Cocr1Ic4HQ0QllEEspmV71eDViMZO2LbjjDY")
                .clientKey("0kvwYLEfafn0m0uTXzAAZfqshzGjaU861gWwDCWT")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
