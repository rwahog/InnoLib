package com.kitaphana.Servlet;


import com.kitaphana.Entities.Employee;
import com.kitaphana.Entities.User;
import com.kitaphana.Service.LibrarianService;
import com.kitaphana.Service.PatronService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

  String phone_number;
  String password;
  PatronService patronService;
  LibrarianService librarianService;
  final static Logger logger = Logger.getLogger(LoginServlet.class);
//    TelegramBot telegramBot;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {
    HttpSession session = request.getSession(false);
    if (session != null && session.getAttribute("user") != null) {
      response.sendRedirect("/main");
    } else {
      request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {
    patronService = new PatronService();
    phone_number = request.getParameter("login");
    password = request.getParameter("password");
    librarianService = new LibrarianService();
    HttpSession session = request.getSession();
    String check = request.getParameter("remember");
    if (check != null) {
      session.setMaxInactiveInterval(24 * 60 * 60);
    } else {
      session.setMaxInactiveInterval(2 * 60 * 60);
    }
    boolean isValidUser = patronService.loginCheck(phone_number, password);
    if (isValidUser) {
      String role = patronService.getRole(phone_number);
      session.setAttribute("role", role);
      User user;
      if (role.equals("librarian") || role.equals("admin")) {
        Employee employee = librarianService.findByPhone(phone_number);
        session.setAttribute("user", employee);
        session.setAttribute("privilege", employee.getPrivilege());
      } else {
        user = patronService.findUserByPhoneNumber(phone_number);
        session.setAttribute("user", user);
      }
//      logger.info(user.getName() + user.getSurname() + " have logged in Kitaphana.");
      response.sendRedirect("/main");
    } else {
      request.setAttribute("errorMessage", "Invalid username or password");
      request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
  }
}