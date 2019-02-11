package com.example.cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class DrawLayout extends RelativeLayout {

    RelativeLayout drawLayout;
    ArrayList<CardView> currentDraw;
    ArrayList<CardView> drawStack;
    CardView cardToDrag;

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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        super.onInterceptTouchEvent(event);
        // Consumes touch event on drawn cards. Only the latest available
        // card will have a disallow enabled.
        return true;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallow) {
        super.requestDisallowInterceptTouchEvent(disallow);
    }

    RelativeLayout.OnDragListener onDragListener = new RelativeLayout.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if(event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                View view = (View) event.getLocalState(); // dragged card
                if (view instanceof CardView && view.equals(cardToDrag)) {
                    if (event.getResult()) { // card was moved
                        //remove card from draw
                        cardToDrag.getParent().requestDisallowInterceptTouchEvent(false);
                        currentDraw.remove(cardToDrag);
                        if (currentDraw.size() <= 0) {
                            currentDraw.add(drawStack.remove(drawStack.size() - 1));
                        }
                        //set next card in draw to allow touch events
                        currentDraw.get(currentDraw.size() - 1).getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        // return card to draw
                        cardToDrag.setVisibility(VISIBLE);
                    }
                }
            }
            return true;
        }
    };

    private void addDrawToStack() {
        if(currentDraw.size() > 0)
            currentDraw.get(currentDraw.size() - 1).getParent().requestDisallowInterceptTouchEvent(false);

        CardView curr;
        while(currentDraw.size() > 0) {
            curr = currentDraw.remove(0);
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            curr.setLayoutParams(params);
            drawStack.add(0, curr);
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
            drawLayout.addView(cards[i]);
        }
        cardToDrag = (CardView) drawLayout.getChildAt(drawLayout.getChildCount() - 1);
        cardToDrag.getParent().requestDisallowInterceptTouchEvent(true);
    }

    public ArrayList<Card> returnDeck() {
        addDrawToStack();
        ArrayList<Card> result  = new ArrayList<>();
        while(drawStack.size() > 0) {
            result.add(drawStack.remove(0).card);
        }
        while(drawLayout.getChildCount() > 0) {
            CardView child = (CardView)drawLayout.getChildAt(0);
            child.setVisibility(GONE);
        }
        drawStack.clear();
        currentDraw.clear();
        return result;
    }
}
