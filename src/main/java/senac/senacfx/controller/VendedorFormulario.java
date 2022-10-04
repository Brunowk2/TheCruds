package senac.senacfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Constraints;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Clientes;
import senac.senacfx.model.entities.Department;
import senac.senacfx.model.entities.Seller;
import senac.senacfx.model.entities.Vendedor;
import senac.senacfx.model.exceptions.ValidationException;
import senac.senacfx.model.services.ClientesService;
import senac.senacfx.model.services.DepartmentService;
import senac.senacfx.model.services.SellerService;
import senac.senacfx.model.services.VendedorService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class VendedorFormulario implements Initializable {

    private Vendedor entity;

    private VendedorService service;

    private ClientesService departmentService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtComissao;

    @FXML
    private TextField txtEmail;

    @FXML
    private ComboBox<Clientes> comboBoxClientes;
    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorCpf;

    @FXML
    private Label labelErrorComissao;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList<Clientes> obsList;

    //Contolador agora tem uma instancia do departamento
    public void setSeller(Vendedor entity){
        this.entity = entity;
    }

    public void setServices(VendedorService, ClientesService clientesService){
        this.service = service;
        this.clientesService = clientesService;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        //validacao manual pois nao esta sendo usado framework para injetar dependencia
        if (entity == null){
            throw new IllegalStateException("Entidade nula");
        }
        if (service == null){
            throw new IllegalStateException("Servico nulo");
        }

        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e){
            Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), Alert.AlertType.ERROR);
        } catch (ValidationException e){
            setErrorMessages(e.getErrors());
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Vendedor getFormData() {
        Vendedor obj = new Vendedor();

        ValidationException exception = new ValidationException("Erro na validacao");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("name", "campo nao pode ser vazio");
        }
        obj.setName(txtName.getText());

        if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addError("email", "campo nao pode ser vazio");
        }
        obj.setEmail(txtEmail.getText());

        if (txtCpf.getValue() == null){
            exception.addError("cpf", "cpf incorreto");
        } else {
            Instant instant = Instant.from(txtCpf.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setCpf(String.valueOf(Date.from(instant)));
        }

        if (txtComissao.getText() == null || txtComissao.getText().trim().equals("")){
            exception.addError("Comissao", "campo nao pode ser vazio");
        }
        obj.setComissao(Utils.tryParseToDouble(txtComissao.getText()));

        obj.setClientes(comboBoxClientes.getValue());

        if (exception.getErrors().size() > 0){
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldDouble(txtCpf);
        Constraints.setTextFieldMaxLength(txtComissao, 60);
        Constraints.setTextFieldMaxLength(txtEmail, 70);

        initializeComboBoxClientes();

    }

    private void initializeComboBoxClientes() {
    }

    public void updateFormData(){

        if (entity == null){
            throw new IllegalStateException("Entidade nula");
        }

        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
        txtEmail.setText(entity.getEmail());

        Locale.setDefault(Locale.US);

        if (entity.getEmail() != null) {
            dpEmail.setValue(LocalDate.ofInstant(entity.getEmail().toInstant(), ZoneId.systemDefault()));
        }

        txtComissao.setText(String.format("%.2f", entity.getComissao()));

        if (entity.getClientes() == null) {
            comboBoxClientes.getSelectionModel().selectFirst();
        } else {
            comboBoxClientes.setValue(entity.getClientes());
        }

    }

    public void loadAssociatedObjects(){

        if (departmentService == null){
            throw new IllegalStateException("DepartmentService was null");
        }

        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxClientes.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
        labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
        labelErrorBirthDate.setText((fields.contains("birthDate") ? errors.get("birthDate") : ""));
        labelErrorBaseSalary.setText((fields.contains("baseSalary") ? errors.get("baseSalary") : ""));
        labelErrorName.getStyleClass().add("button");

    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }

}


}
