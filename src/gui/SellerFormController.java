package gui;


import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidateExceptions;
import model.servicies.DepartmentService;
import model.servicies.SellerService;

public class SellerFormController implements Initializable {
	
	private Seller entity;
	private SellerService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	private DepartmentService depService;
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtEamil;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtSalary;
	@FXML
	private ComboBox<Department> cbDepartment;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	@FXML
	private Label lbId;
	@FXML
	private Label lbNome;
	@FXML
	private Label lbEmail;
	@FXML
	private Label lbBirthDate;
	@FXML
	private Label lbSalary;
	
	private ObservableList<Department> obsList;
	
	
	public void setSeller(Seller entity) {
		this.entity = entity;
	}
	public void setService(SellerService service, DepartmentService depService) {
		this.service = service;
		this.depService = depService;
	}
	
	public void subcribeDataChangerListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entidade Vendedor não foi injetada");
		}
		if(service == null) {
			throw new IllegalStateException("Servico Vendedor não foi injetado");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListener();
			Utils.currentStage(event).close();
		}
		catch(ValidateExceptions e) {
			setErroMessages(e.getErros());
		}
		catch(DbException e) {
			Alerts.showAlerts("Erro ao Salvar no banco", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListener() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
		
	}
	private Seller getFormData() {
		Seller obj = new Seller();
		ValidateExceptions exception = new ValidateExceptions("Erro de Validacao");
		obj.setId(Utils.tryParsetoInt(txtId.getText()));
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addErro("Name", "Campo Nome nao pode ser vazio");
		}
		obj.setName(txtNome.getText());
		
		if(txtEamil.getText() == null || txtEamil.getText().trim().equals("")) {
			exception.addErro("email", "Campo Email nao pode ser vazio");
		} 
		obj.setEmail(txtEamil.getText());
		
		if(dpBirthDate.getValue() == null) {
			exception.addErro("birthDate", "Campo Data de Nascimento nao pode ser vazio");
		}else {
		Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setBirthDate(Date.from(instant));
		}
		
		if(txtSalary.getText() == null || txtSalary.getText().trim().equals("")) {
			exception.addErro("baseSalary", "Campo Salario nao pode ser vazio");
		}
		obj.setBaseSalary(Utils.tryParsetoDouble(txtSalary.getText()));
		
		obj.setDepartment(cbDepartment.getValue());
		
		if(exception.getErros().size() > 0) {
			throw exception;
		}
		
		return obj;
	}
	
	@FXML
	public void onComboBoxDepAction() {
		
	}
	
	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rs) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldMaxLength(txtEamil, 100);
		Constraints.setTextFieldDouble(txtSalary);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		
	    InitializeComboBoxDepartment();
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entidade nao foi injetada");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getName());
		txtEamil.setText(entity.getEmail());
		if(entity.getBirthDate() != null) {
		dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		Locale.setDefault(Locale.US);
		txtSalary.setText(String.format("%.2f",entity.getBaseSalary()));
		if(entity.getDepartment() == null) {
			cbDepartment.getSelectionModel().selectFirst();
		}else {
		cbDepartment.setValue(entity.getDepartment());
		}
	}
	
	public void loadAssociatedObjects() {
		if(depService == null) {
			throw new IllegalStateException("Departamento não foi injetado");
		}
		List<Department> list = depService.findAll();
		obsList = FXCollections.observableArrayList(list);
		cbDepartment.setItems(obsList);
	}
	
	private void setErroMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		
		lbNome.setText((fields.contains("Name") ? erros.get("Name") : ""));
		lbEmail.setText((fields.contains("email") ? erros.get("email") : ""));
		lbBirthDate.setText((fields.contains("birthDate") ? erros.get("birthDate") : ""));
		lbSalary.setText((fields.contains("baseSalary") ? erros.get("baseSalary") : ""));
		
	}
	
	private void InitializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
		@Override
		protected void updateItem(Department item, boolean empty) {
			super.updateItem(item, empty);
			setText(empty ? "" : item.getNome());
		}
		};
		cbDepartment.setCellFactory(factory);
		cbDepartment.setButtonCell(factory.call(null));
	}

}
