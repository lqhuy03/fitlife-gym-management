package com.fitlife.service.impl;

import com.fitlife.dto.CartItem;
import com.fitlife.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SessionScope // Bean này tồn tại trong 1 phiên làm việc (Session)
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    // Dùng Map để lưu giữ giỏ hàng (Key: ProductID, Value: CartItem)
    private Map<Integer, CartItem> map = new HashMap<>();

    @Override
    public void add(CartItem item) {
        // Nếu sp đã có trong giỏ -> Tăng số lượng
        CartItem existItem = map.get(item.getId());
        if (existItem != null) {
            existItem.setQty(existItem.getQty() + 1);
        } else {
            // Nếu chưa có -> Thêm mới
            map.put(item.getId(), item);
        }
    }

    @Override
    public void remove(int id) {
        map.remove(id);
    }

    @Override
    public CartItem update(int id, int qty) {
        CartItem item = map.get(id);
        if (item != null) {
            item.setQty(qty);
        }
        return item;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Collection<CartItem> getItems() {
        return map.values();
    }

    @Override
    public int getCount() {
        // Tổng số lượng các món hàng (Ví dụ: mua 2 áo, 3 quần -> count = 5)
        return map.values().stream().mapToInt(CartItem::getQty).sum();
    }

    @Override
    public double getAmount() {
        // Tổng tiền = giá * số lượng
        return map.values().stream().mapToDouble(item -> item.getPrice() * item.getQty()).sum();
    }
}
