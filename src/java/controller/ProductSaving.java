/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import dto.Response_Message;
import dto.User_DTO;
import entity.Author;
import entity.Book;
import entity.Book_Condition;
import entity.Book_Status;
import entity.Book_Type;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@MultipartConfig
@WebServlet(name = "ProductSaving", urlPatterns = {"/ProductSaving"})
public class ProductSaving extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_Message response_message = new Response_Message();
        response_message.setStatus(false);
        Gson gson = new Gson();

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Part image = request.getPart("image");
        String type_id = request.getParameter("type");
        String author_id = request.getParameter("author");
        String condition_id = request.getParameter("condition");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");
        String year = request.getParameter("year");

        Session openSession = HibernateUtil.getSessionFactory().openSession();

        if (title.isEmpty()) {
            response_message.setMessage("Enter Title");

        } else if (description.isEmpty()) {
            response_message.setMessage("Enter Description");

        } else if (image.getSubmittedFileName() == null) {
            response_message.setMessage("Set image");

        } else if (Integer.parseInt(type_id) <= 0) {
            response_message.setMessage("Select Book Type");

        } else if (!Validation.isInteger(type_id)) {
            response_message.setMessage("Select Book Type");

        } else if (Integer.parseInt(author_id) <= 0) {
            response_message.setMessage("Select author");

        } else if (!Validation.isInteger(author_id)) {
            response_message.setMessage("Select author");

        } else if (Integer.parseInt(condition_id) <= 0) {
            response_message.setMessage("Select condition");

        } else if (!Validation.isInteger(condition_id)) {
            response_message.setMessage("Select condition");

        } else if (price.isEmpty()) {
            response_message.setMessage("Select Price");

        } else if (!Validation.isDouble(price)) {
            response_message.setMessage("Invalid Price");

        } else if (Double.parseDouble(price) <= 0) {
            response_message.setMessage("Price must be greater than 0");

        } else if (quantity.isEmpty()) {
            response_message.setMessage("Select Quantity");

        } else if (!Validation.isInteger(quantity)) {
            response_message.setMessage("Invalid Quantity");

        } else if (Integer.parseInt(quantity) <= 0) {
            response_message.setMessage("Quantity must be greater than 0");

        } else if (year.isEmpty()) {
            response_message.setMessage("Enter Year");

        } else if (!Validation.isInteger(year)) {
            response_message.setMessage("Invalid Year");

        } else if (!Validation.isYearValid(year)) {
            response_message.setMessage("Invalid year");
        } else {

            Book_Type book_Type = (Book_Type) openSession.get(Book_Type.class, Integer.parseInt(type_id));

            if (book_Type == null) {
                response_message.setMessage("Invalid Book Type");
            } else {

                Book_Condition book_condition = (Book_Condition) openSession.get(Book_Condition.class, Integer.parseInt(condition_id));
                if (book_condition == null) {
                    response_message.setMessage("Invalid Book Type");
                } else {

                    Author author = (Author) openSession.get(Author.class, Integer.parseInt(author_id));
                    if (author == null) {
                        response_message.setMessage("Invalid Book Type");
                    } else {

                        User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
                        Criteria userCriteria = openSession.createCriteria(User.class);
                        userCriteria.add(Restrictions.eq("email", user_DTO.getEmail()));
                        User user = (User) userCriteria.uniqueResult();

                        Book book = new Book();
                        book.setAuthor(author);
                        book.setCondition(book_condition);
                        book.setDate_time(new Date());
                        book.setDescription(description);
                        book.setPrice(Double.parseDouble(price));
                        book.setQty(Integer.parseInt(quantity));
                        book.setStatus((Book_Status) openSession.get(Book_Status.class, 1));
                        book.setTitle(title);
                        book.setType(book_Type);
                        book.setUser(user);
                        book.setYear(year);

                        int bid = (int) openSession.save(book);

                        String applicationpath = request.getServletContext().getRealPath("");
                        String newApplicationpath = applicationpath.replace("build" + File.separator + "web", "web");

                        File folder = new File(newApplicationpath + "//book-images//");

                        File file1 = new File(folder, bid + ".jpg");
                        InputStream inputStream1 = image.getInputStream();
                        Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        openSession.beginTransaction().commit();
                        openSession.close();

                        response_message.setStatus(true);
                        response_message.setMessage("Book Saved");
                    }
                }
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_message));
    }

}
