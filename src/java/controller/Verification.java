/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_Message;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.EmailTemplate;
import model.HibernateUtil;
import model.Mail;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "Verification", urlPatterns = {"/Verification"})
public class Verification extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_Message response_Message = new Response_Message();
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        String code = jsonObject.get("code").getAsString();

        if (code.isEmpty()) {
            response_Message.setMessage("Enter Verification Code");

        } else if (!Validation.isInteger(code)) {
            response_Message.setMessage("Invalid Verification Code");

        } else {

            String email = (String) request.getSession().getAttribute("email");

            Session openSession = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = openSession.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));
            criteria.add(Restrictions.eq("verification", code));

            if (criteria.list().isEmpty()) {
                response_Message.setMessage("Invalid Verification Code");
            } else {

                User user = (User) criteria.list().get(0);
                user.setVerification("verified");

                openSession.update(user);
                openSession.beginTransaction().commit();

                Thread sendEmail = new Thread() {
                    @Override
                    public void run() {
                        EmailTemplate template = new EmailTemplate();
                        Mail.sendMail(user.getEmail(), "Verification Code", template.getTemplate1() + "Successfully Verified your Account" + template.getTemplate2());
                    }
                };
                sendEmail.start();

                request.getSession().removeAttribute("email");
                User_DTO user_dto = new User_DTO(user.getFirst_name(), user.getLast_name(), user.getEmail(), user.getMobile());
                request.getSession().setAttribute("user", user_dto);

                response_Message.setStatus(true);
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_Message));
    }
}
