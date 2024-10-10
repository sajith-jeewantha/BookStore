/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_Message;
import java.io.IOException;
import java.io.PrintWriter;
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
import entity.Book;
import entity.Book_Type;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            String bookId = request.getParameter("id");

            Response_Message response_message = new Response_Message();
            Gson gson = new Gson();

            JsonObject jsonObject = new JsonObject();
            response.setContentType("application/json");

            Session openSession = HibernateUtil.getSessionFactory().openSession();

            if (Validation.isInteger(bookId)) {

                Book book = (Book) openSession.get(Book.class, Integer.parseInt(bookId));

                if (book != null) {

                    book.getUser().setEmail(null);
                    book.getUser().setPassword(null);
                    book.getUser().setVerification(null);

                    Criteria criteria1 = openSession.createCriteria(Book_Type.class);
                    criteria1.add(Restrictions.eq("name", book.getType().getName()));
                    List<Book_Type> typeList = criteria1.list();

                    Criteria Bookcriteria = openSession.createCriteria(Book.class);
                    Bookcriteria.add(Restrictions.in("type", typeList));
                    Bookcriteria.add(Restrictions.ne("id", book.getId()));
                    Bookcriteria.setMaxResults(4);

                    List<Book> BookList = Bookcriteria.list();

                    for (Book Book1 : BookList) {
                        Book1.getUser().setEmail(null);
                        Book1.getUser().setPassword(null);
                        Book1.getUser().setVerification(null);
                    }
                    
                    response_message.setStatus(true);
                    jsonObject.add("book", gson.toJsonTree(book));
                    jsonObject.add("bookList", gson.toJsonTree(BookList));

                    openSession.close();

                } else {
                    response_message.setStatus(false);
                }
            } else {
                response_message.setMessage("book Not found");
            }

            jsonObject.add("resp", gson.toJsonTree(response_message));
            response.getWriter().write(gson.toJson(jsonObject));

        } catch (Exception e) {
            System.err.println("Errorrrr");
            e.printStackTrace();
        }

    }

}
