//package com.kitaphana.Servlet;
//
//import com.kitaphana.Entities.Address;
//import com.kitaphana.Entities.User;
//import com.kitaphana.Service.DBService;
//import com.kitaphana.Service.PatronService;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//@WebServlet(urlPatterns = "/profile")
//public class ProfileServlet extends HttpServlet {
//    PatronService patronService = new PatronService();
//    DBService dbService = new DBService();
//    User user = new User();
//
//    @Override
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        HttpSession session = request.getSession();
//        if (session.getAttribute("id") != null) {
//          user = patronService.findUserByPhoneNumber(((User) session.getAttribute("user"))
//                  .getPhoneNumber());
//        }
//        session.setAttribute("user", user);
//        request.getRequestDispatcher("WEB-INF/views/profile.jsp").forward(request, response);
//    }
//
//    @Override
//    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        String name = request.getParameter("name");
//        String surname = request.getParameter("surname");
//        String status = request.getParameter("status");
//        String phone_number = request.getParameter("phone_number");
//        String email = request.getParameter("email");
//        String password1 = request.getParameter("password1");
//        String password2 = request.getParameter("password2");
//        String country = request.getParameter("country");
//        String town = request.getParameter("town");
//        String street = request.getParameter("street");
//        int house_number = Integer.parseInt(request.getParameter("house_number"));
//        int apartment_number = Integer.parseInt(request.getParameter("apartment_number"));
//        String post_code = request.getParameter("postcode");
//
//        Address address = new Address(country, town, street, house_number, apartment_number, post_code);
//        address.setAddressId(patronService.getUserAddressId(Long.parseLong(request.getParameter("id"))));
//        User user = new User(name, surname, phone_number, password1, email, address);
//        user.setId(Long.parseLong(request.getParameter("id")));
//        user.setPossibleType(status);
//        user.setType(dbService.findColumn(request.getParameter("id"), "users", "type"));
//        patronService.editPatronInfo(user, "user");
//        dbService.updateColumn(request.getParameter("id"), status, "users", "possible_type");
//        response.sendRedirect("/main");
//    }
//}