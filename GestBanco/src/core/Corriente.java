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
public class Corriente extends Cuenta {

    public static final String TAG_CUENTACORRIENTE = "cuentaCorriente";
    public static final String TAG_NUMEROTARJETA = "numeroTarjeta";
    public static final String TAG_FECHACADUCIDAD = "fechaCaducidad";

    private String numTarjeta;
    private Fecha fechaCaducidad;

    /**
     * Nueva cuenta Corriente
     *
     * @param numTarjeta de la cuenta
     * @param fechaCaducidad de la tarjeta
     * @param numCuenta de la clase Cuenta
     * @param fechaApertura de la clase Fecha
     */
    public Corriente(String numTarjeta, Fecha fechaCaducidad, String numCuenta, Fecha fechaApertura) {
        super(numCuenta, fechaApertura);
        this.numTarjeta = numTarjeta;
        this.fechaCaducidad = fechaCaducidad;
    }

    public Corriente(Element corriente) throws ParsingException {
        super(corriente);

        if (corriente.getFirstChildElement(TAG_NUMEROTARJETA) == null) {
            throw new ParsingException("falta el atributo numero de tarjeta");
        }

        if (corriente.getFirstChildElement(TAG_FECHACADUCIDAD) == null) {
            throw new ParsingException("falta el atributo fecha de caducidad");
        }

        this.numTarjeta = corriente.getFirstChildElement(TAG_NUMEROTARJETA).getValue();
        this.fechaCaducidad = new Fecha(corriente.getFirstChildElement(TAG_FECHACADUCIDAD));
    }

    @Override
    public Element toDOM() {
        Element cuenta = super.toDOM();
        cuenta.setLocalName(TAG_CUENTACORRIENTE);

        Element numTarj = new Element(TAG_NUMEROTARJETA);
        numTarj.appendChild(numTarjeta);

        Element fechCaduc = fechaCaducidad.toDOM();
        fechCaduc.setLocalName(TAG_FECHACADUCIDAD);

        cuenta.appendChild(numTarj);
        cuenta.appendChild(fechCaduc);

        return cuenta;
    }

    /**
     * Devuelve el número de la tarjeta
     *
     * @return el número de la tarjeta, como String.
     */
    public String getNumTarjeta() {
        return numTarjeta;
    }

    /**
     * Modifica el número de la tarjeta
     *
     * @param numTarjeta como String
     */
    public void setNumTarjeta(String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }

    /**
     * Devuelve la fecha de caducidad
     *
     * @return la fecha de caducidad, como Fecha.
     */
    public Fecha getFechaCaducidad() {
        return fechaCaducidad;
    }

    /**
     * Modifica la fecha de caducidad
     *
     * @param fechaCaducidad como Fecha
     */
    public void setFechaCaducidad(Fecha fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
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
                .append("CUENTA CORRIENTE. \t")
                .append(super.toString())
                .append("; Num. tarjeta: ")
                .append(getNumTarjeta())
                .append("; Fecha de caducidad")
                .append(getFechaCaducidad());

        return sb.toString();
    }

}
