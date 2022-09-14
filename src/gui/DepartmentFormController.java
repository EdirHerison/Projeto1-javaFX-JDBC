package gui;


import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	private Department entity;
	
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
	
	
	@FXML
	public void onBtSalvarAction() {
		System.out.println("Salvar");
	}
	
	@FXML
	public void onBtCancelarAction() {
		System.out.println("Cancelar");
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
