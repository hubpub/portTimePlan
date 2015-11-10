/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vaadin.tutorial.addressbook.backend;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Jinliang.Xue
 */
public class PortTime implements Serializable, Cloneable {

    private Long id;

    private String port;
    private Date eta;
    private Date etd;
    private Date ata;
    private Date atd;
    private Date cut_off;

    public String getPort() {
        return port;
    }

    public void setPort(String Port) {
        this.port = Port;
    }

    public Date getEta() {
        return eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getEtd() {
        return etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public Date getAta() {
        return ata;
    }

    public void setAta(Date ata) {
        this.ata = ata;
    }

    public Date getAtd() {
        return atd;
    }

    public void setAtd(Date atd) {
        this.atd = atd;
    }

    public Date getCut_off() {
        return cut_off;
    }

    public void setCut_off(Date cut_off) {
        this.cut_off = cut_off;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public PortTime clone() throws CloneNotSupportedException {
        try {
            return (PortTime) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "Port: " + port + "with times: eta/etd/ata/atd/cut off: " + eta + etd + ata + atd + cut_off;
    }

}
