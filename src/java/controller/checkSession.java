/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_Message;
import dto.User_DTO;
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
@WebServlet(name = "checkSession", urlPatterns = {"/checkSession"})
public class checkSession extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_Message response_message = new Response_Message();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject jsonObject = new JsonObject();

        Session openSession = HibernateUtil.getSessionFactory().openSession();

        if (request.getSession().getAttribute("user") != null) {
            //already Sign IN

            User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
            response_message.setStatus(true);
            response_message.setMessage(user_DTO);

        } else {
            //Not Sign IN
            response_message.setMessage("Not Sign in");
        }

        jsonObject.add("response_message", gson.toJsonTree(response_message));

        Gson gson1 = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson1.toJson(jsonObject));
    }

}
