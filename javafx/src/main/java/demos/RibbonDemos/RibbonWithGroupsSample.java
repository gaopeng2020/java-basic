/*
 *  Copyright (c) 2016, 2018 Pixel Duke (Pedro Duque Vieira - www.pixelduke.com)
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *    * Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *    * Neither the name of Pixel Duke, any associated website, nor the
 *  names of its contributors may be used to endorse or promote products
 *  derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL PIXEL DUKE BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package demos.RibbonDemos;

import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class RibbonWithGroupsSample extends Application{

    @Override
    public void start(Stage primaryStage) {
        BorderPane rootNode = new BorderPane();

        Ribbon ribbon = new Ribbon();
        ribbon.setPrefHeight(150);
        rootNode.setTop(ribbon);

        RibbonTab ribbonTab = new RibbonTab("Test");
        RibbonGroup ribbonGroup = new RibbonGroup();
        ribbonTab.getRibbonGroups().add(ribbonGroup);

        Image image = new Image(RibbonWithGroupsSample.class.getResource("icons8_Bold_32px.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(30);
        Button iconButton = new Button("Bold", imageView);
        iconButton.setContentDisplay(ContentDisplay.TOP);

        ribbonGroup.getNodes().add(iconButton);

        image = new Image(RibbonWithGroupsSample.class.getResource("icons8_Italic_32px.png").toExternalForm());
        imageView = new ImageView(image);
        iconButton = new Button("Italic", imageView);
        iconButton.setContentDisplay(ContentDisplay.TOP);

        ribbonGroup.getNodes().add(iconButton);

        image = new Image(RibbonWithGroupsSample.class.getResource("icons8_Underline_32px.png").toExternalForm());
        imageView = new ImageView(image);
        iconButton = new Button("Underline", imageView);
        iconButton.setContentDisplay(ContentDisplay.TOP);
        iconButton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        ribbonGroup.getNodes().add(iconButton);


        ribbonGroup = new RibbonGroup();
        ribbonTab.getRibbonGroups().add(ribbonGroup);
        image = new Image(RibbonWithGroupsSample.class.getResource("icons8_Save_32px.png").toExternalForm());
        imageView = new ImageView(image);
        iconButton = new Button("Save Results", imageView);
        iconButton.setContentDisplay(ContentDisplay.TOP);
        ribbonGroup.getNodes().add(iconButton);

        iconButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount()==2) {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Resource File");
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Text Files", "*.xlsx"),
                            new FileChooser.ExtensionFilter("All Files", "*.*"));

                    File file = fileChooser.showOpenDialog(new Stage());
                    System.out.println("file.getName() = " + file.getName());
                }
            }
        });

        ribbon.getTabs().add(ribbonTab);

        Scene scene = new Scene(rootNode);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
