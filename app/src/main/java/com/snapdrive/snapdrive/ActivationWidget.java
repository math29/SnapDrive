package com.snapdrive.snapdrive;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by crocus on 01/04/15.
 */
public class ActivationWidget extends AppWidgetProvider{

    public static final String ACTIVER= "ACTIVER";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        final int N = appWidgetIds.length;

        // Pour chaque AppWidget MonWidgetDeveloppez (n'oubliez pas qu'on peut en ajouter tant qu'on veut), on les met à jour :
        for (int i = 0; i < N; i++)
        {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    // Cette méthode est entièrement libre, à vous de la modifier comme bon vous semble. Voici toutefois une base minimaliste
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        AppPreferences prefs = new AppPreferences(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout); // On récupère les Views de notre layout
        if(prefs.isActivate()){
            views.setImageViewResource(R.id.img,R.drawable.snapdrive_on);
        }else{
            views.setImageViewResource(R.id.img,R.drawable.snapdrive_off);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views); // On met ensuite à jour l'affichage du widget

        Intent i = new Intent(context, ActivationWidget.class);
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        i.setAction(ACTIVER);

        // On lie l'intent à l'action
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.img, pendingIntent); // L'id de la view qui réagira au clic sur le widget.
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals(ACTIVER)){
            AppPreferences prefs = new AppPreferences(context);
            boolean activation = !prefs.isActivate();
            prefs.activate(activation);

            Intent i = new Intent(context, TTSService.class);
            if(prefs.isActivate()) {
                i.putExtra("action", "activation");
            }else{
                i.putExtra("action","desactivation");
            }
            context.startService(i);

            Bundle extras = intent.getExtras();
            if(extras!=null) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisAppWidget = new ComponentName(context.getPackageName(), ActivationWidget.class.getName());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

                onUpdate(context, appWidgetManager, appWidgetIds);
            }

        }
    }
}

