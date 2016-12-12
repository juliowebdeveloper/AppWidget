package shido.com.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class AKittyWidget extends AppWidgetProvider{

    public final static String ACTION_EXPLICIT_UPDATE ="shido.com.appwidget.action.EXPLICIT_UPDATE";
    public final static long EXPLICIT_UPDATE_INTERVAL = 5000; // Nunca ter um update numa widget de 5 segundos  - Irá acabar com a bateria

    public AKittyWidget() {
    }



    //Metodos usados para o AlarmManager
    static PendingIntent getExplicitUpdatePendingIntent(Context context){
        Intent intent = getExplicitIntent(context);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }


    static Intent getExplicitIntent(Context context){
        Intent intent = new Intent(context, AKittyWidget.class);
        //Setando essa action que será vista no androidmanifest.xml
        intent.setAction(ACTION_EXPLICIT_UPDATE);
        return intent;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        //On receibe faz o parsing das intents que vem e chama os metodos onUpdate, onDeleted, assim por diante.
        String action = intent.getAction(); //Buscando pela action que definimos para ai fazermos o que desejamos.
        Log.d("ACTION", action);
        if(action.equalsIgnoreCase(ACTION_EXPLICIT_UPDATE))
            doExplicitUpdate(context, intent);
        else
            super.onReceive(context, intent);
    }

    private void doExplicitUpdate(Context context, Intent intent) {
        //Queremos que o metodo onUpdate seja chamado, porém preciso passar informações que ele não tem
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //Recuperando os Ids atraves do ComponentName
        ComponentName appWidgetComponentName = new ComponentName(context, AKittyWidget.class);


        //pegando a partir dos extras só aquele Id da widget para fazer o update explicito que foi
        //setado na configuration activity e colocando um invalid como default
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        //Se vier o valor default(ou seja invalido) fará para todos os componentes, se nao, fará o update só para aquele id
        int [] appWidgetIds = appWidgetId ==AppWidgetManager.INVALID_APPWIDGET_ID ?
                appWidgetManager.getAppWidgetIds(appWidgetComponentName):
                new int[]{appWidgetId}; //Caso tenha um id, ele retorna um novo array de ints contendo só o appWidgetId

        //Caso não esteja nulo, iremos chamar o onUpdate normalmente passando os valor

        if(appWidgetIds!=null && appWidgetIds.length>0){
            onUpdate(context, appWidgetManager, appWidgetIds);
        }




    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Cada Widget que vai pra homescreen tem um id unico
        Log.i("ONUPDATE",String.valueOf(appWidgetIds.length));
        for(int i = 0; i <appWidgetIds.length; i++){
            int appWidgetId = appWidgetIds[i];
            Log.i("INDEX ID", String.valueOf(appWidgetId));

            //Chamando a remoteviews pelo metodo criado (Mudança passando o appWidgetId pois será necessario na hora de usar o options bundle
            RemoteViews appWidgetViews = getWidgetRemoteViews(context, appWidgetId);
            //Atualizando aquela widget com o novo layout contendo as intents, actions e afins (seria um onCreate da Widget)
            appWidgetManager.updateAppWidget(appWidgetId, appWidgetViews);
        }
    }

    //Criar as RemoteViews que representa a Widget e amarrar com a UI
    public static RemoteViews getWidgetRemoteViews(Context context, int appWidgetId){
        Intent button1Intent = new Intent(context, ShowImageActivity.class);
        button1Intent.putExtra(ShowImageActivity.EXTRA_IMAGE_RESOURCE_ID, R.drawable.orangecat);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context,0,button1Intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //Flag Current caso haja outra igual a essa, use essa ao inves da outra

        //Pegando o bundle options para ver qual layout que será relacionado à widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);

        //Pegando a info do options
        boolean use2Buttons = options.getBoolean(ConfigurationActivity.USE_2_BUTTONS, true);

        //Se o use2Button existir então ele carrega o layout com 2 botões, se não com 1 botão
        int resourceId = use2Buttons ? R.layout.a_simple_kitty_widget : R.layout.a_simple_kitty_widget_one_button;

        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(), resourceId);

        //Amarrando as actions nos botões usando PendingIntents
        appWidgetViews.setOnClickPendingIntent(R.id.button, pendingIntent1);

        if(use2Buttons){
            //Cria a intent e pending intent para o botão 2, se não fica só com um
        }


        return appWidgetViews;
    }



}
