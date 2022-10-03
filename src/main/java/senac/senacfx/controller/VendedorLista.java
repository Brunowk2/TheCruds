package senac.senacfx.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import senac.senacfx.application.Main;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Seller;
import senac.senacfx.model.entities.Vendedor;
import senac.senacfx.model.services.DepartmentService;
import senac.senacfx.model.services.SellerService;
import senac.senacfx.model.services.VendedorService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VendedorLista implements Initializable, DataChangeListener {

    //ao inves de implementar um service = new SellerService(), ficaria acoplamento forte
    //e seria obrigado a instanciar a classe
    private VendedorService service;

    @FXML
    private TableView<Vendedor> tableViewVendedor;

    @FXML
    private TableColumn<Vendedor, Integer> tableColumnId;

    @FXML
    private TableColumn<Vendedor, String> tableColumnName;

    @FXML
    private TableColumn<Vendedor, String> tableColumnCpf;

    @FXML
    private TableColumn<Vendedor, Double> tableColumnComissao;

    @FXML
    private TableColumn<Vendedor, String> tableColumnEmail;

    @FXML
    private TableColumn<Vendedor, Vendedor> tableColumnEDIT;

    @FXML
    private TableColumn<Vendedor, Vendedor> tableColumnREMOVE;

    @FXML
    private Button btNew;

    private ObservableList<Vendedor> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Seller obj = new Vendedor();
        createDialogForm(obj,"/gui/VendedorForm.fxml", parentStage);
    }

    //feito isso usando um set, para injetar dependencia, boa pratica
    //injecao de dependendencia manual, sem framework pra isso
    public void setVendedorService(VendedorService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        tableColumnComissao.setCellValueFactory(new PropertyValueFactory<>("comissao"));
        Utils.formatTableColumnDouble(tableColumnComissao, 2);
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service is null!");
        }
        List<Vendedor> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewVendedor.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Vendedor obj, String absoluteName, Stage parentStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SellerFormController controller = loader.getController();
            controller.setVendedor(obj);
            controller.setServices(new VendedorService(), new ClientesService());
            controller.loadAssociatedObjects();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter seller data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
            private final Button button = new Button("Editar");
            @Override
            protected void updateItem(Vendedor obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/VendedorForm.fxml",Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
            private final Button button = new Button("Remover");

            @Override
            protected void updateItem(Vendedor obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Vendedor obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Confirma que quer deletar?");

        if (result.get() == ButtonType.OK){
            if (service == null){
                throw new IllegalStateException("Service estava null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbException e){
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

}

}
