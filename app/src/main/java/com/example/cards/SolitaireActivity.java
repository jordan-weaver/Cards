package com.example.cards;

import android.widget.ImageButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class SolitaireActivity extends AppCompatActivity {

    Deck deck;
    ImageButton deckView;
    CardView foundationOneView, foundationTwoView,
                foundationThreeView, foundationFourView;
    ColumnLayout[] columnLayouts;
    DrawLayout drawLayout;
    public static final int DRAW_SIZE = 3;
    public static final int COLUMNS = 7;

    void initVars() {
        deck = new Deck();
        deckView = findViewById(R.id.deck);
        deckView.setOnClickListener(deckOnClickListener);
        foundationOneView = findViewById(R.id.foundation_one);
        foundationTwoView = findViewById(R.id.foundation_two);
        foundationThreeView = findViewById(R.id.foundation_three);
        foundationFourView = findViewById(R.id.foundation_four);
        columnLayouts = new ColumnLayout[] { findViewById(R.id.column_one),
                findViewById(R.id.column_two), findViewById(R.id.column_three),
                findViewById(R.id.column_four), findViewById(R.id.column_five),
                findViewById(R.id.column_six), findViewById(R.id.column_seven)};
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
        CardView cv;
        for(int i = 0; i < COLUMNS; ++i) {
            for (int j = 0; j <= i; ++j) {
                c = deck.pop();
                if (c == null)
                    break;
                cv = new CardView(this, c);
                if (j != i) {
                    cv.setTouchable(false);
                    cv.setFaceDown(true);
                }
                columnLayouts[i].addCard(cv);
            }
        }
    }

    ImageButton.OnClickListener deckOnClickListener = new ImageButton.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(deck.size() > 0) {
                ArrayList<CardView> cards = new ArrayList<>();
                CardView[] cardArray = new CardView[] {null, null, null};
                Card c;
                for(int i = 0; i < DRAW_SIZE; ++i) {
                    c = deck.pop();
                    if (c != null) {
                        cards.add(new CardView(getApplicationContext(), c));
                        cardArray[i] = new CardView(getApplicationContext(), c);
                    }
                }
                drawLayout.addCards(cards);
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


}
