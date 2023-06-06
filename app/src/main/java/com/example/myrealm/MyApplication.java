package com.example.myrealm;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    private Long realmVersion = 0L;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("course.db")
                .allowWritesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(realmVersion)
                .build();

        Realm.setDefaultConfiguration(config);

    }
}
