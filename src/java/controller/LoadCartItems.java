/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.User;
import entity.Book;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "LoadCartItems", urlPatterns = {"/LoadCartItems"})
public class LoadCartItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        HttpSession session = request.getSession();
        ArrayList<Cart_DTO> cart_DTO_List = new ArrayList<>();
        Session openSession = HibernateUtil.getSessionFactory().openSession();
        try {

            if (session.getAttribute("user") != null) {
                //DB Cart
                User_DTO user_DTO = (User_DTO) session.getAttribute("user");

                Criteria criteria = openSession.createCriteria(User.class);
                criteria.add(Restrictions.eq("email", user_DTO.getEmail()));
                User user = (User) criteria.uniqueResult();

                Criteria cartCriteria = openSession.createCriteria(Cart.class);
                cartCriteria.add(Restrictions.eq("user", user));

                List<Cart> cartList = cartCriteria.list();

                for (Cart cart : cartList) {

                    Cart_DTO cart_DTO = new Cart_DTO();

                    Book product = cart.getBook();
                    product.setUser(null);

                    cart_DTO.setBook(product);
                    cart_DTO.setQty(cart.getQty());

                    cart_DTO_List.add(cart_DTO);
                }

            } else {
                //Session Cart

                if (session.getAttribute("sessionCart") != null) {

                    cart_DTO_List = (ArrayList<Cart_DTO>) session.getAttribute("sessionCart");

                    for (Cart_DTO cart_DTO : cart_DTO_List) {
                        cart_DTO.getBook().setUser(null);
                    }

                } else {
                    //cart empty

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        openSession.close();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(cart_DTO_List));

    }

}
