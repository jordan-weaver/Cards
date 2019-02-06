package com.example.cards;

import android.content.Context;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class DrawLayout extends RelativeLayout {

    RelativeLayout drawLayout;
    ArrayList<Card> drawQueue;
    int DRAW_SIZE;

    public DrawLayout(Context context) {
        super(context);
        drawLayout = findViewById(R.id.draw_layout);
        DRAW_SIZE = 3;
        drawQueue = new ArrayList<>();
    }
}
