package david.naor.com.memorygame;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import tyrantgit.explosionfield.ExplosionAnimator;
import tyrantgit.explosionfield.ExplosionField;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String GAME_LEVEL = "GAME_LEVEL";
    public static final int GAME_ACTIVITY_RESULT = 1;

    private int             maxTries;
    private int             tries;
    private int             level;
    private int[]           cardIds;
    private Card            first;
    private Card            second;
    private ArrayList<Card> cardArrayList;
    private List<Bitmap>    images;
    TextView                result;
    private int             matches;
    private int             pairs;
    boolean                 active;
    private Map<Integer, Integer>  pairedMap;
    private TableLayout     table;

    private ExplosionField explosionField;
    private MyExploder myExploder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initMembers();

        initComponents();
    }

    private void initMembers(){
        Bundle intentData = getIntent().getExtras();
        level = intentData.getInt(GAME_LEVEL);
        pairs = level*level/2;
        maxTries = pairs * 2;
        first = null;
        second= null;
        matches = 0;
        tries = 0;
        active = true;
        cardIds = new int[pairs];
        for(int j = 0; j < cardIds.length; j++)
            cardIds[j] = 0;
        pairedMap = new HashMap ();
        cardArrayList = new ArrayList<>();
        explosionField = ExplosionField.attach2Window(this);
        myExploder = new MyExploder(this);
        myExploder.setDuration(2000);
        myExploder.setExplosionField(explosionField);
    }

    private void initComponents(){
        initImages();
        result = (TextView) findViewById(R.id.result);
        result.setText(getString(R.string.act_game_activity_matches) + matches);
        table = (TableLayout) findViewById(R.id.ect_game_activity_cards_table);
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
                c.setScaleType(ImageView.ScaleType.CENTER_CROP);
                row.addView(c);
                cardArrayList.add(c);
            }
            table.addView(row);
        }

    }


    @Override
    public void onClick(View v) {
        if (!active)
            return;

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
        if (first.isMatch(second)) {
            matches ++;
            pairedMap.put(Integer.valueOf(first.getId()), Integer.valueOf(second.getId()));
            resetCards();
        }
        else {
            tries ++;
            active = false;
            flipCards();
        }
        result.setText(getString(R.string.act_game_activity_matches) + matches);
        checkGame();
    }


    private void initImages(){
        images = new ArrayList<Bitmap>();

        images.add(getScaledBitmap(R.mipmap.img_001));
        images.add(getScaledBitmap(R.mipmap.img_002));
        images.add(getScaledBitmap(R.mipmap.img_003));
        images.add(getScaledBitmap(R.mipmap.img_004));
        images.add(getScaledBitmap(R.mipmap.img_005));
        images.add(getScaledBitmap(R.mipmap.img_006));
        images.add(getScaledBitmap(R.mipmap.img_007));
        images.add(getScaledBitmap(R.mipmap.img_008));
        images.add(getScaledBitmap(R.mipmap.img_009));
        images.add(getScaledBitmap(R.mipmap.img_010));
        images.add(getScaledBitmap(R.mipmap.img_011));
        images.add(getScaledBitmap(R.mipmap.img_012));
    }

    private Bitmap getScaledBitmap(int id){
        Bitmap b = BitmapFactory.decodeResource(getResources(), id);
        b = Bitmap.createScaledBitmap(b, 250, 250, true);
        return b;
    }

    private int getCardId(){
        int chosen = -1;
        //random of number
        Random r = new Random();

        while (chosen < 0){
            int n = r.nextInt(cardIds.length);
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

    private void flipCards(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                first.flip();
                second.flip();
                resetCards();
                active = true;
            }
        }, 1000);
    }

    private void resetCards(){
        first = null;
        second = null;
    }

    private void checkGame(){
        if (tries > maxTries && matches != pairs)
            gameOver();
        if (matches != pairs)
            return;

        gameWin();
    }

    private void flipBackToGame(){
        if (pairedMap.isEmpty())
            return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Integer key = (Integer) pairedMap.keySet().toArray()[0];
                Integer value = pairedMap.get(key);
                Card first = (Card) findViewById(key);
                Card second = (Card) findViewById(value);
                first.flip();
                second.flip();
                matches--;
                result.setText(getString(R.string.act_game_activity_matches) + matches);
            }
        }, 2000);
    }

    private void gameWin(){
        myExploder.setDuration(1000);
        for (Card c: cardArrayList)
            animete(c);


        result.setText(R.string.act_game_activity_well_done);
        Toast.makeText(this, R.string.act_game_activity_well_done, Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        int score = maxTries - tries;
        intent.putExtra("score", score * level);
        setResult(RESULT_OK, intent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishGame();
            }
        }, 2000);
    }

    private void gameOver(){
        result.setText(R.string.act_game_activity_game_over);
        Toast.makeText(this, R.string.act_game_activity_game_over, Toast.LENGTH_LONG).show();

        myExploder.setDuration(2000);
        myExploder.explode(table);

        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishGame();
            }
        }, 2000);
    }

    protected void finishGame(){
        finish();
    }

    private void animete(Card card){
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator xAnimation = ObjectAnimator.ofFloat(card,    "x", 420f);
        xAnimation.setDuration(1000);

        ObjectAnimator yAnimation = ObjectAnimator.ofFloat(card,    "y", 1120f);
        yAnimation.setDuration(2000);

        animatorSet.playTogether(xAnimation, yAnimation);
        animatorSet.start();
    }
}
