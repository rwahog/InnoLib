package com.kitaphana.Servlet;

import com.kitaphana.Entities.Patron;
import com.kitaphana.Entities.User;
import com.kitaphana.Service.DocumentHoldersService;
import com.kitaphana.Service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/holders")
public class DocumentHoldersServlet extends HttpServlet {

    DocumentHoldersService service = new DocumentHoldersService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ArrayList<Patron> patrons = service.fillPage(request.getParameter("id"));
        request.setAttribute("users", patrons);
        request.getRequestDispatcher("WEB-INF/views/documentHolders.jsp").forward(request, response);
    }
}
