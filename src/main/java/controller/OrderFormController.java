package controller;

import dto.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import service.ServiceFactory;
import service.custom.CustomerService;
import service.custom.ItemService;
import service.custom.OrderService;
import util.ServiceType;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {

    @FXML
    private ComboBox cmbCustomerId;

    @FXML
    private ComboBox cmbItemCode;

    @FXML
    private TableColumn colDescription;

    @FXML
    private TableColumn colItemCode;

    @FXML
    private TableColumn colQtyOnHand;

    @FXML
    private TableColumn colTotal;

    @FXML
    private TableColumn colUnitPrice;

    @FXML
    private Label lbkNetTotal;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTIme;

    @FXML
    private TableView tblCart;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtQty;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtUnitPrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        loadDateAndTime();
        loadCustomerIDs();
        loadItemCode();

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            setValuesToCustomerText((String) newVal);
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, oldVal, newVal) -> {
            setValuesToTextItem((String) newVal);
        });
    }

    CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    ItemService itemService = ServiceFactory.getInstance().getServiceType(ServiceType.ITEM);
    OrderService orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);

    List<CartTM> cartTMS = new ArrayList<>();
    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        String itemCode = cmbItemCode.getValue().toString();
        String description = txtDescription.getText();
        Integer qtyOnHand = Integer.parseInt(txtQty.getText());
        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Double total = qtyOnHand*unitPrice;

        cartTMS.add(new CartTM(itemCode,description,qtyOnHand,unitPrice,total));

        tblCart.setItems(FXCollections.observableArrayList(cartTMS));
        calNetTotal();


    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        String orderId = txtOrderId.getText();
        String date  = lblDate.getText();
        String customerId = cmbCustomerId.getValue().toString();

        List<OrderDetails> orderDetails = new ArrayList<>();

        cartTMS.forEach(cartTM -> {
            orderDetails.add(
                    new OrderDetails(orderId,cartTM.getItemCode(),cartTM.getQtyOnHand(),cartTM.getUnitPrice()));
        });

        Order order = new Order(orderId,date,customerId,orderDetails);
        System.out.println(order);
        orderService.placeOrder(order);
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(date);
        lblDate.setText(format1);

        System.out.println(LocalDate.now());

//        -------------------TIME--------------------

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    LocalTime now = LocalTime.now();
                    lblTIme.setText(now.getHour() + " : " + now.getMinute() + " : " + now.getSecond());
                }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }


    private void loadCustomerIDs() {
        try {
            List<String> customerIds = customerService.getCustomerIds();
            cmbCustomerId.setItems(FXCollections.observableArrayList(customerIds));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadItemCode() {

        try {
            List<String> allItemCodes = itemService.getAllItemCodes();
            cmbItemCode.setItems(FXCollections.observableArrayList(allItemCodes));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setValuesToCustomerText(String customerId) {
        try {
            Customer customer = customerService.searchById(customerId);
            txtCustomerName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setValuesToTextItem(String itemCode) {
        try {
            Item item = itemService.searchById(itemCode);
            txtDescription.setText(item.getDescription());
            txtStock.setText(item.getQty().toString());
            txtUnitPrice.setText(item.getUnitPrice().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void calNetTotal(){
        Double netTotal = 0.0;
        for (CartTM item : cartTMS ){
            netTotal+=item.getTotal();
        }

        lbkNetTotal.setText(netTotal.toString());

    }

}
