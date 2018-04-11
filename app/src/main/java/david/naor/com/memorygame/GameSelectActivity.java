package david.naor.com.memorygame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);
        Bundle intentData = getIntent().getExtras();
        String name = intentData.getString("user_name");
        String age = intentData.getString("user_age");

        TextView textView_name = (TextView) findViewById(R.id.act_game_select_name);
        textView_name.setText(name);

        TextView textView_age = (TextView) findViewById(R.id.act_game_select_age);
        textView_age.setText(age);
    }
}
