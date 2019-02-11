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
        setOnTouchListener(cardViewOnTouchListener);
        setOnDragListener(cardViewOnDragListener);
        card = null;
    }

    CardView(Context c, AttributeSet attr) {
        super(c, attr);
        setOnTouchListener(cardViewOnTouchListener);
        setOnDragListener(cardViewOnDragListener);
        card = null;
        imageResource = R.drawable.card_blank;
        faceDown = false;
    }

    CardView(Context c, AttributeSet attr, int def) {
        super(c, attr, def);
        setOnTouchListener(cardViewOnTouchListener);
        setOnDragListener(cardViewOnDragListener);
        card = null;
        imageResource = R.drawable.card_blank;
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
    }

    void setCard(Card newCard) {
        card = newCard;
        imageResource = getResources().getIdentifier(getFileNameOfCardImage(),
                "drawable", getContext().getPackageName());
        setImageResource(imageResource);
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
                    return true;
                }
                //otherwise drag card
                // TO DO:       implement dragging of columns of cards
                else {
                    ClipData clipData = ClipData.newPlainText("", "");
                    View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(clipData, myShadowBuilder, v, 0);
                    v.setVisibility((View.INVISIBLE));
                    return true;
                }
            }
            return false;
        }
    };

    View.OnDragListener cardViewOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //Toast.makeText(getContext(), getFileNameOfCardImage() + " started drag", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Toast.makeText(getContext(), getFileNameOfCardImage() + " entered drag", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Toast.makeText(getContext(), getFileNameOfCardImage() + " exited drag", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DROP:
                    Toast.makeText(getContext(), getFileNameOfCardImage() + " dropped on", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Toast.makeText(getContext(), "ACTION_DRAG_ENDED with result of " + event.getResult(), Toast.LENGTH_SHORT).show();
                    if(!event.getResult()) { // getResult == true if card was moved
                        View view = (View) event.getLocalState();
                        view.setVisibility(VISIBLE);
                    }
                    break;
            }
            return false;
        }
    };
}

