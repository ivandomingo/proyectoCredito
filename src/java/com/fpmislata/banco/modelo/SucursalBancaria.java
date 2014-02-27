/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.banco.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alumno
 */
@JsonIgnoreProperties({ "handler" ,"hibernateLazyInitializer" })
public class SucursalBancaria implements Serializable{

    private int idSucursalBancaria;
    private EntidadBancaria entidadBancaria;
    private String codigoSucursal;
    private String nombreSucursal;
    private List<CuentaBancaria> ListaCuentaBancaria = new ArrayList();

    public SucursalBancaria() {
    }

    public int getIdSucursalBancaria() {
        return idSucursalBancaria;
    }

    public void setIdSucursalBancaria(int idSucursalBancaria) {
        this.idSucursalBancaria = idSucursalBancaria;
    }

    public EntidadBancaria getEntidadBancaria() {
        return entidadBancaria;
    }

    public void setEntidadBancaria(EntidadBancaria entidadBancaria) {
        this.entidadBancaria = entidadBancaria;
    }

    public String getCodigoSucursal() {
        return codigoSucursal;
    }

    public void setCodigoSucursal(String codigoSucursal) {
        this.codigoSucursal = codigoSucursal;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public List<CuentaBancaria> getListaCuentaBancaria() {
        return ListaCuentaBancaria;
    }

    public void setListaCuentaBancaria(List<CuentaBancaria> ListaCuentaBancaria) {
        this.ListaCuentaBancaria = ListaCuentaBancaria;
    }
}
