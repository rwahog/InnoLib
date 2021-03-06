package com.kitaphana.dao;

import com.kitaphana.Database.Database;
import com.kitaphana.Entities.AVMaterial;
import com.kitaphana.Entities.Book;
import com.kitaphana.Entities.Document;
import com.kitaphana.Entities.JournalArticle;
import com.kitaphana.exceptions.DocumentNotFoundException;
import com.kitaphana.exceptions.OperationFailedException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class commonDAOImpl implements commonDAO {

  private Database db = Database.getInstance();
  private static final String FIND_LAST_ID = "SELECT MAX(id) AS maxID FROM %s";
  private static final String DELETE = "DELETE FROM %s WHERE %s=?";
  private static final String FIND_BY_DOCUMENT_ID = "SELECT * FROM %s WHERE document_id=?";

  @Override
  public long findLastId(String table) {
    long id = 0;
    try {
      String statement = String.format(FIND_LAST_ID, table);
      PreparedStatement ps = db.connect().prepareStatement(statement);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        id = rs.getInt("maxID");
      } else {
        throw new OperationFailedException();
      }
    } catch (SQLException e) {
      throw new OperationFailedException();
    }
    return id;
  }

  @Override
  public void delete(long id, String table, String column) {
    try {
      String statement = String.format(DELETE, table, column);
      PreparedStatement ps = db.connect().prepareStatement(statement);
      ps.setLong(1, id);

      ps.executeUpdate();
      ps.close();
    } catch (SQLException e) {
      throw new OperationFailedException();
    }
  }

  public Document findByDocumentId(String docId, String table) {
    Document document = new Document();
    try {
      String statement = String.format(FIND_BY_DOCUMENT_ID, table);
      PreparedStatement ps = db.connect().prepareStatement(statement);
      ps.setLong(1, Long.parseLong(docId));

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        switch (table) {
          case "books":
            Book book = new Book();
            book.setTitle(rs.getString("title"));
            book.setPublisher(rs.getString("publisher"));
            book.setYear(rs.getInt("year"));
            book.setEditionNumber(rs.getInt("edition_number"));
            book.setBestseller(rs.getInt("best_seller"));
            book.setDocumentId(rs.getLong("document_id"));
            document = book;
            break;
          case "ja":
            JournalArticle journalArticle = new JournalArticle();
            journalArticle.setTitle(rs.getString("title"));
            journalArticle.setDate(rs.getString("date"));
            journalArticle.setJournalName(rs.getString("journal_name"));
            journalArticle.setEditors(rs.getString("editors"));
            journalArticle.setDocumentId(rs.getLong("document_id"));
            document = journalArticle;
            break;
          case "av":
            AVMaterial avMaterial = new AVMaterial();
            avMaterial.setTitle(rs.getString("title"));
            avMaterial.setDocumentId(rs.getLong("document_id"));
            break;
        }
      } else {
        throw new DocumentNotFoundException();
      }

      rs.close();
      ps.close();
    } catch (SQLException e) {
      throw new OperationFailedException();
    }
    return document;
  }

}