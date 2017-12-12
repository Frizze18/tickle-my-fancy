package se.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import se.academy.domain.Customer;
import se.academy.domain.Product;
import se.academy.domain.Review;
import se.academy.domain.ProductWrapper;
import se.academy.domain.ShoppingCart;
import se.academy.repository.DbRepository;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.*;


@Controller
public class FancyController {
    @Autowired
    private DbRepository repository;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        handleLoginStatus(session,model);
        model.addAttribute("makeUp", repository.getBySubCategoryTop3("Herrdoft"));
        model.addAttribute("nails", repository.getBySubCategoryTop3("Damdoft"));
        model.addAttribute("eyes", repository.getBySubCategoryTop3("Hudvård"));
        return "index";
    }

    @RequestMapping("/customerpage")
    public String showPersonalPage(Model model, HttpSession session){
        Customer customer = (Customer) session.getAttribute("sessionCustomer");
        handleLoginStatus(session, model);
        if(customer == null){
            return "redirect:/";
        }
        model.addAttribute("customer",customer);
        return "customerpage";
    }

    @PostMapping("/login")
    public String login(Model model, HttpSession session, @RequestParam String email, @RequestParam String password) {
        Customer customer = repository.loginCustomer(email,password);
        if (customer == null){
            String loginFail = "Inloggning misslyckades";
            session.setAttribute("loginFail",loginFail);
            return "redirect:/";
        }
        else{
            session.setAttribute("sessionCustomer",customer);
            session.removeAttribute("loginFail");
            return "redirect:/"; //TODO make it return page you were on
        }
    }

    @GetMapping("/search")
    public String search(Model model, HttpSession session, @RequestParam String srch) {
        model.addAttribute("products", repository.search(srch));
        handleLoginStatus(session, model);
        return "search";
    }

    @GetMapping("/p")
    public String product(Model model, HttpSession session, @RequestParam int id) {
        handleLoginStatus(session, model);

        return "index"; //TODO make it product with ID id
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {

        return "redirect:/"; //TODO make it return page you were on
    }

    @PostMapping("/logout")
    public String logoutPost(HttpSession session){
        //TODO cookies and stuff
        session.removeAttribute("sessionCustomer");
        return "redirect:/";
    }

    @GetMapping("/registration")
    public ModelAndView registration(HttpSession session, Model model){
        Customer customer = new Customer();
        handleLoginStatus(session, model);
        return new ModelAndView("registration").addObject("customer", customer);
    }

    @PostMapping("/registration")
    public ModelAndView registration(@Valid Customer customer, BindingResult bindingResult, HttpSession session){
        if(bindingResult.hasErrors()) {
            return new ModelAndView("registration").addObject("customer", customer);
        }
        if(repository.checkIfCustomerExist(customer)) {
            String emailOccupied = "Email redan registrerat.";
            return  new ModelAndView("registration").addObject("customer", customer).addObject("occupied",emailOccupied);
        }

        repository.registerCustomer(customer, new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
        return new ModelAndView("index");
    }

    @GetMapping("/productinfo")
    public String productInfo (Model model, HttpSession session, @RequestParam int productID){
        model.addAttribute("product", repository.getProduct(productID));
        model.addAttribute("makeUp", repository.getBySubCategoryTop3("Herrdoft"));
        model.addAttribute("review", new Review(productID, 0, ""));
        model.addAttribute("productID", productID);
        model.addAttribute("reviews", repository.getReviews(productID));
        model.addAttribute("nails", repository.getBySubCategoryTop3("Damdoft"));
//        model.addAttribute("avg",repository.getAvg(productID));

        handleLoginStatus(session, model);
        return "productinfo";
    }
    @PostMapping("/addreview")
    public String addReview (@ModelAttribute Review review){
        repository.addReview(review);
        return "redirect:/productinfo?productID="+review.getProductID();
    }

    @PostMapping("/addProduct")
    public String addProductToShoppingCart(HttpSession session,@RequestParam int productID){
        System.out.println("Klick på lägg till produkt nr:"+productID);
        if(session.getAttribute("shoppingCart") == null){
            ShoppingCart shoppingCart = new ShoppingCart();
            session.setAttribute("shoppingCart", shoppingCart);
        }
        Product product = repository.getProduct(productID);
        ShoppingCart shoppingCart =  (ShoppingCart) session.getAttribute("shoppingCart");
        shoppingCart.addProduct(product);
        return "redirect:/productinfo?productID="+productID;
    }

    @GetMapping("/addProductInCart")
    public String plusShoppingCart(HttpSession session,@RequestParam int productID){
        Product product = repository.getProduct(productID);
        ShoppingCart shoppingCart =  (ShoppingCart) session.getAttribute("shoppingCart");
        shoppingCart.addProduct(product);
        return "redirect:shoppingcart";
    }

    @GetMapping("/removeProductInCart")
    public String minusShoppingCart(HttpSession session,@RequestParam int productID){
        Product product = repository.getProduct(productID);
        ShoppingCart shoppingCart =  (ShoppingCart) session.getAttribute("shoppingCart");
        shoppingCart.removeProduct(product);
        return "redirect:shoppingcart";
    }

    @GetMapping("/shoppingcart")
    public String shoppingcart(Model model, HttpSession session){
        handleLoginStatus(session,model);
        ShoppingCart shoppingCart;
        if(session.getAttribute("shoppingCart") != null){
            shoppingCart =  (ShoppingCart) session.getAttribute("shoppingCart");
        }
        else{
            shoppingCart = new ShoppingCart();
            session.setAttribute("shoppingCart",shoppingCart);
        }
        model.addAttribute("shoppingCart",shoppingCart);
        return "shoppingcart";
    }

    @PostMapping("/buyShoppingCart")
    public String buyShoppingCart(Model model, HttpSession session){
        List<Product> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        Customer customer = (Customer) session.getAttribute("sessionCustomer");
        String email = customer.getEmail();
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        for(Map.Entry<Integer, ProductWrapper> entry : shoppingCart.getShoppingmap().entrySet()) {
            products.add(entry.getValue().getProduct());
            quantities.add(entry.getValue().getQuantity());
        }
        repository.addOrder(products,quantities,email);
        return "redirect:shoppingcart";
    }

    @PostMapping("/emptyShoppingCart")
    public String emptyShoppingCart(HttpSession session){
        session.removeAttribute("shoppingCart");
        return "redirect:shoppingcart";
    }
  
    @GetMapping("/subcategory")
    public String getSubcategory(Model model, HttpSession session, @RequestParam String sc) {

        Queue<Product> products = repository.getBySubCategory(sc);
        if (products.isEmpty()) {
            //Should never happen
            return "redirect:/";
        }
        Queue<Product> topThree = repository.getBySubCategoryTop3(sc);

        model.addAttribute("category", topThree.peek().getSubcategory());
        model.addAttribute("topProducts", topThree);
        model.addAttribute("productsInSubcategory", products);

        return "subcategory";
    }

    private void handleLoginStatus(HttpSession session, Model model){
        boolean isLogedIn;
        if(session.getAttribute("sessionCustomer") == null){
            isLogedIn = false;
        }else{
            isLogedIn = true;
        }
        if(session.getAttribute("loginFail") != null){
            model.addAttribute("loginFail",session.getAttribute("loginFail"));
        }
        model.addAttribute("isLogedIn",isLogedIn);
    }
}
