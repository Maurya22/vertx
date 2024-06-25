package com.globalpayex.entities;

public class Book {

    private int id;
    private String title;
    private  int pages;
    private double price;

    public Book(){

    }

    public Book(int id, String title, double price, int pages) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.pages = pages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pages=" + pages +
                ", price=" + price +
                '}';
    }
}
