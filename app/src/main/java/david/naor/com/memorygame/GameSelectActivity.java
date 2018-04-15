package david.naor.com.memorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameSelectActivity extends AppCompatActivity {

    public  static final String     USER_NAME       = "USER_NAME";
    public  static final String     USER_AGE        = "USER_AGE";
    private static final int        EASY_LEVEL      = 2;
    private static final int        MID_LEVEL       = 4;
    private static final int        HARD_LEVEL      = 5;

    private String      name;
    private int         age;
    private TextView    textView_name;
    private TextView    textView_age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select);

        initMembersFromIntent();
        initComponents();

    }


    private void initComponents(){
        String nameText = String.format(getString(R.string.act_game_select_user_name), name);
        textView_name = (TextView) findViewById(R.id.act_game_select_text_view_name);
        textView_name.setText(nameText);

        String ageText = String.format(getString(R.string.act_game_select_user_age), age);
        textView_age = (TextView) findViewById(R.id.act_game_select_text_view_age);
        textView_age.setText(ageText);

        Button easyBtn = (Button) findViewById(R.id.act_game_select_button_easy_level);
        easyBtn.setText(String.format(getString(R.string.act_game_select_easy_level),EASY_LEVEL,EASY_LEVEL));
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameByLevel(EASY_LEVEL);
            }
        });

        Button midBtn = (Button) findViewById(R.id.act_game_select_button_mid_level);
        midBtn.setText(String.format(getString(R.string.act_game_select_mid_level),MID_LEVEL,MID_LEVEL));
        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameByLevel(MID_LEVEL);
            }
        });

        Button hardBtn = (Button) findViewById(R.id.act_game_select_button_hard_level);
        hardBtn.setText(String.format(getString(R.string.act_game_select_hard_level),HARD_LEVEL,HARD_LEVEL));
        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameByLevel(HARD_LEVEL);
            }
        });
    }

    private void initMembersFromIntent(){
        Bundle intentData = getIntent().getExtras();
        name = intentData.getString(USER_NAME);
        age = intentData.getInt(USER_AGE);
    }

    private void goToGameByLevel(int level){
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(GameActivity.GAME_LEVEL, level);

        startActivity(intent);
    }
}
