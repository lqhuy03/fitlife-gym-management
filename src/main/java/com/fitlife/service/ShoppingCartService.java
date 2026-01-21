package com.fitlife.service;

import com.fitlife.dto.CartItem;
import java.util.Collection;

public interface ShoppingCartService {
    void add(CartItem item);

    void remove(int id);

    CartItem update(int id, int qty);

    void clear();

    Collection<CartItem> getItems();

    int getCount();

    double getAmount();
}
