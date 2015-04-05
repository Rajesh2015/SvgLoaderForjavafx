package com.sataurday.Fun.svgLoader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class ScaleTest extends Application {

  private Pane editPane;
  private StackPane graphics;
  private Node content;
  private ImageView imageView;

  @Override
  public void start(final Stage stage) throws Exception {
    stage.setTitle(getClass().getSimpleName());

    editPane = new Pane();
    graphics = new StackPane();

    editPane.getChildren().add(graphics);

    StackPane root = new StackPane();
    root.getChildren().add(editPane);

    Scene scene = new Scene(root, 500, 500);
    stage.setScene(scene);
    stage.show();

    content = loadContent();
    graphics.getChildren().add(content);

    editPane.widthProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
          Number newValue) {
        adjustTransform();
      }
    });

    editPane.heightProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue,
          Number newValue) {
        adjustTransform();
      }
    });

    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        adjustTransform();
      }
    });
  }

  private Node loadContent() {
    MyTranscoder transcoder = new MyTranscoder(getClass().getResourceAsStream("image/digram.svg"));
    Image javafxCompatibleImage = transcoder.getJavafxCompatibleImage(700, 500);

    imageView = new ImageView(javafxCompatibleImage);
    imageView.setImage(javafxCompatibleImage);
    Group content = new Group();
    content.getChildren().add(imageView);
    return content;
  }

  private void adjustTransform() {
    graphics.getTransforms().clear();

    double cx = content.getBoundsInParent().getMinX();
    double cy = content.getBoundsInParent().getMinY();
    double cw = content.getBoundsInParent().getWidth();
    double ch = content.getBoundsInParent().getHeight();

    double ew = editPane.getWidth();
    double eh = editPane.getHeight();

    if (ew > 0.0 && eh > 0.0) {
      double scale = Math.min(ew / cw, eh / ch);

      // Offset to center content
      double sx = 0.5 * (ew - cw * scale);
      double sy = 0.5 * (eh - ch * scale);

      graphics.getTransforms().add(new Translate(sx, sy));
      graphics.getTransforms().add(new Translate(-cx, -cy));
      graphics.getTransforms().add(new Scale(scale, scale, cx, cy));
    }
  }

  public static void main(String[] args) {
    launch(args);
  }

}