package com.kitaphana.Servlet;

import com.kitaphana.Entities.AVMaterial;
import com.kitaphana.Entities.Book;
import com.kitaphana.Entities.Document;
import com.kitaphana.Entities.JournalArticle;
import com.kitaphana.Service.DBService;
import com.kitaphana.Service.DocumentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/librarianPanel/addDocument", "/librarianPanel/editDocument"})
public class AddDocumentServlet extends HttpServlet {

  DocumentService service = new DocumentService();
  DBService dbService = new DBService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    if (request.getRequestURI().equals("/librarianPanel/editDocument")) {
      String docId = request.getParameter("id");
      String type = dbService.findColumn(docId, "documents", "type");
      Document doc = new Document();
      switch (type) {
        case "book":
          doc = dbService.findDocumentAndTypeInfo(docId, "books");
          break;
        case "ja":
          doc = dbService.findDocumentAndTypeInfo(docId, type);
          break;
        case "av":
          doc = dbService.findDocumentAndTypeInfo(docId, type);
          break;
      }
      request.setAttribute("doc", doc);
    }
    request.getRequestDispatcher("/WEB-INF/views/addDocument.jsp").forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String title = request.getParameter("title");
    String authors = request.getParameter("authors");
    String description = request.getParameter("description");
    String keywords = request.getParameter("keywords");
    String type = request.getParameter("document-type");
    int price = Integer.parseInt(request.getParameter("price"));
    int amount = Integer.parseInt(request.getParameter("amount"));
    boolean unique = dbService.checkUnique(title, authors, type);
    switch (type) {
      case "book":
        int edition_number = Integer.parseInt(request.getParameter("edition_number"));
        String publisher = request.getParameter("publisher");
        int year = Integer.parseInt(request.getParameter("year"));
        int bestseller;
        if (request.getParameter("bestseller") == null) {
          bestseller = 0;
        } else {
          bestseller = Integer.parseInt(request.getParameter("bestseller"));
        }
        if (unique) {
          Book book = new Book(title, price, amount, type, description, publisher, year, edition_number, bestseller);
          book.setAuthorsId(authors);
          book.setKeywordsId(keywords);
          service.saveDocument(book);
        }
        break;
      case "article":
        String editors = request.getParameter("editors");
        String journal_name = request.getParameter("journal_name");
        String date = request.getParameter("date");
        if (unique) {
          JournalArticle journalArticle = new JournalArticle(title, price, amount, type, description, editors, journal_name, date);
          journalArticle.setAuthorsId(authors);
          journalArticle.setKeywordsId(keywords);
          service.saveDocument(journalArticle);
        }
        break;
      case "av":
        if (unique) {
          AVMaterial avMaterial = new AVMaterial(title, price, amount, type, description);
          avMaterial.setAuthorsId(authors);
          avMaterial.setKeywordsId(keywords);
          service.saveDocument(avMaterial);
        }
        break;
    }
    response.sendRedirect("/librarianPanel");
  }
}
