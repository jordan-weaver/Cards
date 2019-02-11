package com.example.cards;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class SolitaireActivity extends AppCompatActivity {

    Deck deck;
    ImageButton deckView;
    CardView foundationOneView, foundationTwoView,
                foundationThreeView, foundationFourView;
    RelativeLayout columnOne, columnTwo, columnThree, columnFour,
                columnFive, columnSix, columnSeven;
    DrawLayout drawLayout;
    public static final int DRAW_SIZE = 3;

    void initVars() {
        deck = new Deck();
        deckView = findViewById(R.id.deck);
        deckView.setOnClickListener(deckOnClickListener);
        foundationOneView = findViewById(R.id.foundation_one);
        foundationTwoView = findViewById(R.id.foundation_two);
        foundationThreeView = findViewById(R.id.foundation_three);
        foundationFourView = findViewById(R.id.foundation_four);
        foundationOneView.setCard(new Card(0, Suit.SPADE));
        foundationTwoView.setCard(new Card(0, Suit.SPADE));
        foundationThreeView.setCard(new Card(0, Suit.SPADE));
        foundationFourView.setCard(new Card(0, Suit.SPADE));
        foundationOneView.setOnDragListener(foundationDragListener);
        foundationTwoView.setOnDragListener(foundationDragListener);
        foundationThreeView.setOnDragListener(foundationDragListener);
        foundationFourView.setOnDragListener(foundationDragListener);
        foundationOneView.setOnTouchListener(null);
        foundationTwoView.setOnTouchListener(null);
        foundationThreeView.setOnTouchListener(null);
        foundationFourView.setOnTouchListener(null);
        columnOne = findViewById(R.id.column_one);
        columnTwo = findViewById(R.id.column_two);
        columnThree = findViewById(R.id.column_three);
        columnFour = findViewById(R.id.column_four);
        columnFive = findViewById(R.id.column_five);
        columnSix = findViewById(R.id.column_six);
        columnSeven = findViewById(R.id.column_seven);
        drawLayout = findViewById(R.id.draw_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitaire);

        initVars();
        deck.shuffle();
        Deal();

    }

    void Deal() {
        Card c;
        RelativeLayout column;
        int numColumns = 7;
        for(int i = 0; i < numColumns; ++i)
            for (int j = numColumns - i; j > 0; --j) {
                c = deck.pop();
                switch (j + i) {
                    case 1:
                        column = columnOne;
                        break;
                    case 2:
                        column = columnTwo;
                        break;
                    case 3:
                        column = columnThree;
                        break;
                    case 4:
                        column = columnFour;
                        break;
                    case 5:
                        column = columnFive;
                        break;
                    case 6:
                        column = columnSix;
                        break;
                    case 7:
                        column = columnSeven;
                        break;
                    default:
                        throw new IllegalArgumentException("Attempting to place card in illegal column " + (j + i));
                }
                CardView cardView = new CardView(this, c);
                if (j != 1) {
                    cardView.setFaceDown(true);
                }
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                params.setMargins(0, i * 20, 0, 0);
                cardView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                cardView.setAdjustViewBounds(true);
                cardView.setLayoutParams(params);
                column.addView(cardView);
            }
    }

    ImageButton.OnClickListener deckOnClickListener = new ImageButton.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(deck.size() > 0) {
                CardView[] cardArray = new CardView[] {null, null, null};
                Card c;
                for(int i = 0; i < DRAW_SIZE; ++i) {
                    c = deck.pop();
                    if (c != null) {
                        cardArray[i] = new CardView(getApplicationContext(), c);
                    }
                }
                drawLayout.newDraw(cardArray);
                if(deck.size() <= 0) {
                    deckView.setImageResource(R.drawable.card_blank);
                }
            }
            else {
                deck.set(drawLayout.returnDeck());
                deckView.setImageResource(R.drawable.card_back);
            }
        }
    };

    View.OnDragListener foundationDragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            CardView foundation = (CardView)v;
            View view = (View) event.getLocalState();
            switch(event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    if (view instanceof CardView){
                        CardView draggedView = (CardView)view;
                        int value = draggedView.card.value;
                        Suit suit = draggedView.card.suit;
                        if(         (value == 1 && foundation.card.value == 0) ||
                                    (value - foundation.card.value == 1
                                            && suit.equals(foundation.card.suit))) {
                            foundation.setCard(draggedView.card);
                            return true;
                        }
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENDED:
                    if(!event.getResult() && view instanceof CardView) {
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return false;
        }
    };
}
