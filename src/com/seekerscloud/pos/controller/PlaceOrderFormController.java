package com.seekerscloud.pos.controller;

import com.seekerscloud.pos.db.Database;
import com.seekerscloud.pos.modal.Customer;
import com.seekerscloud.pos.modal.Item;
import com.seekerscloud.pos.view.tm.CartTm;
import com.seekerscloud.pos.view.tm.ItemTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaceOrderFormController {
    public TextField txtDate;
    public ComboBox<String> cmbCustomerIds;
    public ComboBox<String> cmbItemCodes;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtQty;
    public TableView<CartTm> tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colOption;
    public Label lblTotal;

    public void initialize(){

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setDateAndOrderId();
        loadAllCustomerIds();
        loadAllItemCodes();

        cmbCustomerIds.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if(newValue!=null){
                        setCustomerDetails();
                    }
        });

        cmbItemCodes.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if(newValue!=null){
                        setItemDetails();
                    }
                });

    }

    private void setItemDetails() {
        for (Item i:Database.itemTable
        ) {
            if (i.getCode().equals(cmbItemCodes.getValue())){
                txtDescription.setText(i.getDescription());
                txtUnitPrice.setText(String.valueOf(i.getUnitPrice()));
                txtQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
            }
        }
    }

    private void setCustomerDetails() {
        for (Customer c:Database.customerTable
             ) {
            if (c.getId().equals(cmbCustomerIds.getValue())){
                txtName.setText(c.getName());
                txtAddress.setText(c.getAddress());
                txtSalary.setText(String.valueOf(c.getSalary()));
            }
        }
    }

    private void loadAllItemCodes() {
        for(Item i : Database.itemTable){
            cmbItemCodes.getItems().add(i.getCode());
        }
    }

    private void loadAllCustomerIds() {
        for(Customer c: Database.customerTable){
            cmbCustomerIds.getItems().add(c.getId());
        }
    }

    private void setDateAndOrderId() {
        // set Date
        /*Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String d =df.format(date);
        txtDate.setText(d);*/
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    ObservableList<CartTm> obList = FXCollections.observableArrayList();
    public void addToCartOnAction(ActionEvent actionEvent) {
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qty=Integer.parseInt(txtQty.getText());
        double total = unitPrice*qty;
        Button btn = new Button("Delete");
        int row = isAlreadyExists(cmbItemCodes.getValue());
        if (row==-1){
            CartTm tm = new CartTm(cmbItemCodes.getValue(),txtDescription.getText(),unitPrice,qty,total,btn);
            obList.add(tm);
            tblCart.setItems(obList);
        }else{
            int tempQty=obList.get(row).getQty()+qty;
            double tempTotal = unitPrice* tempQty;
            obList.get(row).setQty(tempQty);
            obList.get(row).setTotal(tempTotal);
            tblCart.refresh();
        }

        calculateTotal();
        clearFields();
        cmbItemCodes.requestFocus();

    }

    private void clearFields() {
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtQty.clear();
    }

    private int isAlreadyExists(String code){
        for (int i = 0; i < obList.size(); i++) {
            if (obList.get(i).getCode().equals(code)){
                return i;
            }
        }
        return -1;
    }

    private void calculateTotal(){
        double total=0.00;
        for (CartTm tm: obList
             ) {
            total+=tm.getTotal();
        }
        lblTotal.setText(String.valueOf(total));
    }
}
