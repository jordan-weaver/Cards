package com.example.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawLayout extends RelativeLayout {

    RelativeLayout drawLayout;
    ArrayList<CardView> currentDraw;
    ArrayList<CardView> drawStack;

    public DrawLayout(Context context) {
        super(context);
        drawLayout = findViewById(R.id.draw_layout);
        drawLayout.setOnDragListener(onDragListener);
        currentDraw = new ArrayList<>();
        drawStack = new ArrayList<>();
    }

    public DrawLayout(Context context, AttributeSet attr) {
        super(context, attr);
        drawLayout = findViewById(R.id.draw_layout);
        drawLayout.setOnDragListener(onDragListener);
        currentDraw = new ArrayList<>();
        drawStack = new ArrayList<>();
    }

    public DrawLayout(Context context, AttributeSet attr, int def) {
        super(context, attr, def);
        drawLayout = findViewById(R.id.draw_layout);
        drawLayout.setOnDragListener(onDragListener);
        currentDraw = new ArrayList<>();
        drawStack = new ArrayList<>();
    }

    RelativeLayout.OnDragListener onDragListener = new RelativeLayout.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if(event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                View view = (View) event.getLocalState(); // dragged card
                CardView draggable = currentDraw.get(currentDraw.size() - 1);
                if (view instanceof CardView && view.equals(draggable)) {
                    if (event.getResult()) { // card was moved
                        //remove card from draw
                        currentDraw.remove(draggable);
                        drawLayout.removeView(draggable);
                        if (currentDraw.size() <= 0) {
                            currentDraw.add(drawStack.remove(drawStack.size() - 1));
                        }
                        //set next card in draw to allow touch events
                        draggable = currentDraw.get(currentDraw.size() - 1);
                        draggable.setOnTouchListener(draggable.cardViewOnTouchListener);
                    } else {
                        // return card to draw
                        draggable.setVisibility(VISIBLE);
                    }
                }
            }
            return true;
        }
    };

    private void addDrawToStack() {
        if(currentDraw.size() > 0) {
            currentDraw.get(currentDraw.size() - 1).setOnTouchListener(null);
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

    public void newDraw(CardView[] cards) {
        addDrawToStack();
        for(int i = 0; i < cards.length && cards[i] != null; ++i) {
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
            switch(i) {
                case 0:
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    break;
                case 1:
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    break;
                case 2:
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    break;
            }
            cards[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
            cards[i].setAdjustViewBounds(true);
            cards[i].setPadding(1,1,1,1);
            cards[i].setLayoutParams(params);
            cards[i].setOnTouchListener(null);
            currentDraw.add(cards[i]);
            drawLayout.addView(cards[i]);
        }
        CardView draggable = currentDraw.get(currentDraw.size() - 1);
        draggable.setOnTouchListener(draggable.cardViewOnTouchListener);
    }

    public Card[] returnDeck() {
        addDrawToStack();
        Card[] result = new Card[drawStack.size()];
        CardView cv;
        for(int i = 0; i < drawStack.size(); ++i) {
            cv = drawStack.get(i);
            result[i] = cv.card;
            //remove views from drawLayout here?
        }
        drawLayout.removeViewsInLayout(0, drawLayout.getChildCount());
        drawStack.clear();
        currentDraw.clear();
        Toast.makeText(getContext(), "Return deck length: " + result.length, Toast.LENGTH_SHORT).show();
        return result;
    }
}
