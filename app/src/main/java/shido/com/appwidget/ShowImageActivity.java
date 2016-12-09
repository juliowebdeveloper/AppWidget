package shido.com.appwidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShowImageActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_RESOURCE_ID = "IMAGE RESOURCE ID";
    public static final int DEFAULT_IMAGE_RESOURCE_ID = R.drawable.blackcaticon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
    }
}
