/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Cart_DTO;
import dto.Response_Message;
import dto.User_DTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_Message response_Message = new Response_Message();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_dto = gson.fromJson(request.getReader(), User_DTO.class);

        if (user_dto.getEmail().isEmpty()) {
            response_Message.setMessage("Enter Email");

        } else if (!Validation.isEmailValid(user_dto.getEmail())) {
            response_Message.setMessage("Invalid Email");

        } else if (user_dto.getPassword().isEmpty()) {
            response_Message.setMessage("Enter Password");

        } else {

            Session openSession = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = openSession.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", user_dto.getEmail()));
            criteria.add(Restrictions.eq("password", user_dto.getPassword()));

            if (!criteria.list().isEmpty()) {
                User user = (User) criteria.list().get(0);
                if (!user.getVerification().equals("verified")) {

                    request.getSession().setAttribute("email", user.getEmail());
                    response_Message.setMessage("NotVerified");

                } else {

                    if (request.getSession().getAttribute("sessionCart") != null) {
                        //DB Cart is Empty
                        //Session cart save to DB.
                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) request.getSession().getAttribute("sessionCart");

                        Criteria cartCriteria = openSession.createCriteria(Cart.class);
                        cartCriteria.add(Restrictions.eq("user", user));
                        List<Cart> DBCart = cartCriteria.list();

                        if (DBCart.isEmpty()) {
                            for (Cart_DTO cart_DTO : sessionCart) {

                                Cart cart = new Cart();
                                cart.setBook(cart_DTO.getBook());
                                cart.setUser(user);
                                cart.setQty(cart_DTO.getQty());
                                openSession.save(cart);
                            }
                        } else {

                            for (Cart_DTO cart_DTO : sessionCart) {

                                boolean isFoundInDBCart = false;

                                for (Cart cart : DBCart) {

                                    if (cart_DTO.getBook().getId() == cart.getBook().getId()) {
                                        isFoundInDBCart = true;

                                        if ((cart_DTO.getQty() + cart.getQty()) <= cart.getBook().getQty()) {
                                            //quantity available

                                            cart.setQty(cart_DTO.getQty() + cart.getQty());
                                            openSession.update(cart);

                                        } else {
                                            //quantity available
                                            cart.setQty(cart.getBook().getQty());
                                            openSession.update(cart);
                                        }
                                    }
                                }

                                if (!isFoundInDBCart) {
                                    //not found in DB cart
                                    Cart cart = new Cart();
                                    cart.setBook(cart_DTO.getBook());
                                    cart.setUser(user);
                                    cart.setQty(cart_DTO.getQty());
                                    openSession.save(cart);
                                }

                            }

                        }
                        request.getSession().removeAttribute("sessionCart");
                        openSession.beginTransaction().commit();
                    }

                    user_dto.setEmail(user.getEmail());
                    user_dto.setFirst_name(user.getFirst_name());
                    user_dto.setLast_name(user.getLast_name());
                    user_dto.setMobile(user.getMobile());
                    user_dto.setPassword(null);

                    request.getSession().setAttribute("user", user_dto);

                    response_Message.setStatus(true);

                }
            } else {
                response_Message.setMessage("Email or Password Incorrect");
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_Message));

    }
}
