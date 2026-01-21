package com.fitlife.controller.client;

import com.fitlife.entity.Product;
import com.fitlife.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CartController {

    private final ProductRepository productRepository;

    public CartController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable("id") Integer id, HttpSession session) {
        // Lấy giỏ hàng từ Session, nếu chưa có thì tạo mới
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            item.setQty(item.getQty() + 1);
        } else {
            Product p = productRepository.findById(id).orElse(null);
            if (p != null) {
                cart.put(id, new CartItem(p.getId(), p.getName(), p.getImage(), p.getPrice(), 1));
            }
        }

        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new HashMap<>();

        double total = cart.values().stream().mapToDouble(i -> i.getPrice() * i.getQty()).sum();

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("totalAmount", total);
        return "client/cart";
    }
}

// Class hỗ trợ lưu thông tin sản phẩm trong giỏ (Bạn có thể để trong file này hoặc file riêng)
class CartItem {
    private Integer id;
    private String name;
    private String image;
    private Double price;
    private Integer qty;

    public CartItem(Integer id, String name, String image, Double price, Integer qty) {
        this.id = id; this.name = name; this.image = image; this.price = price; this.qty = qty;
    }
    // Getter & Setter
    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getImage() { return image; }
    public Double getPrice() { return price; }
    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
}