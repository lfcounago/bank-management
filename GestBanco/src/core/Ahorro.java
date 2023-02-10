/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import nu.xom.*;

/**
 *
 * @author LuisFerCou
 */
public class Ahorro extends Cuenta {

    public static final String TAG_CUENTAAHORRO = "cuentaAhorro";
    public static final String TAG_INTERES = "interes";

    private double interes;

    /**
     * Nueva cuenta Ahorro con un interés
     *
     * @param interes como double
     * @param numCuenta de la clase Cuenta
     * @param fechaApertura de la clase Fecha
     */
    public Ahorro(double interes, String numCuenta, Fecha fechaApertura) {
        super(numCuenta, fechaApertura);
        this.interes = interes;
    }

    public Ahorro(Element ahorro) throws ParsingException {
        super(ahorro);

        if (ahorro.getFirstChildElement(TAG_INTERES) == null) {
            throw new ParsingException("falta el atributo interes");
        }

        this.interes = Double.parseDouble(ahorro.getFirstChildElement(TAG_INTERES).getValue());
    }

    @Override
    public Element toDOM() {
        Element cuenta = super.toDOM();
        cuenta.setLocalName(TAG_CUENTAAHORRO);

        Element inter = new Element(TAG_INTERES);
        inter.appendChild(Double.toString(interes));

        cuenta.appendChild(inter);

        return cuenta;
    }

    /**
     * Devuelve el interés de la cuenta
     *
     * @return el interés de la cuenta, como double.
     */
    public double getInteres() {
        return interes;
    }

    /**
     * Modifica el interés de la cuenta
     *
     * @param interes como double
     */
    public void setInteres(double interes) {
        this.interes = interes;
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
                .append("CUENTA AHORRO. \t")
                .append(super.toString())
                .append("; Interés: ")
                .append(getInteres());

        return sb.toString();
    }

}
