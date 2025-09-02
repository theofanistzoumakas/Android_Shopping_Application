package com.example.final_assignment_app1;

public class App1Products {
// app1's object class for products
    public Integer App1ProductCode;
    public String App1ProductDescription;
    public String App1ProductImage;
    public Double App1ProductPrice;
    public Integer App1ProductReleaseDate;
    public String App1ProductStore;
    public String App1ProductTitle;


    public App1Products(){}

    public App1Products(Integer App1ProductCode, String App1ProductDescription, String App1ProductImage, Double App1ProductPrice, Integer App1ProductReleaseDate,String App1ProductStore, String App1ProductTitle){
        this.App1ProductCode = App1ProductCode;
        this.App1ProductDescription = App1ProductDescription;
        this.App1ProductImage = App1ProductImage;
        this.App1ProductPrice = App1ProductPrice;
        this.App1ProductReleaseDate = App1ProductReleaseDate;
        this.App1ProductStore = App1ProductStore;
        this.App1ProductTitle = App1ProductTitle;

    }

    public String getTitle(){
        return App1ProductTitle;
    }

    public String getCode(){
        return App1ProductCode.toString();
    }

    public Double getPrice(){
        return App1ProductPrice;
    }
}
