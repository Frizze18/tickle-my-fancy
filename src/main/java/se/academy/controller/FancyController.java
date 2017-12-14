package se.academy.controller;

import com.klarna.rest.api.CheckoutOrder;
import com.klarna.rest.api.Client;
import com.klarna.rest.api.DefaultClient;
import com.klarna.rest.api.Order;
import com.klarna.rest.api.model.CheckoutOrderData;
import com.klarna.rest.api.model.MerchantUrls;
import com.klarna.rest.api.model.OrderData;
import com.klarna.rest.api.model.OrderLine;
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

import static java.lang.Math.toIntExact;


@Controller
public class FancyController {
    @Autowired
    private DbRepository repository;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        handleLoginStatus(session,model);
        handleAddSubCategories(model);
        pickRandomTopp(model);
        return "index";
    }

    @RequestMapping("/customerpage")
    public String showPersonalPage(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("sessionCustomer");
        handleAddSubCategories(model);
        handleLoginStatus(session, model);
        if (customer == null) {
            return "redirect:/";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("orders", repository.getOrdersForCustomer(customer.getEmail()));
        return "customerpage";
    }

    @GetMapping("/showOrder")
    public String showPersonalPage(Model model, HttpSession session, @RequestParam int orderID) {
        Customer customer = (Customer) session.getAttribute("sessionCustomer");
        handleAddSubCategories(model);
        handleLoginStatus(session, model);
        if (customer == null) {
            return "redirect:/";
        }
        model.addAttribute("orderID", orderID);
        model.addAttribute("suborders", repository.getWholeOrder(orderID));
        return "order";
    }

    @PostMapping("/login")
    public String login(Model model, HttpSession session, @RequestParam String email, @RequestParam String password) {
        Customer customer = repository.loginCustomer(email, password);
        if (customer == null) {
            String loginFail = "Inloggning misslyckades";
            session.setAttribute("loginFail", loginFail);
            return "redirect:/";
        } else {
            session.setAttribute("sessionCustomer", customer);
            session.removeAttribute("loginFail");
            return "redirect:/"; //TODO make it return page you were on
        }
    }

    @GetMapping("/search")
    public String search(Model model, HttpSession session, @RequestParam String srch) {
        handleLoginStatus(session, model);
        pickRandomTopp(model);
        model.addAttribute("products", repository.search(srch));
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
    public String logoutPost(HttpSession session) {
        //TODO cookies and stuff
        session.removeAttribute("sessionCustomer");
        return "redirect:/";
    }

    @GetMapping("/registration")
    public ModelAndView registration(HttpSession session, Model model) {
        Customer customer = new Customer();
        handleLoginStatus(session, model);
        return new ModelAndView("registration").addObject("customer", customer);
    }

    @PostMapping("/registration")
    public ModelAndView registration(@Valid Customer customer, BindingResult bindingResult, Model model, HttpSession session) {
        handleLoginStatus(session, model);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("registration").addObject("customer", customer);
        }
        if (repository.checkIfCustomerExist(customer)) {
            String emailOccupied = "Email redan registrerat.";
            return new ModelAndView("registration").addObject("customer", customer).addObject("occupied", emailOccupied);
        }

        repository.registerCustomer(customer, new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/productinfo")

    public String productInfo (Model model, HttpSession session, @RequestParam int productID){
        pickRandomTopp(model);

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
    public String addReview(@ModelAttribute Review review) {
        repository.addReview(review);
        return "redirect:/productinfo?productID=" + review.getProductID();
    }

    @PostMapping("/addProduct")
    public String addProductToShoppingCart(HttpSession session, @RequestParam int productID) {
        System.out.println("Klick på lägg till produkt nr:" + productID);
        if (session.getAttribute("shoppingCart") == null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            session.setAttribute("shoppingCart", shoppingCart);
        }
        Product product = repository.getProduct(productID);
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        shoppingCart.addProduct(product);
        return "redirect:/productinfo?productID=" + productID;
    }

    @GetMapping("/addProductInCart")
    public String plusShoppingCart(HttpSession session, @RequestParam int productID) {
        Product product = repository.getProduct(productID);
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        shoppingCart.addProduct(product);
        return "redirect:shoppingcart";
    }

    @GetMapping("/removeProductInCart")
    public String minusShoppingCart(HttpSession session, @RequestParam int productID) {
        Product product = repository.getProduct(productID);
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        shoppingCart.removeProduct(product);
        return "redirect:shoppingcart";
    }

    @GetMapping("/shoppingcart")
    public String shoppingcart(Model model, HttpSession session){
        handleLoginStatus(session,model);
        pickRandomTopp(model);
        ShoppingCart shoppingCart;
        if (session.getAttribute("shoppingCart") != null) {
            shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        } else {
            shoppingCart = new ShoppingCart();
            session.setAttribute("shoppingCart", shoppingCart);
        }
        boolean isEmpty = (shoppingCart.getShoppingmap().isEmpty()) ? true : false;
        model.addAttribute("emptyCart", isEmpty);
        model.addAttribute("shoppingCart", shoppingCart);
        return "shoppingcart";
    }


    @GetMapping("/buyShoppingCart")
    public String buyShoppingCart( HttpSession session){
        List<Product> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        String email = "default@default.com";
        if (session.getAttribute("sessionCustomer") != null) {
            Customer customer = (Customer) session.getAttribute("sessionCustomer");
            email = customer.getEmail();
        }
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        for (Map.Entry<Integer, ProductWrapper> entry : shoppingCart.getShoppingmap().entrySet()) {
            products.add(entry.getValue().getProduct());
            quantities.add(entry.getValue().getQuantity());
        }

  repository.addOrder(products, quantities, email, "Inget");
        return "redirect:/emptyShoppingCart";
    }

    @GetMapping("/emptyShoppingCart")
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

        model.addAttribute("rubrik",sc);
        model.addAttribute("category", topThree.peek().getSubcategory());
        model.addAttribute("topProducts", topThree);
        model.addAttribute("productsInSubcategory", products);
        pickRandomTopp(model);

        return "subcategory";
    }


    @PostMapping("/checkout")
    @ResponseBody
    public String checkout(HttpSession session) {
        List<Product> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
        for (Map.Entry<Integer, ProductWrapper> entry : shoppingCart.getShoppingmap().entrySet()) {
            products.add(entry.getValue().getProduct());
            quantities.add(entry.getValue().getQuantity());
        }

        Client client = DefaultClient.newInstance(
                "PK00448_b783e776f1ae",
                "Ra7CAQA0tSSU5uPM",
                Client.EU_TEST_BASE_URL);
        CheckoutOrder checkout;
        CheckoutOrderData orderData;
        if (session.getAttribute("klarna_order_id") != null) {
            checkout = client.newCheckoutOrder("" + session.getAttribute("klarna_order_id"));
            checkout.fetch();
            orderData = checkout.update(createOrderData(products, quantities));
        } else {
            checkout = client.newCheckoutOrder();
            checkout.create(createOrderData(products, quantities));
            orderData = checkout.fetch();
            session.setAttribute("klarna_order_id", orderData.getOrderId());
        }

        return orderData.getHtmlSnippet();
    }

    @GetMapping("/confirmation")
    public String confirmation(Model model, HttpSession session, @RequestParam String klarna_order_id) {
        Client client = DefaultClient.newInstance(
                "PK00448_b783e776f1ae",
                "Ra7CAQA0tSSU5uPM",
                Client.EU_TEST_BASE_URL);

        CheckoutOrder checkout = client.newCheckoutOrder(klarna_order_id);
        CheckoutOrderData orderData = checkout.fetch();
        model.addAttribute("confirmation", orderData.getHtmlSnippet());
        String email = "default@gmail.com";
        if (session.getAttribute("sessionCustomer") != null) {
            Customer customer = (Customer) session.getAttribute("sessionCustomer");
            email = customer.getEmail();
        }
        createOrder((ShoppingCart) session.getAttribute("shoppingCart"), email, klarna_order_id);
        session.removeAttribute("shoppingCart");
        session.removeAttribute("klarna_order_id");

        return "confirmation";
    }

    @GetMapping("/push")
    public void confirmation(Model model, @RequestParam String klarna_order_id) {
        Client client = DefaultClient.newInstance(
                "PK00448_b783e776f1ae",
                "Ra7CAQA0tSSU5uPM",
                Client.EU_TEST_BASE_URL);

        Order order = client.newOrder(klarna_order_id);
        OrderData data = order.fetch();

        order.acknowledge();
    }
    private void handleLoginStatus(HttpSession session, Model model) {
        boolean isLogedIn;
        if (session.getAttribute("sessionCustomer") == null) {
            isLogedIn = false;
        } else {
            isLogedIn = true;
        }
        if (session.getAttribute("loginFail") != null) {
            model.addAttribute("loginFail", session.getAttribute("loginFail"));
        }
        model.addAttribute("isLogedIn", isLogedIn);
    }

    private void handleAddSubCategories(Model model){
        model.addAttribute("herrdoft", repository.getBySubCategoryTop3("Herrdoft"));
        model.addAttribute("damdoft", repository.getBySubCategoryTop3("Damdoft"));
        model.addAttribute("hudvard", repository.getBySubCategoryTop3("Hudvård"));
    }

    private void pickRandomTopp(Model model){
        Random random = new Random();
        int nmbr = random.nextInt(5);
        switch (nmbr){
            case 1: model.addAttribute("randomTopp", repository.getBySubCategoryTop3("Hudvård"));
            break;
            case 2: model.addAttribute("randomTopp", repository.getBySubCategoryTop3("Herrdoft"));
            break;
            case 3: model.addAttribute("randomTopp", repository.getBySubCategoryTop3("Damdoft"));
            break;
            default: model.addAttribute("randomTopp", repository.getBySubCategoryTop3("Smink"));
        }
    }
    private CheckoutOrderData createOrderData(List<Product> products, List<Integer> quantities) {
        final List<OrderLine> lines = new ArrayList<>();
        long totalAmount = 0;

       for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            lines.add(new OrderLine()
                    .setType("physical")
                    .setReference("" + product.getProductID())
                    .setName(product.getName())
                    .setQuantity((long) quantities.get(i))
                    .setQuantityUnit("st")
                    .setUnitPrice((long) product.getPrice() * 100)
                    .setTaxRate(2500)
                    .setTotalAmount((long) (product.getPrice() * 100 * quantities.get(i)))
                    .setTotalTaxAmount((long) (product.getPrice() * 100 * quantities.get(i) * 0.20 ))
            );
            totalAmount += (long) (product.getPrice() * 100 * quantities.get(i));
        }

        final MerchantUrls urls = new MerchantUrls() {
            {
                setTerms(
                        "http://localhost:8080/terms");
                setCheckout(
                        "http://localhost:8080/checkout?klarna_order_id={checkout.order.id}");
                setConfirmation(
                        "http://localhost:8080/confirmation?klarna_order_id={checkout.order.id}");
                setPush(
                        "http://localhost:8080/push?klarna_order_id={checkout.order.id}");
            }
        };
        final long totAmount = totalAmount;
        final long totTaxAmount = (long) (totalAmount * 0.20);

        CheckoutOrderData orderData = new CheckoutOrderData() {
            {
                setPurchaseCountry("se");
                setPurchaseCurrency("sek");
                setLocale("sv-se");
                setOrderAmount(totAmount);
                setOrderTaxAmount(totTaxAmount);
                setOrderLines(lines);
                setMerchantUrls(urls);
            }
        };
        return orderData;
    }

    private void createOrder(ShoppingCart shoppingCart, String email, String klarna_order_id) {
        List<Product> products = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        for (Map.Entry<Integer, ProductWrapper> entry : shoppingCart.getShoppingmap().entrySet()) {
            products.add(entry.getValue().getProduct());
            quantities.add(entry.getValue().getQuantity());
        }
        repository.addOrder(products, quantities, email, klarna_order_id);
    }
}
