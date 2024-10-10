/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Author;
import entity.Book_Condition;
import entity.Book_Status;
import entity.Book_Type;
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

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "LoadData", urlPatterns = {"/LoadData"})
public class LoadData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        Session openSession = HibernateUtil.getSessionFactory().openSession();

        Criteria AuthorCriteria = openSession.createCriteria(Author.class);
        AuthorCriteria.addOrder(Order.asc("name"));
        List<Author> AuthorlList = AuthorCriteria.list();

        Criteria Condition = openSession.createCriteria(Book_Condition.class);
        Condition.addOrder(Order.asc("name"));
        List<Book_Condition> ConditionList = Condition.list();

        Criteria Type = openSession.createCriteria(Book_Type.class);
        Type.addOrder(Order.asc("name"));
        List<Book_Type> TypeList = Type.list();

        JsonObject jo = new JsonObject();
        jo.add("AuthorlList", gson.toJsonTree(AuthorlList));
        jo.add("ConditionList", gson.toJsonTree(ConditionList));
        jo.add("TypeList", gson.toJsonTree(TypeList));

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jo));

        openSession.close();
    }

}
