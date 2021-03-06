package com.example.cards;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class CardView extends AppCompatImageView {

    Card card;
    int imageResource;
    boolean faceDown;

    CardView(Context c) {
        super(c);
        setOnTouchListener(null);
        setOnDragListener(null);
        card = null;
        imageResource = R.drawable.card_blank;
        setImageResource(imageResource);
        faceDown = false;
    }

    CardView(Context c, AttributeSet attr) {
        super(c, attr);
        setOnTouchListener(null);
        setOnDragListener(null);
        card = null;
        imageResource = R.drawable.card_blank;
        setImageResource(imageResource);
        faceDown = false;
    }

    CardView(Context c, AttributeSet attr, int def) {
        super(c, attr, def);
        setOnTouchListener(null);
        setOnDragListener(null);
        card = null;
        imageResource = R.drawable.card_blank;
        setImageResource(imageResource);
        faceDown = false;
    }

    CardView(Context c, Card _card) {
        super(c);
        setOnTouchListener(cardViewOnTouchListener);
        setOnDragListener(cardViewOnDragListener);
        card = _card;
        imageResource = getResources().getIdentifier(getFileNameOfCardImage(),
                "drawable", c.getPackageName());
        setImageResource(imageResource);
        faceDown = false;
    }

    CardView(Context c, CardView cv) {
        super(c);
        card = cv.card;
        imageResource = cv.imageResource;
        setImageResource(imageResource);
        faceDown = cv.faceDown;
        setOnTouchListener(cardViewOnTouchListener);
        setOnDragListener(cardViewOnDragListener);
    }

    @Override
    public boolean equals(Object other) {
        if(other == this)
            return true;

        if(! (other instanceof CardView))
            return false;

        CardView cv = (CardView)other;
        if(this.card == null || cv.card == null)
            return (this.card == null && cv.card == null);

        return (this.card.value == cv.card.value &&
                this.card.suit == cv.card.suit);
    }

    void setCard(Card newCard) {
        card = newCard;
        imageResource = getResources().getIdentifier(getFileNameOfCardImage(),
                "drawable", getContext().getPackageName());
        setImageResource(imageResource);
    }

    void setTouchable(boolean val) {
        if(val)
            setOnTouchListener(cardViewOnTouchListener);
        else
            setOnTouchListener(null);
    }

    void setFaceDown(boolean value) {
        faceDown = value;
        if(faceDown)
            setImageResource(R.drawable.card_back);
        else
            setImageResource(imageResource);
    }

    String getFileNameOfCardImage() {
        if(card == null)
            return "card_blank";

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
                return "card_blank";
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
                return "card_blank";
        }
        return fileName;
    }

    OnTouchListener cardViewOnTouchListener = new OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                CardView card = (CardView)v;
                // flip card if face down
                if(card.faceDown) {
                    card.setFaceDown(false);
                    SolitaireActivity.addMove(new Moves((ColumnLayout)getParent()));
                    return true;
                }
                //otherwise drag card
                else {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(clipData, myShadowBuilder, v, 0);
                    return true;
                }
            }
            return false;
        }
    };

    View.OnDragListener cardViewOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            CardView card = (CardView)v;
            View dragged = (View)event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if(dragged instanceof CardView && card.equals(dragged)) {
                        v.setVisibility(INVISIBLE);
                        return true;
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setVisibility(VISIBLE);
                    break;
            }
            return false;
        }
    };
}

