/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.Response_Message;
import dto.User_DTO;
import entity.User;
import entity.Cart;
import entity.Book;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session openSession = HibernateUtil.getSessionFactory().openSession();

        Transaction transaction = openSession.beginTransaction();

        Response_Message response_message = new Response_Message();
        Gson gson = new Gson();

        try {

            String id = request.getParameter("id");
            String qty = request.getParameter("qty");

            if (!Validation.isInteger(id)) {
                response_message.setMessage("Invalid id");

            } else if (!Validation.isInteger(qty)) {
                response_message.setMessage("Invalid Qty");

            } else {

                int productId = Integer.parseInt(id);
                int productQty = Integer.parseInt(qty);

                if (productQty <= 0) {
                    response_message.setMessage("qty must be greater than 0");
                } else {

                    Book book = (Book) openSession.get(Book.class, productId);

                    if (book != null) {
                        if (request.getSession().getAttribute("user") != null) {
                            //DB Cart

                            User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");

                            Criteria Usercriteria = openSession.createCriteria(User.class);
                            Usercriteria.add(Restrictions.eq("email", user_DTO.getEmail()));
                            User user = (User) Usercriteria.uniqueResult();

                            Criteria cartCriteria = openSession.createCriteria(Cart.class);
                            cartCriteria.add(Restrictions.eq("user", user));
                            cartCriteria.add(Restrictions.eq("book", book));

                            if (cartCriteria.list().isEmpty()) {
                                if (productQty <= book.getQty()) {

                                    Cart cart = new Cart();
                                    cart.setBook(book);
                                    cart.setQty(productQty);
                                    cart.setUser(user);

                                    openSession.save(cart);
                                    transaction.commit();
                                    response_message.setStatus(true);
                                    response_message.setMessage("Successfully added to your cart");

                                } else {
                                    response_message.setMessage("Higher Quantity");
                                }
                            } else {

                                Cart cart = (Cart) cartCriteria.list().get(0);

                                if ((cart.getQty() + productQty) <= book.getQty()) {
                                    cart.setQty(cart.getQty() + productQty);
                                    openSession.update(cart);
                                    transaction.commit();
                                    response_message.setStatus(true);
                                    response_message.setMessage("Card Updated");

                                } else {
                                    response_message.setMessage("Higher Quantity");
                                }
                            }
                        } else {
                            //Session Cart
                            HttpSession session = request.getSession();
                            if (session.getAttribute("sessionCart") != null) {

                                ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) session.getAttribute("sessionCart");

                                Cart_DTO cartFound = null;

                                for (Cart_DTO cart_DTO : sessionCart) {
                                    if (cart_DTO.getBook().getId() == book.getId()) {
                                        cartFound = cart_DTO;
                                        break;
                                    }
                                }

                                if (cartFound != null) {

                                    if ((cartFound.getQty() + productQty) <= book.getQty()) {
                                        cartFound.setQty(cartFound.getQty() + productQty);

                                        sessionCart.remove(cartFound);
                                        sessionCart.add(cartFound);
                                        response_message.setStatus(true);
                                        response_message.setMessage("Card Updated");

                                    } else {
                                        response_message.setMessage("Higher Quantity");
                                    }

                                } else {

                                    if (productQty <= book.getQty()) {
                                        Cart_DTO cart_DTO = new Cart_DTO();
                                        cart_DTO.setBook(book);
                                        cart_DTO.setQty(productQty);

                                        sessionCart.add(cart_DTO);
                                        response_message.setStatus(true);
                                        response_message.setMessage("Successfully added to your cart");

                                    } else {
                                        response_message.setMessage("Higher Quantity");
                                    }

                                }

                            } else {
                                ArrayList<Cart_DTO> sessionCart = new ArrayList<>();

                                if (productQty <= book.getQty()) {

                                    Cart_DTO cart_DTO = new Cart_DTO();
                                    cart_DTO.setBook(book);
                                    cart_DTO.setQty(productQty);

                                    sessionCart.add(cart_DTO);
                                    session.setAttribute("sessionCart", sessionCart);
                                    response_message.setStatus(true);
                                    response_message.setMessage("Successfully added to your cart");

                                } else {
                                    response_message.setMessage("Higher Quantity");
                                }
                            }
                        }

                    } else {
                        response_message.setMessage("Unavailable Product");
                    }
                }
            }

        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        openSession.close();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_message));

    }

}
