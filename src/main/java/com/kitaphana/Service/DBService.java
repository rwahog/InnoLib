package com.kitaphana.Service;

import com.kitaphana.Database.Database;
import com.kitaphana.Entities.*;
import com.kitaphana.dao.authorDAOImpl;
import com.kitaphana.dao.keywordDAOImpl;
import com.kitaphana.exceptions.OperationFailedException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DBService {

  private Database db = Database.getInstance();
  private authorDAOImpl authorDAO = new authorDAOImpl();
  private keywordDAOImpl keywordDAO = new keywordDAOImpl();

  public Document findDocumentAndTypeInfo(String docId, String table) {
    String statement = String.format("SELECT * FROM documents INNER JOIN %s ON documents.id = %s.document_id WHERE %s.document_id=?", table, table, table);
    Document document = new Document();
    try {
      PreparedStatement ps = db.con.prepareStatement(statement);
      ps.setLong(1, Long.parseLong(docId));

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        switch (table) {
          case "books":
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthorsId(rs.getString("authors"));
            book.setCover(rs.getString("document_cover"));
            book.setAmount(rs.getInt("amount"));
            book.setPrice(rs.getInt("price"));
            book.setType(rs.getString("type"));
            book.setBestseller(rs.getInt("best_seller"));
            book.setEditionNumber(rs.getInt("edition_number"));
            book.setPublisher(rs.getString("publisher"));
            book.setYear(rs.getInt("year"));
            book.setDescription(rs.getString("description"));
            book.setKeywordsId(rs.getString("keywords"));
            setAuthors(book);
            setKeywords(book);
            document = book;
            break;
          case "ja":
            JournalArticle journalArticle = new JournalArticle();
            journalArticle.setId(rs.getInt("id"));
            journalArticle.setTitle(rs.getString("title"));
            journalArticle.setAuthorsId(rs.getString("authors"));
            journalArticle.setKeywordsId(rs.getString("keywords"));
            journalArticle.setCover(rs.getString("document_cover"));
            journalArticle.setAmount(rs.getInt("amount"));
            journalArticle.setPrice(rs.getInt("price"));
            journalArticle.setType(rs.getString("type"));
            journalArticle.setJournalName(rs.getString("journal_name"));
            journalArticle.setEditors(rs.getString("editors"));
            journalArticle.setTitle(rs.getString("title"));
            journalArticle.setDate(rs.getString("date"));
            setAuthors(journalArticle);
            setKeywords(journalArticle);
            document = journalArticle;
            break;
          case "av":
            AVMaterial avMaterial = new AVMaterial();
            avMaterial.setId(rs.getInt("id"));
            avMaterial.setTitle(rs.getString("title"));
            avMaterial.setAuthorsId(rs.getString("authors"));
            avMaterial.setKeywordsId(rs.getString("keywords"));
            avMaterial.setCover(rs.getString("document_cover"));
            avMaterial.setAmount(rs.getInt("amount"));
            avMaterial.setPrice(rs.getInt("price"));
            avMaterial.setType(rs.getString("type"));
            setAuthors(avMaterial);
            setKeywords(avMaterial);
            document = avMaterial;
            break;
        }
      }

      rs.close();
      ps.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return document;
  }

  public void setAuthors(Document document) {
    ArrayList<String> authorsId = fromDBStringToArray(document.getAuthorsId());
    ArrayList<Author> authors = new ArrayList<>();
    for (String authorId : authorsId) {
      Author author = authorDAO.findById(Long.parseLong(authorId));
      authors.add(author);
    }
    document.setAuthors(authors);
  }

  public void setKeywords(Document document) {
    ArrayList<String> keywordsId = fromDBStringToArray(document.getKeywordsId());
    ArrayList<Keyword> keywords = new ArrayList<>();
    for (String keyId : keywordsId) {
      Keyword keyword = keywordDAO.findById(Long.parseLong(keyId));
      keywords.add(keyword);
    }
    document.setKeywords(keywords);
  }

  public void updateColumn(String user_id, String info, String table, String column) {
    String statement = String.format("UPDATE %s SET %s=? WHERE id=?", table, column);
    try {
      PreparedStatement ps = db.con.prepareStatement(statement);
      ps.setString(1, info);
      ps.setLong(2, Long.parseLong(user_id));

      ps.executeUpdate();
      ps.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<String> findAllPhones() {
    ArrayList<String> phones = new ArrayList<>();
    final String query = "SELECT users.phone_number FROM users";
    try {
      PreparedStatement ps = db.connect().prepareStatement(query);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        phones.add(rs.getString("phone_number"));
      }
    } catch (SQLException e) {
      throw new OperationFailedException();
    }
    return phones;
  }

  public String findColumn(String id, String table, String column) {
    String statement = String.format("SELECT %s FROM %s WHERE id=? AND %s != ''", column, table, column);
    return findColumnFromTable(id, statement, column);
  }

  public String findColumn(String id, String table, String column, String criteria) {
    String statement = String.format("SELECT %s FROM %s WHERE %s=? AND %s != ''", column, table, criteria, column);
    return findColumnFromTable(id, statement, column);
  }

  private String findColumnFromTable(String id, String statement, String column) {
    String info = "";
    try {
      PreparedStatement ps = db.con.prepareStatement(statement);
      ps.setLong(1, Long.parseLong(id));

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        info = rs.getString(column);
      }

      rs.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return info;
  }

  public boolean checkUnique(String title, String authors, String type) {
    final String CHECK = "SELECT authors FROM documents WHERE title=? AND type=?";
    boolean unique = false;
    try {
      PreparedStatement ps = db.con.prepareStatement(CHECK);
      ps.setString(1, title);
      ps.setString(2, type);

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ArrayList AuthorsDB = fromDBStringToArray(rs.getString("authors"));
        ArrayList AuthorsIN = fromDBStringToArray(authors);
        Collections.sort(AuthorsDB);
        Collections.sort(AuthorsIN);
        if (AuthorsDB.equals(AuthorsIN)) {
          return false;
        }
      }
      unique = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return unique;
  }

  public ArrayList<String> fromDBStringToArray(String sample) {
    ArrayList<String> arrayList = null;
    if (sample != null && sample.length() != 0) {
      arrayList = new ArrayList<>(Arrays.asList(sample.split(",")));
    }
    return arrayList;
  }

  public String fromArrayToDBString(ArrayList<String> sample) {
    String string = "";
    if (sample != null && sample.size() != 0) {
      int n = sample.size();
      if (n == 1) {
        string = sample.get(0);
      } else {
        for (int i = 0; i < n - 1; i++) {
          string = string.concat(sample.get(i) + ",");
        }
        string = string.concat(sample.get(n - 1));
      }
    }
    return string;
  }

  public ArrayList<String> getLibrariansChatId() {
    ArrayList<String> librariansId = new ArrayList<>();
    final String query = "SELECT chat_id FROM users WHERE type='Librarian' AND chat_id != 0";
    try {
      PreparedStatement ps = db.con.prepareStatement(query);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        long chatId = rs.getLong("chat_id");
        librariansId.add(String.valueOf(chatId));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return librariansId;
  }

  public void sendMessageToLibrarians(String message) {
    ArrayList<String> librariansId = getLibrariansChatId();
    for (String librarianId : librariansId) {
      try {
        sendMsg(Long.parseLong(librarianId), message);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void sendMessageToUser(String message, String userId) {
    try {
      sendMsg(Long.parseLong(userId), message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendMsg(long chatId, String message) throws Exception {
    String postURL = "https://api.telegram.org/bot577066011:AAFK2TXevqQRFXkJjS_eAIEEaPH2MOcXJ_s/sendMessage";

    HttpPost post = new HttpPost(postURL);

    HttpClient client = new DefaultHttpClient();
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("chat_id", Long.toString(chatId)));
    params.add(new BasicNameValuePair("text", message));

    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, "UTF-8");
    post.setEntity(ent);
    try {
      HttpResponse responsePOST = client.execute(post);
    }catch (Exception e){}
  }
}
