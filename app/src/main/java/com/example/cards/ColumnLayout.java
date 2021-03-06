package com.example.cards;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.util.ArrayList;

public class ColumnLayout extends RelativeLayout implements CardHolder {

    Context context;
    AttributeSet attributeSet;

    ArrayList<CardView> column;
    CardView touched;
    public final int MARGIN_FACE_UP = 30;
    public final int MARGIN_FACE_DOWN = 30;

    public ColumnLayout(Context context) {
        super(context);
        this.context = context;
        this.attributeSet = null;
        column = new ArrayList<>();
        setOnDragListener(columnOnDragListener);
        setOnTouchListener(columnOnTouchListener);
    }

    public ColumnLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attributeSet = attrs;
        column = new ArrayList<>();
        setOnDragListener(columnOnDragListener);
        setOnTouchListener(columnOnTouchListener);
    }

    public ColumnLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attributeSet = attrs;
        column = new ArrayList<>();
        setOnDragListener(columnOnDragListener);
        setOnTouchListener(columnOnTouchListener);
    }

    @Override
    public void addCards(ArrayList<CardView> cards) {
        if(cards == null)
            return;
        for(CardView cv : cards) {
            addCard(cv);
            cv.setTouchable(false);
        }
        getBottomCard().setTouchable(true);
    }

    @Override
    public void removeCards(ArrayList<CardView> cards) {
        if(cards == null)
            return;

        for(int i = cards.size() - 1; i >= 0; --i) {
            removeCard(cards.get(i));
        }

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
            marginSize = MARGIN_FACE_DOWN;
        }
        else {
            marginSize = MARGIN_FACE_UP;
        }
        //********************************
        //              TO DO
        //*********************************
        // Margin will need to be adjusted based on how many facedown cards there are
        // numFaceDowns * 20 + numFaceUps * 30
        params.setMargins(0, column.size() * marginSize, 0, 0);
        cv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        cv.setAdjustViewBounds(true);
        // If no cards are in column, remove filler space before adding cards to layout
        if(getBottomCard() == null)
            removeAllViewsInLayout();
        column.add(cv);
        addView(cv, params);
        cv.setVisibility(VISIBLE);
    }

    public void removeCard(CardView cv) {
        if(cv.equals(getBottomCard())) {
            column.remove(cv);
            this.removeView(cv);
        }
        // If removing card empties the column, place the filler view
        if(getBottomCard() == null) {
            CardView filler = new CardView(context);
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            cv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            cv.setAdjustViewBounds(true);
            addView(filler, params);
            cv.setVisibility(VISIBLE);
        }
    }

    public CardView getBottomCard() {
        if(column.size() <= 0)
            return null;
        return column.get(column.size() - 1);
    }

    public CardView getTopCard() {
        if(column.size() <= 0)
            return null;
        return column.get(0);
    }

    private boolean droppable(CardView base, CardView dropped) {
        if(base == null)
            return dropped.card.value == 13;

        if(base.faceDown)
            return false;

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

    private void setTouchedCard(MotionEvent event) {
        touched = null;
        Rect outRect = new Rect();
        int[] location = new int[2];
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            for (CardView cv : column) {
                cv.getDrawingRect(outRect);
                cv.getLocationOnScreen(location);
                outRect.offset(location[0], location[1]);
                if (outRect.contains(x, y)) {
                    touched = cv;
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            setTouchedCard(event);
            if (touched == null)
                return false;

            // Let bottom card consume touch event
            if (touched.equals(getBottomCard()))
                return false;

            return true;

        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            touched = null;
        }
        return false;
    }

    RelativeLayout.OnTouchListener columnOnTouchListener = new RelativeLayout.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                if(!touched.faceDown) {
                    ClipData clipData = ClipData.newPlainText("", "");
                    ColumnDragShadowBuilder myShadowBuilder = new ColumnDragShadowBuilder(v, touched);
                    v.startDragAndDrop(clipData, myShadowBuilder, v, 0);
                    for(int i = column.indexOf(touched); i < column.size(); ++i) {
                        column.get(i).setVisibility(INVISIBLE);
                    }
                    return true;
                }
            }
            return false;
        }
    };

    RelativeLayout.OnDragListener columnOnDragListener = new RelativeLayout.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            View view = (View)event.getLocalState();
            CardView cv;
            switch(event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if(view instanceof CardView) {
                        cv = (CardView) view;
                        if(droppable(getBottomCard(), cv))
                            return true;
                    }
                    if(view instanceof ColumnLayout) {
                        cv = ((ColumnLayout) view).touched;
                        if(droppable(getBottomCard(), cv))
                            return true;
                        if(view.equals(v))
                            return true;
                    }
                    return false;
                case DragEvent.ACTION_DROP:
                    CardHolder parentLayout;
                    if(view instanceof ColumnLayout)
                        parentLayout = (ColumnLayout)view;
                    else
                        parentLayout = (CardHolder)view.getParent();

                    ArrayList<CardView> list = new ArrayList<CardView>();
                    if(view instanceof ColumnLayout) {
                        ColumnLayout parent = (ColumnLayout)view;
                        for(int i = parent.column.indexOf(parent.touched); i < parent.column.size(); ++i) {
                            list.add(parent.column.get(i));
                        }
                    }
                    else {
                        list.add((CardView)view);
                    }

                    parentLayout.removeCards(list);
                    addCards(list);
                    SolitaireActivity.addMove(new Moves(parentLayout, ColumnLayout.this, list));
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    for(int i = 0; i < column.size(); ++i)
                        column.get(i).setVisibility(VISIBLE);
                    return false;
            }
            return false;
        }
    };

    private class ColumnDragShadowBuilder extends View.DragShadowBuilder {

        ColumnLayout columnLayout;
        ArrayList<CardView> column;
        CardView touched;
        int cardWidth;
        int cardHeight;
        int numCards;

        public ColumnDragShadowBuilder(View view) {
            super(view);
            cardHeight = 0;
            cardWidth = 0;
            numCards= 0;
            if(view instanceof ColumnLayout) {
                columnLayout = (ColumnLayout) view;
                column = columnLayout.column;
            }
        }

        public ColumnDragShadowBuilder(View view, CardView touched) {
            super(view);
            cardHeight = 0;
            cardWidth = 0;
            numCards = 0;
            if(view instanceof ColumnLayout) {
                columnLayout = (ColumnLayout) view;
                column = columnLayout.column;
                if(column.contains(touched))
                    this.touched = touched;
            }
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);

            int numDown = 0;
            for(CardView cv : column) {
                if(!cv.faceDown)
                    break;
                ++numDown;
            }
            int numUp = column.size() - numDown;
            numCards = column.size() - column.indexOf(touched);
            cardWidth = shadowSize.x;
            cardHeight = shadowSize.y -
                    (numDown * columnLayout.MARGIN_FACE_DOWN) -
                    ((numUp - 1) * columnLayout.MARGIN_FACE_UP);

            shadowSize.set(shadowSize.x, cardHeight + (numCards - 1) * columnLayout.MARGIN_FACE_UP);
            shadowTouchPoint.set(shadowTouchPoint.x, cardHeight / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            //super.onDrawShadow(canvas);
            Paint paint = new Paint();
            Bitmap[] bitmaps = new Bitmap[numCards];
            for(int i = column.indexOf(touched); i < column.size(); ++i) {
                Bitmap image = BitmapFactory.decodeResource(getResources(), column.get(i).imageResource);
                Bitmap scaled = Bitmap.createScaledBitmap(image, cardWidth, cardHeight, true);
                bitmaps[i - column.indexOf(touched)] = scaled;
            }
            for(int i = 0; i < bitmaps.length; ++i) {
                canvas.drawBitmap(bitmaps[i], 0, columnLayout.MARGIN_FACE_UP * i, paint);
            }
        }
    }
}
