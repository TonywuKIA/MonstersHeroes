package com.monstersandheroes.items;

/**
 * Marker for items that can be sold or bought in the market.
 */
public interface Purchasable extends Item {
    void onPurchase();

    void onSell();
}
