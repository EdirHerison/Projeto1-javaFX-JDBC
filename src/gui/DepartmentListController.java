package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.servicies.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener{
	
	private DepartmentService service;

	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnNome;
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Department> obsList;
	
	@FXML
	public void onBtNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}



	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Erro lista não foi injetada");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}
	
	private void createDialogForm (Department obj, String absoluteName, Stage parentSatge) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DepartmentFormController controller = loader.getController();
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.subcribeDataChangerListener(this);
			controller.updateFormData();
			
			Stage dialoStage = new Stage();
			dialoStage.setTitle("Cadastro de novo Departamento");
			dialoStage.setScene(new Scene(pane));
			dialoStage.setResizable(false);
			dialoStage.initOwner(parentSatge);
			dialoStage.initModality(Modality.WINDOW_MODAL);
			dialoStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlerts("IO Excepition", "Erro loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
