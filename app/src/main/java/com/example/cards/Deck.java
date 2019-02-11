package com.example.cards;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private final int DECK_SIZE = 52;
    private final Boolean JOKERS = false;

    protected ArrayList<Card> cards;

    Deck() {
        cards = new ArrayList<Card>(DECK_SIZE);
        for(int i = 0; i < 13; ++i) {
            cards.add(new Card(i + 1, Suit.SPADE));
            cards.add(new Card(i + 1, Suit.HEART));
            cards.add(new Card(i + 1, Suit.DIAMOND));
            cards.add(new Card(i + 1, Suit.CLUB));
        }
    }

    Deck (int size) {
        cards = new ArrayList<Card>(size);
    }

    int size() {
        return cards.size();
    }

    void set(Card[] _cards) {
        cards = new ArrayList<>();
        for(int i = 0; i < _cards.length; ++i)
            cards.add(_cards[i]);
    }

    Card pop() {
        if (cards.size() > 0) {
            return cards.remove(0);
        } else {
            return null;
        }
    }

    Card popBack() {
        if (cards.size() > 0) {
            return cards.remove(cards.size() - 1);
        } else {
            return null;
        }
    }

    void push(Card c) {
        cards.add(0, c);
    }

    void pushBack(Card c) {
        cards.add(c);
    }



    public void shuffle() {
        Random rand = new Random();
        int a, b;
        Card swap;
        for(int i = 0; i < 500; ++i) {
            a = rand.nextInt(DECK_SIZE);
            b = rand.nextInt(DECK_SIZE);
            swap = cards.get(a);
            cards.set(a, cards.get(b));
            cards.set(b, swap);
        }
    }
}
