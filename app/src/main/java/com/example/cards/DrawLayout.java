package com.example.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class DrawLayout extends RelativeLayout implements CardHolder {

    RelativeLayout drawLayout;
    ArrayList<CardView> currentDraw;
    ArrayList<CardView> drawStack;

    public DrawLayout(Context context) {
        super(context);
        drawLayout = findViewById(R.id.draw_layout);
        currentDraw = new ArrayList<>();
        drawStack = new ArrayList<>();
    }

    public DrawLayout(Context context, AttributeSet attr) {
        super(context, attr);
        drawLayout = findViewById(R.id.draw_layout);
        currentDraw = new ArrayList<>();
        drawStack = new ArrayList<>();
    }

    public DrawLayout(Context context, AttributeSet attr, int def) {
        super(context, attr, def);
        drawLayout = findViewById(R.id.draw_layout);
        currentDraw = new ArrayList<>();
        drawStack = new ArrayList<>();
    }

    private void addDrawToStack() {
        if(currentDraw.size() > 0) {
            currentDraw.get(currentDraw.size() - 1).setTouchable(false);
        }

        CardView curr;
        while(currentDraw.size() > 0) {
            curr = currentDraw.remove(0);
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            curr.setLayoutParams(params);
            drawStack.add(curr);
        }
    }

    public Card[] returnDeck() {
        addDrawToStack();
        Card[] result = new Card[drawStack.size()];
        CardView cv;
        for(int i = 0; i < drawStack.size(); ++i) {
            cv = drawStack.get(i);
            result[i] = cv.card;
        }
        drawLayout.removeViewsInLayout(0, drawLayout.getChildCount());
        drawStack.clear();
        currentDraw.clear();
        return result;
    }

    @Override
    public void addCards(ArrayList<CardView> cards) {
        addDrawToStack();
        CardView curr;
        for(int i = 0; i < cards.size(); ++i) {
            curr = cards.get(i);
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.MATCH_PARENT);
            switch (i) {
                case 0:
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    break;
                case 1:
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    break;
                case 2:
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    break;
                default:
                    throw new IllegalArgumentException("Too mamy cards passes to draw");
            }
            curr.setScaleType(ImageView.ScaleType.FIT_CENTER);
            curr.setAdjustViewBounds(true);
            curr.setPadding(1, 1, 1, 1);
            curr.setLayoutParams(params);
            curr.setTouchable(false);
            currentDraw.add(curr);
            drawLayout.addView(curr);
        }
        currentDraw.get(currentDraw.size() - 1).setTouchable(true);
    }

    @Override
    public void removeCards(ArrayList<CardView> cards) {
        if(cards.size() != 1) {
            throw new IllegalArgumentException("attempted to remove more than 1 card from draw");
        }
        else {
            CardView draggable = currentDraw.get(currentDraw.size() - 1);
            CardView remove = cards.get(0);
            if (remove.equals(draggable)) {
                currentDraw.remove(draggable);
                drawLayout.removeView(draggable);
                if (currentDraw.size() <= 0 && drawStack.size() > 0) {
                    currentDraw.add(drawStack.remove(drawStack.size() - 1));
                }
                // if available set next card in draw to allow touch events
                if(currentDraw.size() > 0) {
                    draggable = currentDraw.get(currentDraw.size() - 1);
                    draggable.setTouchable(true);
                }
            }
        }
    }
}
