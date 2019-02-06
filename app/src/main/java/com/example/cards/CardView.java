package com.example.cards;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CardView extends AppCompatImageView {

    Card card;
    int imageResource;
    boolean faceDown;

    CardView(Context c) {
        super(c);
        card = null;
    }

    CardView(Context c, AttributeSet attr) {
        super(c, attr);
    }

    CardView(Context c, AttributeSet attr, int def) {
        super(c, attr, def);
    }

    CardView(Context c, Card _card) {
        super(c);
        card = _card;
        imageResource = getResources().getIdentifier(getFileNameOfCardImage(),
                "drawable", c.getPackageName());
        setImageResource(imageResource);
    }

    void setCard(Card newCard) {
        card = newCard;
    }

    void setFaceDown(boolean value) {
        faceDown = value;
        setImageResource(R.drawable.card_back);
    }

    String getFileNameOfCardImage() {
        String fileName = "";
        switch (card.value) {
            case 1:
                fileName += "ace";
                break;
            case 2:
                fileName += "two";
                break;
            case 3:
                fileName += "three";
                break;
            case 4:
                fileName += "four";
                break;
            case 5:
                fileName += "five";
                break;
            case 6:
                fileName += "six";
                break;
            case 7:
                fileName += "seven";
                break;
            case 8:
                fileName += "eight";
                break;
            case 9:
                fileName += "nine";
                break;
            case 10:
                fileName += "ten";
                break;
            case 11:
                fileName += "jack";
                break;
            case 12:
                fileName += "queen";
                break;
            case 13:
                fileName += "king";
                break;
            default:
                throw new IllegalArgumentException("Illegal card value");
        }
        fileName += "_of_";
        switch (card.suit) {
            case SPADE:
                fileName += "spades";
                break;
            case HEART:
                fileName += "hearts";
                break;
            case DIAMOND:
                fileName += "diamonds";
                break;
            case CLUB:
                fileName += "clubs";
                break;
            default:
                throw new IllegalArgumentException("Illegal Suit enum value");
        }
        return fileName;
    }
}

