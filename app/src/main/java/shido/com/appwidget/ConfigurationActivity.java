package shido.com.appwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ConfigurationActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private Button btnChangeConfiguration;

    public final static String USE_2_BUTTONS = "use 2 buttons";



    //Essa activity será chamada como uma "For result" assim que a widget for colocada na homescreen
    //Após o resultado ser retornado (ResultValue) ele irá aplicar de acordo com o que for setado
    //nos radios buttons. Está configurado
    //                 <action android:name="android.appwidget.action.APPWIDGET_CONFIGURE" />
    //No manifest, dizendo que essa class que é responsavel por essa configuração
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuration);
        //Ter certeza que a Widget nao foi colocada caso o usuario abandone a tela de configuração
        setResult(RESULT_CANCELED);

        //Irá colocar no options o valor necessario e retornar para a MainActivity esse resultado.

        radioGroup = (RadioGroup) findViewById(R.id.radioGroupSelect);
        radioButton1 = (RadioButton) findViewById(R.id.radio1Button);
        radioButton2 = (RadioButton)findViewById(R.id.radio2Button);
        btnChangeConfiguration = (Button) findViewById(R.id.btnChangeConfiguration);
        btnChangeConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity =  ConfigurationActivity.this;
                boolean use2Buttons = radioGroup.getCheckedRadioButtonId() == R.id.radio2Button;
                Log.i("[Config] use2Buttons=%b", String.valueOf(use2Buttons));
                    int appWidgetId = resolveAppWidgetId(activity);
                    //Conceito de Options no appWidget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(activity);
                Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId); //Traz a lista de options para aquela widgetId
                if(options!=null){
                    options = new Bundle();
                }
                options.putBoolean(USE_2_BUTTONS, use2Buttons);
                appWidgetManager.updateAppWidgetOptions(appWidgetId,options);


                //Como mesmo fazendo essa mudança a Widget não atualiza, precisamos explicitamente atualizar
               // appWidgetManager.updateAppWidget(appWidgetId);
                //ou usar o provider que já definimos antes
                Intent providerIntent = AKittyWidget.getExplicitIntent(activity);
                //Provendo o id De uma widget só
                providerIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                //Enviando o Broadcast customizado para só aquela appwidgetId

                activity.sendBroadcast(providerIntent);



                //Notificando a Intent que possui um resultValue e retornando o resultok
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                activity.setResult(RESULT_OK, resultValue);

                activity.finish();
            }
        });
    }

    private int resolveAppWidgetId(Activity activity) {
        int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
        Intent createIntent = ConfigurationActivity.this.getIntent();

        Bundle extras = createIntent.getExtras();
        if(extras!=null ){
            //Pegando O AppWidgetId e caso nao tenho retorna um invalid
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        }
        return appWidgetId;
    }


}
