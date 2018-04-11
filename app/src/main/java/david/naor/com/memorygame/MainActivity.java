package david.naor.com.memorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private int         user_age;
    private String      user_name;
    private EditText    editText_user_name;
    private EditText    editText_user_age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        Button startBtn = (Button) findViewById(R.id.act_main_button_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput())
                    goToGameSelectActivity();
            }
        });


    }

    private void initComponents(){
        editText_user_name  = (EditText) findViewById(R.id.act_main_edit_text_user_name);
        editText_user_age   = (EditText) findViewById(R.id.act_main_edit_text_user_age);
    }


    private boolean validateInput(){
        boolean valid = true;
        if (editText_user_name.getText().length() == 0 ) {
            editText_user_name.setError(getString(R.string.err_empty_user_name));
            valid = false;
        }
        if (editText_user_age.getText().length() == 0) {
            editText_user_age.setError(getString(R.string.err_empty_user_age));

            valid = false;
        }
        return valid;
    }

    private void goToGameSelectActivity(){
        Intent intent = new Intent(this, GameSelectActivity.class);
        intent.putExtra("user_name", editText_user_name.getText().toString());
        intent.putExtra("user_age", editText_user_age.getText().toString());
        startActivity(intent);
    }
}
