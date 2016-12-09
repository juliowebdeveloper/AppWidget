package shido.com.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class AKittyWidget extends AppWidgetProvider{
    public AKittyWidget() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Cada Widget que vai pra homescreen tem um id unico
        Log.i("ONUPDATE",String.valueOf(appWidgetIds.length));
        for(int i = 0; i <appWidgetIds.length; i++){
            int appWidgetId = appWidgetIds[i];
            Log.i("INDEX ID", String.valueOf(appWidgetId));

            //Chamando a remoteviews pelo metodo criado
            RemoteViews appWidgetViews = getWidgetRemoteViews(context);
            //Atualizando aquela widget com o novo layout contendo as intents, actions e afins
            appWidgetManager.updateAppWidget(appWidgetId, appWidgetViews);
        }
    }

    //Criar as RemoteViews que representa a Widget e amarrar com a UI
    public static RemoteViews getWidgetRemoteViews(Context context){
        Intent button1Intent = new Intent(context, ShowImageActivity.class);
        button1Intent.putExtra(ShowImageActivity.EXTRA_IMAGE_RESOURCE_ID, R.drawable.orangecat);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context,0,button1Intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Flag Current caso haja outra igual a essa, use essa ao inves da outra




        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(), R.layout.a_simple_kitty_widget);

        //Amarrando as actions nos botões usando PendingIntents
        appWidgetViews.setOnClickPendingIntent(R.id.button, pendingIntent1);


        return appWidgetViews;
    }



}
