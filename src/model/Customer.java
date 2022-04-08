package model;

import java.util.ArrayList;

public class Customer extends Person{

    private String address;
    private int postalCode;
    private ArrayList<SaleOrder> saleOrders;


    public Customer(int id, String firstName, String lastName, String city, String address, int postalCode) {
        super(id, firstName, lastName, city);
        this.address = address;
        this.postalCode = postalCode;

    }

    public Customer(String firstName, String lastName, String city, String address, int postalCode) {
        super(firstName, lastName, city);
        this.address = address;
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public ArrayList<SaleOrder> getSaleOrders() {
        return saleOrders;
    }

    public void setSaleOrders(ArrayList<SaleOrder> saleOrders) {
        this.saleOrders = saleOrders;
    }

}
