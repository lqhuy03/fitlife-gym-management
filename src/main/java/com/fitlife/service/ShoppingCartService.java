package com.fitlife.service;

import com.fitlife.dto.CartItem;
import java.util.Collection;

public interface ShoppingCartService {
    // Thêm sản phẩm vào giỏ
    void add(CartItem item);

    // Xóa sản phẩm khỏi giỏ
    void remove(int id);

    // Cập nhật số lượng item
    CartItem update(int id, int qty);

    // Xóa sạch giỏ hàng
    void clear();

    // Lấy danh sách các món trong giỏ
    Collection<CartItem> getItems();

    // Lấy tổng số lượng món hàng
    int getCount();

    // Lấy tổng tiền
    double getAmount();
}
