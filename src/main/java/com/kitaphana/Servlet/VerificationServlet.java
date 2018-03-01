package com.kitaphana.Servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/verification")
public class VerificationServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/views/verification.jsp").forward(request, response);
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getSession().setAttribute("name", request.getParameter("name"));
        request.getSession().setAttribute("surname", request.getParameter("surname"));
        response.sendRedirect("/main");
    }
}