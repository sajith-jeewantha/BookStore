/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import entity.Book;
import entity.Book_Type;
import entity.Author;
import entity.Book_Condition;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "AdvanceSearch", urlPatterns = {"/AdvanceSearch"})
public class AdvanceSearch extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject responceJsonObject = new JsonObject();
        responceJsonObject.addProperty("success", false);

        Gson gson = new Gson();

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        String title = requestJsonObject.get("title").getAsString();
        String type_id = requestJsonObject.get("type").getAsString();
        String author_id = requestJsonObject.get("author").getAsString();
        String condition_id = requestJsonObject.get("condition").getAsString();
        String year = requestJsonObject.get("year").getAsString();
        String sort_text = requestJsonObject.get("sort").getAsString();
        String start_price = requestJsonObject.get("startprice").getAsString();
        String end_price = requestJsonObject.get("endprice").getAsString();
        int firstResult = requestJsonObject.get("firstResult").getAsInt();

        Session openSession = HibernateUtil.getSessionFactory().openSession();
        Criteria bookCriteria = openSession.createCriteria(Book.class);

        //title
        if (!title.isEmpty()) {
            bookCriteria.add(Restrictions.like("title", title, MatchMode.ANYWHERE));
        }

        //type
        if (!type_id.isEmpty()) {

            Criteria BookTypeCriteria = openSession.createCriteria(Book_Type.class);
            BookTypeCriteria.add(Restrictions.eq("id", Integer.parseInt(type_id)));
            Book_Type book_type = (Book_Type) BookTypeCriteria.uniqueResult();

            if (book_type != null) {
                bookCriteria.add(Restrictions.eq("type", book_type));
            } else {
                responceJsonObject.addProperty("message", "Invalid Type");
            }

        }

        //author
        if (!author_id.isEmpty()) {
            Criteria AuthorCriteria = openSession.createCriteria(Author.class);
            AuthorCriteria.add(Restrictions.eq("id", Integer.parseInt(author_id)));
            Author author = (Author) AuthorCriteria.uniqueResult();

            if (author != null) {
                bookCriteria.add(Restrictions.eq("author", author));
            } else {
                responceJsonObject.addProperty("message", "Invalid Author");
            }
        }

        //condition
        if (!condition_id.isEmpty()) {

            Criteria ConditionCriteria = openSession.createCriteria(Book_Condition.class);
            ConditionCriteria.add(Restrictions.eq("id", Integer.parseInt(condition_id)));
            Book_Condition book_condition = (Book_Condition) ConditionCriteria.uniqueResult();

            if (book_condition != null) {
                bookCriteria.add(Restrictions.eq("condition", book_condition));
            } else {
                responceJsonObject.addProperty("message", "Invalid Condition");
            }

        }
        //year
        if (!year.isEmpty()) {

            if (!Validation.isYearValid(String.valueOf(year))) {
                responceJsonObject.addProperty("message", "Invalid Year");
            } else {
                bookCriteria.add(Restrictions.eq("year", year));
            }
        }

        //sort
        if (sort_text.equals("Sort by Latest")) {
            bookCriteria.addOrder(Order.desc("id"));

        } else if (sort_text.equals("Sort by Oldest")) {
            bookCriteria.addOrder(Order.asc("id"));

        } else if (sort_text.equals("Sort by Title")) {
            bookCriteria.addOrder(Order.asc("title"));

        } else if (sort_text.equals("Sort by Price Low to high")) {
            bookCriteria.addOrder(Order.asc("price"));

        } else if (sort_text.equals("Sort by Price high to Low")) {
            bookCriteria.addOrder(Order.desc("price"));
        }

        //price
        if (!start_price.isEmpty()) {
            bookCriteria.add(Restrictions.ge("price", Double.parseDouble(start_price)));
        }

        if (!end_price.isEmpty()) {
            bookCriteria.add(Restrictions.le("price", Double.parseDouble(end_price)));
        }

        responceJsonObject.addProperty("allProductCount", bookCriteria.list().size());

        if (Validation.isInteger(String.valueOf(firstResult))) {
            bookCriteria.setFirstResult(firstResult);
            bookCriteria.setMaxResults(4);
        }
        
        List<Book> bookList = bookCriteria.list();

        for (Book book : bookList) {
            book.setUser(null);
        }

        responceJsonObject.addProperty("success", true);
        responceJsonObject.add("bookList", gson.toJsonTree(bookList));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responceJsonObject));
    }

}
