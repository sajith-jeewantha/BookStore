/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Response_Message;
import dto.User_DTO;
import java.io.IOException;
import java.io.PrintWriter;
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
import entity.User;
import entity.User_Status;
import model.EmailTemplate;
import model.Mail;

/**
 *
 * @author sajithjeewantha
 */
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_Message response_Message = new Response_Message();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_dto = gson.fromJson(request.getReader(), User_DTO.class);

        if (user_dto.getFirst_name().isEmpty()) {
            response_Message.setMessage("Please Enter First Name");

        } else if (user_dto.getLast_name().isEmpty()) {
            response_Message.setMessage("Please Enter Last Name");

        } else if (user_dto.getMobile().isEmpty()) {
            response_Message.setMessage("Please Enter Mobile");

        } else if (!Validation.isMobileValid(user_dto.getMobile())) {
            response_Message.setMessage("Please Enter Valid Mobile");

        } else if (user_dto.getEmail().isEmpty()) {
            response_Message.setMessage("Please Enter Email");

        } else if (!Validation.isEmailValid(user_dto.getEmail())) {
            response_Message.setMessage("Please Enter Valid Email");

        } else if (user_dto.getPassword().isEmpty()) {
            response_Message.setMessage("Please Enter Password");

        } else {

            Session openSession = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = openSession.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", user_dto.getEmail()));

            if (!criteria.list().isEmpty()) {
                response_Message.setMessage("User is already exists");
            } else {

                User user = new User();
                user.setFirst_name(user_dto.getFirst_name());
                user.setLast_name(user_dto.getLast_name());
                user.setEmail(user_dto.getEmail());
                user.setMobile(user_dto.getMobile());
                user.setPassword(user_dto.getPassword());
                user.setUser_status((User_Status) openSession.load(User_Status.class, 1));
                int code = (int) (Math.random() * 1000000);
                user.setVerification(String.valueOf(code));

                openSession.save(user);

                Thread sendEmail = new Thread() {
                    @Override
                    public void run() {
                        EmailTemplate template = new EmailTemplate();
                        Mail.sendMail(user.getEmail(), "Verification Code", template.getTemplate1() + user.getVerification() + template.getTemplate2());
                    }
                };
                sendEmail.start();
                request.getSession().setAttribute("email", user.getEmail());
                response_Message.setStatus(true);
                openSession.beginTransaction().commit();
            }
            openSession.close();
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_Message));
    }
}
