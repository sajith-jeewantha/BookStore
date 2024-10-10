/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Book;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "Home", urlPatterns = {"/Home"})
public class Home extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseJsonObject = new JsonObject();

        Session openSession = HibernateUtil.getSessionFactory().openSession();

        //Featured Books
        Criteria featuredBooks = openSession.createCriteria(Book.class);
        featuredBooks.addOrder(Order.desc("id"));
        featuredBooks.setMaxResults(4);
        List<Book> booklist = featuredBooks.list();
        for (Book book : booklist) {
            book.setUser(null);
        }

        responseJsonObject.add("featuredBooks", gson.toJsonTree(booklist));
        
        //slide Books
        Criteria slideBooks = openSession.createCriteria(Book.class);
        slideBooks.addOrder(Order.asc("id"));
        slideBooks.setMaxResults(2);
        List<Book> slideBookslist = slideBooks.list();
        for (Book book : slideBookslist) {
            book.setUser(null);
        }

        responseJsonObject.add("slide", gson.toJsonTree(slideBookslist));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
    }

}
