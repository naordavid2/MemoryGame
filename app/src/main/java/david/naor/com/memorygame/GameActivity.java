package david.naor.com.memorygame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GameActivity extends AppCompatActivity {
    public static final String GAME_LEVEL = "GAME_LEVEL";
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initMembersFromIntent();

        initComponents();
    }

    private void initMembersFromIntent(){
        Bundle intentData = getIntent().getExtras();
        level = intentData.getInt(GAME_LEVEL);

    }

    private void initComponents(){
        TableLayout table = (TableLayout) findViewById(R.id.ect_game_activity_cards_table);

        for (int i=0; i<level; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < level; j++) {
                Button b = new Button(this);
                b.setText(String.format("row: %d\ncol: %d", i, j));
                row.addView(b);
            }
            table.addView(row);
        }

    }
}
