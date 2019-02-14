package com.example.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ColumnLayout extends RelativeLayout implements CardHolder {

    ArrayList<CardView> column;

    public ColumnLayout(Context context) {
        super(context);
        column = new ArrayList<>();
        setOnDragListener(columnOnDragListener);
    }

    public ColumnLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        column = new ArrayList<>();
        setOnDragListener(columnOnDragListener);
    }

    public ColumnLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        column = new ArrayList<>();
        setOnDragListener(columnOnDragListener);
    }

    @Override
    public void addCards(ArrayList<CardView> cards) {
        if(cards == null || cards.size() != 1)
            return;

        addCard(cards.get(0));
    }

    @Override
    public void removeCards(ArrayList<CardView> cards) {
        if(cards == null || cards.size() != 1)
            return;

        removeCard(cards.get(0));
        CardView botCard = getBottomCard();
        if(botCard != null)
            botCard.setTouchable(true);
    }

    public void addCard(CardView cv) {
        int marginSize;
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        CardView botCard = getBottomCard();
        if(botCard == null || botCard.faceDown) {
            marginSize = 20;
        }
        else {
            marginSize = 30;
        }
        //********************************
        //              TO DO
        //*********************************
        // Margin will need to be adjusted based on how many facedown cards there are
        // numFaceDowns * 20 + numFaceUps * 30
        params.setMargins(0, column.size() * marginSize, 0, 0);
        cv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        cv.setAdjustViewBounds(true);
        column.add(cv);
        addView(cv, params);
        cv.setVisibility(VISIBLE);
    }

    public void removeCard(CardView cv) {
        if(cv.equals(getBottomCard())) {
            column.remove(cv);
            this.removeView(cv);
        }
    }

    public CardView getBottomCard() {
        if(column.size() <= 0)
            return null;
        return column.get(column.size() - 1);
    }

    private boolean dropable(CardView base, CardView dropped) {
        if(base == null)
            return dropped.card.value == 13;

        if(base.card.value - dropped.card.value != 1)
            return false;

        Suit droppedSuit = dropped.card.suit;
        switch(base.card.suit) {
            case CLUB:
            case SPADE:
                if(droppedSuit == Suit.HEART || droppedSuit == Suit.DIAMOND)
                    return true;
                break;
            case HEART:
            case DIAMOND:
                if(droppedSuit == Suit.CLUB || droppedSuit == Suit.SPADE)
                    return true;
                break;
            default:
                throw new IllegalArgumentException("Card has illegal suit value");
        }
        return false;
    }

    RelativeLayout.OnDragListener columnOnDragListener = new RelativeLayout.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            View view = (View)event.getLocalState();
            CardView cv;
            switch(event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if(view instanceof CardView) {
                        cv = (CardView) view;
                        if(dropable(getBottomCard(), cv))
                            return true;
                    }
                    return false;
                case DragEvent.ACTION_DROP:
                    ViewParent parentLayout = view.getParent();
                    if(parentLayout instanceof ColumnLayout) {
                        ArrayList<CardView> list = new ArrayList<CardView>();
                        list.add((CardView)view);
                        ((ColumnLayout) parentLayout).removeCards(list);
                        addCards(list);
                        return true;
                    }
                    else if(parentLayout instanceof DrawLayout) {
                        ArrayList<CardView> list = new ArrayList<CardView>();
                        list.add((CardView)view);
                        ((DrawLayout) parentLayout).removeCards(list);
                        addCards(list);
                        return true;
                    }
                    else {
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    return false;
            }
            return false;
        }
    };
}
