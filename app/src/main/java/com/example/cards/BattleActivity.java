package com.example.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BattleActivity extends AppCompatActivity {

    CardView cpuHandView ;
    CardView cpuPlayView ;
    CardView playerHandView;
    CardView playerPlayView;
    CardView[] cpuTieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

         cpuHandView = findViewById(R.id.cpu_hand);
         cpuPlayView = findViewById(R.id.cpu_play);
         playerHandView = findViewById(R.id.player_hand);
         playerPlayView = findViewById(R.id.player_play);
         cpuTieView = new CardView[]{findViewById(R.id.cpu_tie_one),
            findViewById(R.id.cpu_tie_two), findViewById(R.id.cpu_tie_three)};

        final Hand cpuHand = new Hand();
        final Hand playerHand = new Hand();
        Deal(cpuHand, playerHand);



        View mainLayout = (RelativeLayout)findViewById(R.id.main_layout);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Play(cpuHand, playerHand))
                    EndGame();
            }
        });

    }

    boolean Play(Hand cpuHand, Hand playerHand) {
        /*
        Card cpuPlay = cpuHand.Pop();
        Card playerPlay = playerHand.Pop();
        cpuPlayView.setImageResource(getResources().getIdentifier(cpuPlay.getFileNameOfCardImage(),
                "drawable", getPackageName()));
        playerPlayView.setImageResource(getResources().getIdentifier(playerPlay.getFileNameOfCardImage(),
                "drawable", getPackageName()));
        if(cpuPlay.value > playerPlay.value){
            cpuHand.PushBack(cpuPlay);
            cpuHand.PushBack(playerPlay);
        }
        else if (cpuPlay.value < playerPlay.value) {
            playerHand.PushBack(cpuPlay);
            playerHand.PushBack(playerPlay);
        }
        else {
            Hand cpuTies = new Hand();
            Hand playerTies = new Hand();

            while (cpuPlay.value == playerPlay.value){
                cpuTies.Push(cpuPlay);
                for (int i = 0; i < 3; ++i) {
                    if (cpuHand.Size() > 1)
                        cpuTies.Push(cpuHand.Pop());
                }
                cpuPlay = cpuHand.Pop();

                playerTies.Push(playerPlay);
                for(int i = 0; i < 3; ++i) {
                    if (playerHand.Size() > 1)
                        playerTies.Push(playerHand.Pop());
                }
                playerPlay = playerHand.Pop();
            }
            cpuTies.Push(cpuPlay);
            playerTies.Push(playerPlay);
            if (cpuPlay.value > playerPlay.value){
                while (cpuTies.Size() > 0)
                    cpuHand.PushBack(cpuTies.Pop());
                while (playerTies.Size() > 0)
                    cpuHand.PushBack(playerTies.Pop());
            }
            else {
                while (cpuTies.Size() > 0)
                    playerHand.PushBack(cpuTies.Pop());
                while (playerTies.Size() > 0)
                    playerHand.PushBack(playerTies.Pop());
            }
        }
        if(cpuHand.Size() <= 0 || playerHand.Size() <= 0)
            return true;
        */
        return false;
    }

     void EndGame() {
        startActivity(new Intent(BattleActivity.this, MainMenuActivity.class));
    }

    void Deal(Hand cpuHand, Hand playedHand){
        Deck deck = new Deck();
        deck.shuffle();
        while(deck.size() > 0 ) {
            cpuHand.pushBack(deck.pop());
            if(deck.size() > 0 )
                playedHand.pushBack(deck.pop());
        }
    }
}
