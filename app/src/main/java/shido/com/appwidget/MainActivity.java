package shido.com.appwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {


    private Button btnChangeWidget;
    private Button btnStartAlarm;
    private Button btnStopAlarm;
    private Button btnGoToConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnChangeWidget = (Button) findViewById(R.id.changeWidget);
        btnChangeWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
                ComponentName appWidgetComponentName = new ComponentName(MainActivity.this, AKittyWidget.class);
                //Com isso ele traz todos os ids das widgets que estiverem implementando esse component em especifico(AKittyWidget)
                int [] appWidgetIds = appWidgetManager.getAppWidgetIds(appWidgetComponentName);
                for(int i = 0; i < appWidgetIds.length;i++){
                    int appWidgetId = appWidgetIds[i];
                    Log.d("Widget it", String.valueOf(appWidgetId));

                    //Usando o getRemoteViews para buscar as RemoteViews relacionadas à essa Widget
                    RemoteViews remoteViews = AKittyWidget.getWidgetRemoteViews(MainActivity.this, appWidgetId);
                    //Não podemos simplesmente acessar a view e mudar o texto do textview ja que a view não existe ainda.
                    //Ela só foi inflada dentro da widget, nao na aplicação
                    //Para isso iremos utilizar o queued methods
                    remoteViews.setCharSequence(R.id.textView, "setText", "MEOW MEOW"); //Identifica a View, o Metodo que quero chamar e passar a sequencia de caracteres para a mudança
                     appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                    //Irá aplicar as mudanças
                }



            }
        });


        btnStartAlarm = (Button) findViewById(R.id.startAlarm);
        btnStartAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se estiver dentro de um Fragment, usar o Context = getActivity
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                PendingIntent pendingIntent = AKittyWidget.getExplicitUpdatePendingIntent(MainActivity.this);

                //Dizendo ao alarma manager quando começar e com que frequencia quero roda-lo
                long currentTimeMillis = System.currentTimeMillis();
                //Nunca usar um intervalo tão pequeno - Apenas para teste
                long intervalMillis = AKittyWidget.EXPLICIT_UPDATE_INTERVAL;
                /*InexactRepeating é melhor para bateria:
                O sistema irá verificar todas as coisas que tem que fazer e tentar agrupar o timing desse evento
                com outros eventos, para que nao precise acordar o device só pra fazer o seu evento
                AlarmManager.ELAPSED_REALTIME  = System's elapsed time
                AlarmManager.RTC = Current Time
                WAKE VERSION = irá acordar o dispositivo para fazer essa chamada
                NON-WAKE = não irá acordar. Prefirá isso

                */

                alarmManager.setInexactRepeating
                        (AlarmManager.RTC, currentTimeMillis + intervalMillis, intervalMillis, pendingIntent);
                // irá enviar um broadcast daqui 5 segundos e a cada 5 segundos passando a Pending Intent

            }
        });


        btnStopAlarm = (Button) findViewById(R.id.stopAlarm);
        btnStopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                PendingIntent pendingIntent = AKittyWidget.getExplicitUpdatePendingIntent(MainActivity.this);

                //Para fazer o cancel ele se basea em uma pending intent, por isso necessitamos pegar ela novamente
                alarmManager.cancel(pendingIntent);

            }
        });


        btnGoToConfig = (Button) findViewById(R.id.goToConfiguration);
        btnGoToConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ConfigurationActivity.class);
                startActivity(i);
            }
        });

    }

    private static PendingIntent getMyWidgetAlarmPendingIntent(Context context){
        PendingIntent pendingIntent = null;

        return pendingIntent;
    }





}
