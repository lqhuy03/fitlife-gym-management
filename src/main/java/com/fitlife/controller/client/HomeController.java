package com.fitlife.controller.client;

import com.fitlife.entity.Product;
import com.fitlife.repository.CategoryRepository;
import com.fitlife.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public HomeController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping({"/", "/index", "/home"})
    public String home(Model model) {
        List<Product> list = productRepository.findAll();
        model.addAttribute("products", list.stream().limit(8).toList());
        return "client/home";
    }

    @GetMapping("/shop")
    public String shop(Model model, @RequestParam("cid") Optional<String> cid) {
        if (cid.isPresent()) {
            model.addAttribute("products", productRepository.findByCategoryId(cid.get()));
        } else {
            model.addAttribute("products", productRepository.findAll());
        }
        model.addAttribute("categories", categoryRepository.findAll());
        return "client/shop";
    }

    @GetMapping("/product/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Product product = productRepository.findById(id).get();
        model.addAttribute("p", product);
        return "client/detail";
    }


    @GetMapping("/contact")
    public String contact() {
        return "client/contact";
    }

    @PostMapping("/contact/send")
    public String sendContact() {
        return "redirect:/contact?success";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/admin/dashboard")
    public String admin(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "admin/dashboard";
    }
}


