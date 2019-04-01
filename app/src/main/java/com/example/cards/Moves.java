package com.example.cards;

import android.view.View;
import android.view.ViewParent;

import java.util.ArrayList;

enum MoveType { Drag, DeckFlip, ColumnFlip};

public class Moves {
    MoveType type;
    ColumnLayout cl;
    CardHolder prevParent;
    CardHolder nextParent;
    ArrayList<CardView> object;

    Moves(CardHolder prev, CardHolder next, ArrayList obj) {
        type = MoveType.Drag;
        prevParent = prev;
        nextParent = next;
        object = obj;
    }

    Moves(ColumnLayout column) {
        type = MoveType.ColumnFlip;
        cl = column;
    }
}
