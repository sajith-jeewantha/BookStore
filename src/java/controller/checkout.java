/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Book;
import entity.Cart;
import entity.City;
import entity.Order_Item;
import entity.Order_Status;
import entity.Orders;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.PayHere;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "checkout", urlPatterns = {"/checkout"})
public class checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        HttpSession session = request.getSession();

        Session openSession = HibernateUtil.getSessionFactory().openSession();
        Transaction beginTransaction = openSession.beginTransaction();

        boolean isCurrentAddress = requestJsonObject.get("checkAddress").getAsBoolean();
        String first_name = requestJsonObject.get("first_name").getAsString();
        String last_name = requestJsonObject.get("last_name").getAsString();
        String city_id = requestJsonObject.get("city_id").getAsString();
        String line1 = requestJsonObject.get("line1").getAsString();
        String line2 = requestJsonObject.get("line2").getAsString();
        String postal_code = requestJsonObject.get("postal_code").getAsString();
        String mobile = requestJsonObject.get("mobile").getAsString();

        if (session.getAttribute("user") != null) {

            User_DTO user_DTO = (User_DTO) session.getAttribute("user");

            Criteria userCriteria = openSession.createCriteria(User.class);
            userCriteria.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user = (User) userCriteria.uniqueResult();

            if (isCurrentAddress) {

                Criteria addressCriteria = openSession.createCriteria(Address.class);
                addressCriteria.add(Restrictions.eq("user", user));
                addressCriteria.addOrder(Order.desc("id"));
                addressCriteria.setMaxResults(1);
                addressCriteria.uniqueResult();

                if (addressCriteria.list().isEmpty()) {
                    responseJsonObject.addProperty("message", "Current Address not found. Please create a new Address");
                } else {

                    //Check out with old Address
                    Address address = (Address) addressCriteria.list().get(0);
                    saveOrders(openSession, beginTransaction, user, address, responseJsonObject);

                }

            } else {

                if (first_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill First Name");

                } else if (last_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill Last Name");

                } else if (mobile.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill Mobile");

                } else if (!Validation.isMobileValid(mobile)) {
                    responseJsonObject.addProperty("message", "Invalid Mobile Number");

                } else if (line1.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill Address Line 1");

                } else if (line2.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please fill Address Line 2");

                } else if (city_id.equals("0")) {
                    responseJsonObject.addProperty("message", "Invalid City Selected");

                } else {

                    Criteria cityCriteria = openSession.createCriteria(City.class);
                    cityCriteria.add(Restrictions.eq("id", Integer.parseInt(city_id)));

                    if (cityCriteria.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Please Selecte city");
                    } else {
                        City city = (City) cityCriteria.list().get(0);

                        if (postal_code.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please fill Postal Code");

                        } else if (postal_code.length() != 5) {
                            responseJsonObject.addProperty("message", "Invalid Postal Code");

                        } else if (!Validation.isInteger(postal_code)) {
                            responseJsonObject.addProperty("message", "Invalid Postal Code");

                        } else {

                            Address address = new Address();
                            address.setCity(city);
                            address.setFirst_name(first_name);
                            address.setLast_name(last_name);
                            address.setLine1(line1);
                            address.setLine2(line2);
                            address.setMobile(mobile);
                            address.setPostal_code(postal_code);
                            address.setUser(user);

                            openSession.save(address);

                            //Check out with new Address
                            saveOrders(openSession, beginTransaction, user, address, responseJsonObject);
                        }
                    }
                }

            }
        } else {

            responseJsonObject.addProperty("message", "User not Signed in");
        }

        openSession.close();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
    }

    private void saveOrders(Session openSession, Transaction beginTransaction, User user, Address address, JsonObject responseJsonObject) {

        try {
            Orders order = new Orders();
            order.setAddress(address);
            order.setDate(new Date());
            order.setStatus((Order_Status) openSession.get(Order_Status.class, 5));
            order.setUser(user);
            int order_id = (int) openSession.save(order);

            Criteria cartCriteria = openSession.createCriteria(Cart.class);
            cartCriteria.add(Restrictions.eq("user", user));
            List<Cart> cartlList = cartCriteria.list();

            double amount = 0;
            String items = "";
            for (Cart cart : cartlList) {

                //Calculate amount
                amount += cart.getQty() * cart.getBook().getPrice();

                //Calculate shipping
                if (address.getCity().getId() == 1) {
                    amount += 1000;
                } else {
                    amount += 2500;
                }

                //items
                items += cart.getBook().getTitle() + " x " + cart.getQty();

                Book book = cart.getBook();

                Order_Item order_Item = new Order_Item();
                order_Item.setOrder(order);
                order_Item.setBook(cart.getBook());
                order_Item.setQty(cart.getQty());
                openSession.save(order_Item);

                book.setQty(book.getQty() - cart.getQty());
                openSession.update(book);

                openSession.delete(cart);
            }

            beginTransaction.commit();

            //Set Payment Data
            String merahantID = ".........";
            String amountFormatted = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchantSecret = "................................................";
            String merchantSecretMd5Hash = PayHere.generateMd5(merchantSecret);

            JsonObject payHere = new JsonObject();
            payHere.addProperty("sandbox", true);
            payHere.addProperty("merchant_id", merahantID);

            payHere.addProperty("return_url", "");
            payHere.addProperty("cancel_url", "");
            payHere.addProperty("notify_url", "VerifyPayments");

            payHere.addProperty("first_name", user.getFirst_name());
            payHere.addProperty("last_name", user.getLast_name());
            payHere.addProperty("email", user.getEmail());
            payHere.addProperty("phone", address.getMobile());
            payHere.addProperty("address", address.getLine1() + " " + address.getLine2());
            payHere.addProperty("city", address.getCity().getName());
            payHere.addProperty("country", "Sri lnaka");
            payHere.addProperty("order_id", String.valueOf(order_id));
            payHere.addProperty("items", items);
            payHere.addProperty("amount", amountFormatted);
            payHere.addProperty("currency", currency);

            //merahantID + orderID + amountFormatted + currency + getMd5(merchantSecret)
            String Md5Hash = PayHere.generateMd5(merahantID + order_id + amountFormatted + currency + merchantSecretMd5Hash);
            payHere.addProperty("hash", Md5Hash);

            responseJsonObject.addProperty("success", true);
            responseJsonObject.addProperty("message", "Checkout Compeleted");

            Gson gson = new Gson();
            responseJsonObject.add("payherejson", gson.toJsonTree(payHere));

        } catch (Exception e) {
            beginTransaction.rollback();
        }
    }
}
