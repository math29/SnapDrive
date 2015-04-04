package com.snapdrive.snapdrive;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by crocus on 01/04/15.
 */
public class WidgetConfiguration extends Activity {

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On essaye de récupérer l'id de l'AppWidget
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null)
        {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // Si l'intent ne contient pas son ID, ça ne sert à rien de continuer.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            Toast.makeText(getApplicationContext(),"Invalid ID",Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }

        // Sinon on le configure comme prévu
        configureWidget(getApplicationContext());
        Toast.makeText(getApplicationContext(),"Configured",Toast.LENGTH_SHORT).show();

        // IMPORTANT : penser à renvoyer l'ID du widget
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void configureWidget(Context context)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ActivationWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
    }
}
