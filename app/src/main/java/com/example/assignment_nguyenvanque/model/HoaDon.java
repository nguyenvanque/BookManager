package com.example.assignment_nguyenvanque.model;

public class HoaDon {
    private String maHoaDon;
    private String ngayMua;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, String ngayMua) {
        this.maHoaDon = maHoaDon;
        this.ngayMua = ngayMua;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(String ngayMua) {
        this.ngayMua = ngayMua;
    }
}

