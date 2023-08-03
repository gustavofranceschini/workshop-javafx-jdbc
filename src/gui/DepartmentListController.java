package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	private DepartmentService departmentService;

	@FXML
	private TableView<Department> tableViewDepartmnt;

	@FXML
	private TableColumn<Department, Integer> tableColumId;

	@FXML
	private TableColumn<Department, String> tableColumName;

	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;

	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		InitializebleNodes();
	}

	private void InitializebleNodes() {
		tableColumId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartmnt.prefHeightProperty().bind(stage.heightProperty());;
		
	}
	
	public void updateTableView() {
		if (departmentService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartmnt.setItems(obsList);
	}

}