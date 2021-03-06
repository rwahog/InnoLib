package com.kitaphana.Service;

import com.kitaphana.Database.Database;
import com.kitaphana.dao.patronDAOImpl;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

  Database db = Database.getInstance();
  patronDAOImpl userDAO = new patronDAOImpl();
  DBService dbService = new DBService();
//    documentDAOImpl documentDAO = new documentDAOImpl();

  public TelegramBot() {
    ApiContextInitializer.init();
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
    try {
      db.connect();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String mes = update.getMessage().getText();
      long chatId = update.getMessage().getChatId();
      if (mes.equals("/start")) {
        sendMsg(chatId, "Enter your phone number in KitapHana library system to attach your account");
      } else if (mes.matches("[0-9]+")) {
        try {
          String user = dbService.findColumn(String.valueOf(chatId), "users", "id", "chat_id");
          if (user != "") {
            String chat = String.valueOf(0);
            dbService.updateColumn(user, chat, "users", "chat_id");
          }
          String user_in = dbService.findColumn(mes, "users", "id", "phone_number");
          dbService.updateColumn(user_in, String.valueOf(chatId), "users", "chat_id");
          String name = dbService.findColumn(user_in, "users", "name");
          sendMsg(chatId, "Dear " + name +
                  "\nYou have successfully attached your account. All important notifications will be sent there!");
        } catch (Exception e) {
          sendMsg(chatId, "Sorry we cannot find this account in KitapHana");
        }
      } else {
        sendMsg(chatId, "This bot is only for notification purposes!");
      }

      //sendMessage(update.getMessage().getChatId(), update.getMessage().getText());
//            System.out.println(update.getMessage().getChatId() + "\n" + update.getMessage().getText());
    }
  }

  @Override
  public String getBotUsername() {
    return "KitapHanaBot";
  }

  @Override
  public String getBotToken() {
    return "577066011:AAFK2TXevqQRFXkJjS_eAIEEaPH2MOcXJ_s";
  }

  @SuppressWarnings("deprecation")
  public void sendMsg(Long chatId, String text) {
    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
            .setChatId(chatId)
            .setText(text);
    try {
      sendMessage(message); // Call method to send the message
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}
