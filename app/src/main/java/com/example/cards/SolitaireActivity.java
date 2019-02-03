package com.example.cards;

import android.content.ClipData;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SolitaireActivity extends AppCompatActivity {

    ImageView deckView;
    ImageView foundationOneView;
    ImageView foundationTwoView;
    ImageView foundationThreeView;
    ImageView foundationFourView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solitaire);

        initVars();
        final Deck deck = new Deck();
        deck.Shuffle();

        View.OnLongClickListener longClickListener = new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                ClipData clipData = ClipData.newPlainText("","");
                View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(clipData, myShadowBuilder, v, 0);
                v.setVisibility((View.INVISIBLE));
                return true;
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

        Card c = deck.Pop();
        ImageView testCardView = findViewById(R.id.test_card);
        testCardView.setImageResource(getResources().getIdentifier(c.getFileNameOfCardImage(),
                "drawable", getPackageName()));
        testCardView.setOnLongClickListener(longClickListener);
        foundationOneView.setOnDragListener(foundationDragListener);
        foundationTwoView.setOnDragListener(foundationDragListener);
        foundationThreeView.setOnDragListener(foundationDragListener);
        foundationFourView.setOnDragListener(foundationDragListener);

    }

    void initVars() {
        deckView = findViewById(R.id.deck);
        foundationOneView = findViewById(R.id.foundation_one);
        foundationTwoView = findViewById(R.id.foundation_two);
        foundationThreeView = findViewById(R.id.foundation_three);
        foundationFourView = findViewById(R.id.foundation_four);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation/keyboard change
        super.onConfigurationChanged(newConfig);
    }
}
