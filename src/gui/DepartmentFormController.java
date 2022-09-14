package gui;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.servicies.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Department entity;
	private DepartmentService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtNome;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btCancelar;
	@FXML
	private Label lbId;
	@FXML
	private Label lbNome;
	
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void subcribeDataChangerListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entidade departamento não foi injetada");
		}
		if(service == null) {
			throw new IllegalStateException("Servico departamento não foi injetado");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListener();
			Utils.currentStage(event).close();
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
	private Department getFormData() {
		Department obj = new Department();
		obj.setId(Utils.tryParsetoInt(txtId.getText()));
		obj.setNome(txtNome.getText());
		return obj;
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
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entidade nao foi injetada");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
	}

}
