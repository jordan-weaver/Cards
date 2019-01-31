package com.example.cards;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Deck deck = new Deck();

        final ImageButton cardImage = (ImageButton) findViewById(R.id.card_image);
        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card c = deck.PopDeck();
                cardImage.setImageResource(
                        getResources().getIdentifier(c.getFileNameOfCardImage(),
                                "drawable", getPackageName()));
            }
        });

    }
}
