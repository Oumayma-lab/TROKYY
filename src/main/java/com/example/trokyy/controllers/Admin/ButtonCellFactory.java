package com.example.trokyy.controllers.Admin;

import com.example.trokyy.models.Utilisateur;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Callback;


public class ButtonCellFactory<S> implements Callback<TableColumn<S, String>, TableCell<S, String>> {

    @Override
    public TableCell<S, String> call(TableColumn<S, String> param) {
        return new TableCell<>() {
            final Button deleteButton = new Button();

            {
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> {
                    // Get the item associated with this row
                    S item = getTableView().getItems().get(getIndex());
                    if (item != null) {
                        ((UserManagementController) getTableView().getProperties().get("controller")).deleteUser((Utilisateur) item);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(deleteButton);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        };
    }
}