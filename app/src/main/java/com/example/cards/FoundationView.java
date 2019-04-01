package com.example.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewParent;

import java.util.ArrayList;

public class FoundationView extends CardView implements CardHolder {

    void init() {
        setTouchable(false);
        setOnDragListener(foundationOnDragListener);
        setCard(new Card(0, Suit.SPADE));
    }

    FoundationView(Context c) {
        super(c);
        init();
    }

    FoundationView(Context c, AttributeSet attr) {
        super(c, attr);
        init();
    }

    FoundationView(Context c, AttributeSet attr, int def) {
        super(c, attr, def);
        init();
    }

    @Override
    public void addCards(ArrayList<CardView> cards) {
        if(cards == null || cards.size() != 1)
            return;

        CardView cv = cards.get(0);
        setCard(cv.card);
    }

    @Override
    public void removeCards(ArrayList<CardView> cards) {
        return;
    }

    View.OnDragListener foundationOnDragListener = new View.OnDragListener() {

        boolean dropSuccess;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            CardView foundation = (CardView)v;
            View view = (View) event.getLocalState();
            switch(event.getAction()) {
                // Listen for drag event only if card can be added to foundation
                case DragEvent.ACTION_DRAG_STARTED:
                    if (view instanceof CardView) {
                        CardView draggedView = (CardView) view;
                        int value = draggedView.card.value;
                        Suit suit = draggedView.card.suit;
                        if ((value == 1 && foundation.card.value == 0) ||
                                (value - foundation.card.value == 1
                                        && suit.equals(foundation.card.suit))) {
                            return true;
                        }
                    }
                    dropSuccess = false;
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    dropSuccess = true;
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    if(dropSuccess) {
                        CardHolder parentLayout = (CardHolder)view.getParent();
                        ArrayList<CardView> list = new ArrayList<>();
                        list.add((CardView)(view));
                        parentLayout.removeCards(list);
                        addCards(list);
                        SolitaireActivity.addMove(new Moves(parentLayout, FoundationView.this, list));
                        return true;
                    }
                    break;
            }
            return false;
        }
    };
}
