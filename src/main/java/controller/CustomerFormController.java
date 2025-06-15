package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable; // For initializing the table
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent; // For table row selection
import dto.Customer;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.impl.CustomerServiceImpl;
import util.CrudUtil;
import util.ServiceType;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colId;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, Double> colSalary;

    @FXML
    private TableView<Customer> tblCustomers;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSalary;

    CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        loadTable();

        tblCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void populateFields(Customer customer) {
        txtId.setText(customer.getId());
        txtName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        txtSalary.setText(String.valueOf(customer.getSalary()));
        txtId.setEditable(false);
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
        txtId.setEditable(true);
        tblCustomers.getSelectionModel().clearSelection();
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String salaryStr = txtSalary.getText();

        if (id.isEmpty() || name.isEmpty() || address.isEmpty() || salaryStr.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields.").show();
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid salary format. Please enter a number.").show();
            return;
        }

        // Check if customer ID already exists
        try {
            Customer customer = customerService.searchById(id);
            if (customer!=null) {
                new Alert(Alert.AlertType.ERROR, "Customer with ID " + id + " already exists.").show();
                return;
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error checking for existing customer: " + e.getMessage()).show();
            e.printStackTrace(); // Log the error
            return;
        }


        Customer newCustomer = new Customer(id, name, address, salary);

        try {
            Boolean b = customerService.addCustomer(newCustomer);

            if (b) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Added Successfully!").show();
                loadTable(); // Refresh table
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add customer.").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            e.printStackTrace(); // Log the error
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Customer selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            String idToDelete = txtId.getText();
            if (idToDelete.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select a customer from the table or enter an ID to delete.").show();
                return;
            }
            // Optional: Confirm deletion even if only ID is provided
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete customer with ID: " + idToDelete + "?");
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean isDeleted = CrudUtil.execute("DELETE FROM customer WHERE id=?", idToDelete);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully!").show();
                        loadTable();
                        clearFields();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Customer with ID " + idToDelete + " not found or failed to delete.").show();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
                    e.printStackTrace();
                }
            }

        } else {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedCustomer.getName() + "?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    boolean isDeleted = CrudUtil.execute("DELETE FROM customer WHERE id=?", selectedCustomer.getId());
                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully!").show();
                        loadTable();
                        clearFields();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete customer.").show();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();
        clearFields();
        new Alert(Alert.AlertType.INFORMATION, "Table Reloaded.").show();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String idToSearch = txtId.getText();
        if (idToSearch.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Customer ID to search.").show();
            return;
        }

        try {
            CustomerService customerService = new CustomerServiceImpl();

            Customer customer = customerService.searchById(idToSearch);

            populateFields(customer);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdatePnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String salaryStr = txtSalary.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a customer or search by ID to update.").show();
            return;
        }
        if (name.isEmpty() || address.isEmpty() || salaryStr.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields for update.").show();
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid salary format. Please enter a number.").show();
            return;
        }

        Customer updatedCustomer = new Customer(id, name, address, salary);

        try {
            boolean isUpdated = CrudUtil.execute("UPDATE customer SET name=?, address=?, salary=? WHERE id=?",
                    updatedCustomer.getName(), updatedCustomer.getAddress(), updatedCustomer.getSalary(), updatedCustomer.getId());

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Updated Successfully!").show();
                loadTable();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Customer with ID " + id + " not found or failed to update.").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }
    private void loadTable() {
        try {
            List<Customer> all = customerService.getAll();
            tblCustomers.setItems(FXCollections.observableArrayList(all));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer data: " + e.getMessage()).show();
            e.printStackTrace(); // Log the full stack trace for debugging
            // Consider throwing a RuntimeException if this is a critical failure
            // throw new RuntimeException("Database error loading customers", e);
        }
    }

    // Optional: Handle mouse click on table to populate fields
    @FXML
    void tblCustomersOnMouseClicked(MouseEvent event) {
        Customer selectedItem = tblCustomers.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            populateFields(selectedItem);
        }
    }
}