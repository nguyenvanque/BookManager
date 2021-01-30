package com.example.assignment_nguyenvanque.model;

import java.io.Serializable;

public class TheLoai implements Serializable {
    private String maTheLoai;
    private String tenTheLoai;
    private String moTa;
    private int viTri;
    private String hinhAnh;

    public TheLoai() {
    }

    public TheLoai(String maTheLoai, String tenTheLoai, String moTa, int viTri, String hinhAnh) {
        this.maTheLoai = maTheLoai;
        this.tenTheLoai = tenTheLoai;
        this.moTa = moTa;
        this.viTri = viTri;
        this.hinhAnh = hinhAnh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMaTheLoai() {
        return maTheLoai;
    }

    public void setMaTheLoai(String maTheLoai) {
        this.maTheLoai = maTheLoai;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getViTri() {
        return viTri;
    }

    public void setViTri(int viTri) {
        this.viTri = viTri;
    }
    @Override
    public String toString() {
        return maTheLoai;
    }
}
