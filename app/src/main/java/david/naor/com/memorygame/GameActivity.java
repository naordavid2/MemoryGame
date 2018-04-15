package david.naor.com.memorygame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String GAME_LEVEL = "GAME_LEVEL";
    private int             level;
    private int[]           cardIds;
    private Card            first;
    private Card            second;
    private List<Bitmap>    images;
    TextView                result;

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
        first = null;
        second= null;
        cardIds = new int[level*level/2];
        for(int j = 0; j < cardIds.length; j++)
            cardIds[j] = 0;
    }

    private void initComponents(){
        initImages();
        result = (TextView) findViewById(R.id.result);
        result.setText("result");
        TableLayout table = (TableLayout) findViewById(R.id.ect_game_activity_cards_table);
        int n = 100;
        int inactiveCard = findInactiveCard() - 1;
        for (int i=0; i<level; i++) {
            TableRow row = new TableRow(this);
            for (int j = 0; j < level; j++) {
                boolean inactive = (i) == inactiveCard && j == inactiveCard;

                int cardId = inactive ? - 1 : getCardId();
                Card c = new Card(this, cardId, inactive);
                c.setId(n++);

                if (!inactive) {
                    c.setCardImage(images.get(cardId));
                    c.setOnClickListener(this);
                }
                row.addView(c);
            }
            table.addView(row);
        }
    }

    @Override
    public void onClick(View v) {
        Card c = (Card) findViewById(v.getId());
        if (c == null)
            return;

        if (first == null){
            first = c;
            first.flip();
            return;
        }
        if (c.isFlipped())
            return;

        second = c;
        second.flip();
        if (first.isMatch(second))
            result.setText("Match");
        else {
            result.setText("MisMatch");
            first.flip();
            second.flip();
        }

        first = null;
        second = null;
    }


    private void initImages(){
        images = new ArrayList<Bitmap>();
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_001));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_002));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_003));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_004));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_005));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_006));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_007));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_008));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_009));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_010));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_011));
        images.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_012));
    }

    private int getCardId(){
        int chosen = -1;
        //random of number
        Random r = new Random();

        while (chosen < 0){
            int n = r.nextInt(cardIds.length);
            System.out.println("rand = " + n);
            if (n < cardIds.length && cardIds[n] < 2)
            {
                cardIds[n]++;
                chosen = n;
            }
        }
        return chosen;
    }

    private int findInactiveCard(){
        if (level%2 == 0)
            return -1;
        else
            return (level / 2) + 1;
    }
}
