package edu.wpi.teamF.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class NewsController implements Initializable {
  public JFXTextField searchTerm;
  public JFXButton searchBtn;
  public JFXTextArea resultsBox;
  public AnchorPane frame;
  public ImageView backgroundImage;
  public VBox articlesPane;
  public String currUrl;

  public NewsApiClient newsApiClient;
  //  public ArticleResponse articles;
  public Boolean success;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    reset();
    setListeners();

    backgroundImage.setPreserveRatio(false);
    backgroundImage.fitHeightProperty().bind(frame.heightProperty());
    backgroundImage.fitWidthProperty().bind(frame.widthProperty());

    newsApiClient = new NewsApiClient("89cd565661a44c85bc4f2eda6cd4c90b");
  }

  public void reset() {
    searchTerm.setText("");
    searchBtn.setDisable(true);
    success = false;
    articlesPane.getChildren().clear();
    currUrl = "";
  }

  public void setListeners() {
    searchTerm
        .textProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 0) {
                  searchBtn.setDisable(false);
                } else {
                  searchBtn.setDisable(true);
                }
              }
            });
  }

  public void searchBtn(ActionEvent actionEvent) {
    articlesPane.getChildren().clear();
    newsApiClient.getEverything(
        new EverythingRequest.Builder()
            .q(searchTerm.getText())
            .language("en")
            .sortBy("relevance")
            .pageSize(10)
            .build(),
        new NewsApiClient.ArticlesResponseCallback() {
          public ArticleResponse articles;

          @Override
          public void onSuccess(ArticleResponse response) {
            System.out.println("Successfully got news");
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    setResponses(response);
                  }
                });
          }

          @Override
          public void onFailure(Throwable throwable) {
            System.out.println(throwable.getMessage());
            Platform.runLater(
                new Runnable() {
                  @Override
                  public void run() {
                    articlesFailed();
                  }
                });
          }
        });
  }

  public void setResponses(ArticleResponse articles) {
    success = true;
    for (Article a : articles.getArticles()) {
      System.out.println(a.getTitle());
      System.out.println(a.getSource().getName());
      System.out.println(a.getDescription());
      System.out.println(a.getUrl());
      articlesPane
          .getChildren()
          .add(
              createDisplayObject(
                  a.getTitle(), a.getSource().getName(), a.getDescription(), a.getUrl()));
    }
  }

  public StackPane createDisplayObject(
      String title, String source, String description, String url) {
    StackPane stackPane = new StackPane();
    stackPane.setPrefWidth(600);

    VBox vbox = new VBox();
    vbox.setPrefWidth(600);
    vbox.setMaxHeight(80);
    vbox.setPadding(new Insets(5, 10, 5, 10));
    vbox.setAlignment(Pos.CENTER_LEFT);
    vbox.setBorder(
        new Border(
            new BorderStroke(
                Color.web("blue"), // TODO Transparent
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(1))));
    vbox.setBackground(
        new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

    Label label1 = new Label();
    label1.setMinHeight(24);
    label1.setMaxWidth(570);
    label1.setFont(new Font("Arial", 16));
    label1.setStyle("-fx-font-weight: bold");
    label1.setText(title);
    vbox.getChildren().add(label1);

    Label label2 = new Label();
    label2.setMinHeight(16);
    label2.setMaxWidth(570);
    label2.setFont(new Font("Arial", 12));
    label2.setStyle("-fx-font-weight: bold");
    label2.setText(source);
    vbox.getChildren().add(label2);

    Label label3 = new Label();
    label3.setMinHeight(18);
    label3.setMaxHeight(18); // Cut of multiple lines
    label3.setMaxWidth(570);
    label3.setFont(new Font("Arial", 12));
    label3.setText("Select this story to read more...");
    if (description != null) {
      if (!description.contains("<")
          && !description.contains("{")
          && !description.contains("/")) { // Dealing with dumb HTML in the API response
        label3.setText(description);
      }
    }
    vbox.getChildren().add(label3);

    JFXButton btn = new JFXButton();
    btn.setPrefWidth(600);
    btn.setMinHeight(80);
    btn.setPrefHeight(vbox.getHeight());
    btn.setBorder( // TODO remove
        new Border(
            new BorderStroke(
                Color.web("rgba(255,0,0,0.9)"),
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(1))));
    btn.setOnAction(actionEvent1 -> onSelect(url, vbox));

    stackPane.getChildren().add(vbox);
    stackPane.getChildren().add(btn);

    return stackPane;
  }

  public void onSelect(String url, VBox vbox) {
    if (url.equals(currUrl)) {
      // Deselecting the current item
      vbox.setBackground(
          new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
      currUrl = "";
    } else {
      // Selecting the current item
      for (Node n : articlesPane.getChildren()) { // Setting all others to white
        StackPane s = (StackPane) n;
        VBox v = (VBox) s.getChildren().get(0);
        v.setBackground(
            new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
      }
      vbox.setBackground(
          new Background(
              new BackgroundFill(Color.web("#99d9ea"), CornerRadii.EMPTY, Insets.EMPTY)));
      currUrl = url;
    }
  }

  public void articlesFailed() {
    success = false;
  }

  public void resetBtn(ActionEvent actionEvent) {
    reset();
  }
}
