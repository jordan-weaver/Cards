package com.example.cards;

import java.util.ArrayList;

public class Deck {
    private final int DECK_SIZE = 52;
    private final Boolean JOKERS = false;

    private ArrayList<Card> cards;

    Deck() {
        cards = new ArrayList<Card>(DECK_SIZE);
        for(int i = 0; i < 13; ++i) {
            cards.add(new Card(i + 1, Suit.SPADE));
            cards.add(new Card(i + 1, Suit.HEART));
            cards.add(new Card(i + 1, Suit.DIAMOND));
            cards.add(new Card(i + 1, Suit.CLUB));
        }
    }

    Card PopDeck() {
        if (cards.size() > 0) {
            return cards.remove(0);
        } else
            throw new IllegalArgumentException("Attempted to remove card from empty deck");
    }
}
