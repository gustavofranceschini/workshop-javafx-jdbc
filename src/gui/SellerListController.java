package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService departmentService;

	@FXML
	private TableView<Seller> tableViewDepartmnt;

	@FXML
	private TableColumn<Seller, Integer> tableColumId;

	@FXML
	private TableColumn<Seller, String> tableColumName;
	
	@FXML
	private TableColumn<Seller, String> tableColumEmail;
	@FXML
	private TableColumn<Seller, Date> tableColumBirthDate;
	@FXML
	private TableColumn<Seller, Double> tableColumBaseSalary;

	@FXML
	TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
//		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	public void setSellerService(SellerService departmentService) {
		this.departmentService = departmentService;
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		InitializebleNodes();
	}

	private void InitializebleNodes() {
		tableColumId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumBirthDate, "dd/MM/yyyy");
		tableColumBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumBaseSalary, 2);


		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartmnt.prefHeightProperty().bind(stage.heightProperty());
		;

	}

	public void updateTableView() {
		if (departmentService == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartmnt.setItems(obsList);
//		initEditButtons();
		initRemoveButtons();
	}

	/*
	private void createDialogForm(Seller obj, String absolutName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.setSellerService(new SellerService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	*/

	@Override
	public void onDataChanged() {
		updateTableView();
	}

//	private void initEditButtons() {
//		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
//		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
//			private final Button button = new Button("edit");
//
//			@Override
//			protected void updateItem(Seller obj, boolean empty) {
//				super.updateItem(obj, empty);
//				if (obj == null) {
//					setGraphic(null);
//					return;
//				}
//				setGraphic(button);
//				button.setOnAction(
//						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
//			}
//		});
//	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
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

	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Area you sure to delete?");
		if (result.get() == ButtonType.OK) {
			if (departmentService == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				departmentService.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}