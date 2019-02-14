package com.example.cards;

import com.example.cards.CardView;

import java.util.ArrayList;

public interface CardHolder {
    void addCards(ArrayList<CardView> cards);
    void removeCards(ArrayList<CardView>  cards);
}
