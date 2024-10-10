/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dto.Cart_DTO;
import entity.Author;
import entity.User_Status;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import entity.Book;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "Test", urlPatterns = {"/Test"})
public class Test extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      HttpSession session = request.getSession();
        Session openSession = HibernateUtil.getSessionFactory().openSession();

        if (session.getAttribute("sessionCart") != null) {

            System.out.println("Have");

            ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) session.getAttribute("sessionCart");

            for (Cart_DTO cart_DTO : sessionCart) {
//                Product product = (Product) openSession.load(Product.class, cart_DTO.getProduct().getId());

                System.out.println(cart_DTO.getBook().getTitle());
                System.out.println(cart_DTO.getQty());
                System.out.println(cart_DTO);

            }

        } else {
            System.out.println("Have Not");

        }

    }

}
