package com.example.cards;

import android.content.ClipData;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SolitaireActivity extends AppCompatActivity {

    ImageView deckView;
    ImageView foundationOneView, foundationTwoView,
                foundationThreeView, foundationFourView;
    RelativeLayout columnOne, columnTwo, columnThree, columnFour,
                columnFive, columnSix, columnSeven;

    void initVars() {
        deckView = findViewById(R.id.deck);
        foundationOneView = findViewById(R.id.foundation_one);
        foundationTwoView = findViewById(R.id.foundation_two);
        foundationThreeView = findViewById(R.id.foundation_three);
        foundationFourView = findViewById(R.id.foundation_four);
        columnOne = findViewById(R.id.column_one);
        columnTwo = findViewById(R.id.column_two);
        columnThree = findViewById(R.id.column_three);
        columnFour = findViewById(R.id.column_four);
        columnFive = findViewById(R.id.column_five);
        columnSix = findViewById(R.id.column_six);
        columnSeven = findViewById(R.id.column_seven);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitaire);

        initVars();
        Deck deck = new Deck();
        deck.Shuffle();
        Deal(deck);

        Card c = deck.Pop();
        //ImageView testCardView = findViewById(R.id.test_card);
        //testCardView.setImageResource(getResources().getIdentifier(c.getFileNameOfCardImage(),
        //      "drawable", getPackageName()));
        //testCardView.setOnTouchListener(onTouchListener);
        foundationOneView.setOnDragListener(foundationDragListener);
        foundationTwoView.setOnDragListener(foundationDragListener);
        foundationThreeView.setOnDragListener(foundationDragListener);
        foundationFourView.setOnDragListener(foundationDragListener);

    }

    void Deal(Deck deck) {
        Card c;
        RelativeLayout column;
        int numColumns = 7;
        for(int i = 0; i < numColumns; ++i) {
            for(int j = numColumns - i; j > 0; --j) {
                c = deck.Pop();
                switch(j + i) {
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
                // IF STATEMENT FOR FACEDOWN
                ImageView imageView = new ImageView(this);
                imageView.setOnTouchListener(onTouchListener);
                imageView.setImageResource(getResources().getIdentifier(c.getFileNameOfCardImage(),
                        "drawable", getPackageName()));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(column.getLayoutParams());
                params.setMargins(0, i * 20, 0, 0);
                column.addView(imageView);
            }
        }
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData clipData = ClipData.newPlainText("", "");
                View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(clipData, myShadowBuilder, v, 0);
                v.setVisibility((View.INVISIBLE));
                return true;
            }
            return false;
        }
    };

    View.OnDragListener foundationDragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            // v    = target
            // view = dragged view
            View view = (View) event.getLocalState();
            switch(event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Toast.makeText(getApplicationContext(), "Dragging over", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Toast.makeText(getApplicationContext(), "Dragged exited", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DROP:
                    Toast.makeText(getApplicationContext(), "Dopped on", Toast.LENGTH_SHORT).show();
                    ((ImageView)v).setImageDrawable(((ImageView)view).getDrawable());
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    boolean validMove = false;
                    if(validMove) {
                        // Remove dragged card from pre-drag position and make move in the game
                        view.setVisibility(View.GONE);
                    }
                    else {
                        // Return dragged card to pre-drag position and make no moves in the game
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
            }

            return true;
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation/keyboard change
        super.onConfigurationChanged(newConfig);
    }
}
