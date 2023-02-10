/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import nu.xom.Element;
import nu.xom.ParsingException;

/**
 *
 * @author LuisFerCou
 */
public class Fecha {

    public static final String TAG_FECHA = "fecha";
    public static final String TAG_DIA = "dia";
    public static final String TAG_MES = "mes";
    public static final String TAG_ANHO = "año";

    private int dia;
    private int mes;
    private int anho;

    /**
     * Nueva Fecha
     *
     * @param dia como int
     * @param mes como int
     * @param anho como int
     */
    public Fecha(int dia, int mes, int anho) {
        this.dia = dia;
        this.mes = mes;
        this.anho = anho;
    }

    public Fecha(Element fecha) throws ParsingException {
        if (fecha.getFirstChildElement(TAG_ANHO) == null) {
            throw new ParsingException("falta el atributo año");
        }

        if (fecha.getFirstChildElement(TAG_MES) == null) {
            throw new ParsingException("falta el atributo mes");
        }

        if (fecha.getFirstChildElement(TAG_DIA) == null) {
            throw new ParsingException("falta el atributo dia");
        }

        this.dia = Integer.parseInt(fecha.getFirstChildElement(TAG_DIA).getValue());
        this.mes = Integer.parseInt(fecha.getFirstChildElement(TAG_MES).getValue());
        this.anho = Integer.parseInt(fecha.getFirstChildElement(TAG_ANHO).getValue());

    }

    public Element toDOM() {
        Element d = new Element(TAG_DIA);
        d.appendChild(Integer.toString(dia));

        Element m = new Element(TAG_MES);
        m.appendChild(Integer.toString(mes));

        Element a = new Element(TAG_ANHO);
        a.appendChild(Integer.toString(anho));

        Element fecha = new Element(TAG_FECHA);
        fecha.appendChild(d);
        fecha.appendChild(m);
        fecha.appendChild(a);

        return fecha;
    }

    /**
     * Devuelve el día
     *
     * @return el día, como int.
     */
    public int getDia() {
        return dia;
    }

    /**
     * Modifica el dia
     *
     * @param dia como int
     */
    public void setDia(int dia) {
        this.dia = dia;
    }

    /**
     * Devuelve el mes
     *
     * @return el mes, como int.
     */
    public int getMes() {
        return mes;
    }

    /**
     * Modifica el mes
     *
     * @param mes como int
     */
    public void setMes(int mes) {
        this.mes = mes;
    }

    /**
     * Devuelve el año
     *
     * @return el año, como int.
     */
    public int getAnho() {
        return anho;
    }

    /**
     * Modifica el año
     *
     * @param anho como int
     */
    public void setAnho(int anho) {
        this.anho = anho;
    }

    /**
     * Devuelve los datos de una cuenta ahorro
     *
     * @return los datos de la cuenta, como cadena
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append(getDia())
                .append("\n")
                .append(getMes())
                .append("\n")
                .append(getAnho());

        return sb.toString();
    }

}
