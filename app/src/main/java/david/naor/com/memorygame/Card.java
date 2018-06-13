package david.naor.com.memorygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import tyrantgit.explosionfield.ExplosionField;

public class Card extends ImageView{
    private long cardId;
    private Bitmap cardImage;
    private Bitmap defaultImage;
    private boolean flipped;

    public Card (Context context, long cardId, boolean inactive){
        super(context);
        this.cardId = cardId;
        flipped = true;
        defaultImage = getScaledBitmap(inactive ? R.mipmap.not_available_card : R.mipmap.card_back);
        cardImage = getScaledBitmap(R.mipmap.card_back);
        flip();
    }

    private Bitmap getScaledBitmap(int id){
        Bitmap b = BitmapFactory.decodeResource(getResources(), id);
        b = Bitmap.createScaledBitmap(b, 250, 250, true);
        return b;
    }

    public boolean isMatch(Card other){
        return this.cardId == other.cardId;
    }

    public boolean isFlipped(){
        return flipped;
    }

    public void flip() {
        if (flipped) {
            setImageBitmap(defaultImage);
            flipped = false;
        }else{
            setImageBitmap(cardImage);
            flipped = true;
        }
    }

    public void setCardImage(Bitmap cardImage){
        this.cardImage = cardImage;
    }

}
