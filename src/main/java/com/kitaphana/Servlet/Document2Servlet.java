package com.kitaphana.Servlet;

import com.kitaphana.Entities.Document;
import com.kitaphana.Service.DocumentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/document2")
public class Document2Servlet extends HttpServlet {
  private DocumentService service = new DocumentService();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {

    Document document = service.setDocInfo(request.getParameter("id"));
    request.setAttribute("document", document);
    request.getRequestDispatcher("WEB-INF/views/document2.jsp").forward(request, response);
  }

}
