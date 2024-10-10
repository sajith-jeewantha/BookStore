/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "loadCheckOut", urlPatterns = {"/loadCheckOut"})
public class loadCheckOut extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject jsonObject = new JsonObject();
        HttpSession session = request.getSession();
        Gson gson = new Gson();

        Session openSession = HibernateUtil.getSessionFactory().openSession();

        if (session.getAttribute("user") != null) {

            User_DTO user_DTO = (User_DTO) session.getAttribute("user");

            Criteria userCriteria = openSession.createCriteria(User.class);
            userCriteria.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user = (User) userCriteria.uniqueResult();

            Criteria addressCriteria = openSession.createCriteria(Address.class);
            addressCriteria.add(Restrictions.eq("user", user));
            addressCriteria.addOrder(Order.desc("id"));
            addressCriteria.setMaxResults(1);

            if (addressCriteria.list().isEmpty()) {
                jsonObject.addProperty("isAddressAvailable", false);
            } else {
                jsonObject.addProperty("isAddressAvailable", true);
                Address address = (Address) addressCriteria.list().get(0);
                address.setUser(null);
                jsonObject.add("address", gson.toJsonTree(address));
            }

            Criteria cityCriteria = openSession.createCriteria(City.class);
            cityCriteria.addOrder(Order.desc("id"));
            List<City> citylist = cityCriteria.list();

            Criteria cartCriteria = openSession.createCriteria(Cart.class);
            cartCriteria.add(Restrictions.eq("user", user));
            cartCriteria.addOrder(Order.desc("id"));
            List<Cart> cartlist = cartCriteria.list();

            jsonObject.add("citylist", gson.toJsonTree(citylist));

            for (Cart cart : cartlist) {
                cart.setUser(null);
                cart.getBook().setUser(null);
            }

            jsonObject.add("cartlist", gson.toJsonTree(cartlist));
            jsonObject.addProperty("success", true);

        } else {
            //not signed in
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("message", "Not signed in");
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
        openSession.close();

    }
}
