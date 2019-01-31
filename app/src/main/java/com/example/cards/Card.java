package com.example.cards;

public class Card {
    int value;
    Suit suit;

    Card(int _value, Suit _suit) {
        value = _value;
        suit = _suit;
    }

    String getFileNameOfCardImage() {
        String fileName = "";
        switch (value) {
            case 1:
                fileName += "ace";
                break;
            case 2:
                fileName += "two";
                break;
            case 3:
                fileName += "three";
                break;
            case 4:
                fileName += "four";
                break;
            case 5:
                fileName += "five";
                break;
            case 6:
                fileName += "six";
                break;
            case 7:
                fileName += "seven";
                break;
            case 8:
                fileName += "eight";
                break;
            case 9:
                fileName += "nine";
                break;
            case 10:
                fileName += "ten";
                break;
            case 11:
                fileName += "jack";
                break;
            case 12:
                fileName += "queen";
                break;
            case 13:
                fileName += "king";
                break;
            default:
                throw new IllegalArgumentException("Illegal card value");
        }
        fileName += "_of_";
        switch (suit) {
            case SPADE:
                fileName += "spades";
                break;
            case HEART:
                fileName += "hearts";
                break;
            case DIAMOND:
                fileName += "diamonds";
                break;
            case CLUB:
                fileName += "clubs";
                break;
            default:
                throw new IllegalArgumentException("Illegal Suit enum value");
        }
        return fileName;
    }
}

