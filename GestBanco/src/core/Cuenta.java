/*
 *  Definición de la clase Cuenta
 *  En un banco tendremos una serie de clientes con sus cuentas bancarias
 */
package core;

import nu.xom.Element;
import nu.xom.ParsingException;

public abstract class Cuenta {

    public static final String TAG_CUENTA = "cuenta";
    public static final String TAG_NUMCUENTA = "numeroCuenta";
    public static final String TAG_FECHAAPERTURA = "fechaApertura";

    private String numCuenta;   // Numero de cuenta 
    private Fecha fechaApertura;   // Año de apertura de la cuenta

    /**
     * Crea una nueva cuenta del cliente, con sus datos: numero de cuenta, año
     * de apertura y tipo de cuenta (ahorro o corriente)
     *
     * @param numCuenta número de cuenta del cliente
     * @param fechaApertura la fecha de apertura de la cuenta del cliente
     */
    public Cuenta(String numCuenta, Fecha fechaApertura) {

        this.numCuenta = numCuenta;
        this.fechaApertura = fechaApertura;
    }

    public Cuenta(Element cuenta) throws ParsingException {
        if (cuenta.getFirstChildElement(TAG_NUMCUENTA) == null) {
            throw new ParsingException("falta el atributo numero de cuenta");
        }

        if (cuenta.getFirstChildElement(TAG_FECHAAPERTURA) == null) {
            throw new ParsingException("falta el atributo fecha de apertura");
        }

        this.numCuenta = cuenta.getFirstChildElement(TAG_NUMCUENTA).getValue();
        this.fechaApertura = new Fecha(cuenta.getFirstChildElement(TAG_FECHAAPERTURA));
    }

    public Element toDOM() {
        Element nCuenta = new Element(TAG_NUMCUENTA);
        nCuenta.appendChild(numCuenta);

        Element fechaAp = this.fechaApertura.toDOM();
        fechaAp.setLocalName(TAG_FECHAAPERTURA);

        Element cuenta = new Element(TAG_CUENTA);
        cuenta.appendChild(nCuenta);
        cuenta.appendChild(fechaAp);

        return cuenta;

    }

    /**
     * Devuelve el número de cuenta del cliente
     *
     * @return el numCuenta del cliente, como String.
     */
    public String getNumCuenta() {
        return numCuenta;
    }

    /**
     * Cambia el número de cuenta del cliente
     *
     * @param nCuenta el numCuenta del cliente
     */
    public void setNumCuenta(String nCuenta) {
        numCuenta = nCuenta;
    }

    /**
     * Devuelve el año de apertura de la cuenta del cliente
     *
     * @return El valor como entero
     *
     */
    public Fecha getFechaApertura() {
        return fechaApertura;
    }

    /**
     * Cambia el año de apertura de la cuenta del cliente
     *
     * @param ano El nuevo valor, como entero
     */
    public void setFechaApertura(Fecha ano) {
        this.fechaApertura = ano;
    }

    /**
     * Devuelve los datos de una cuenta
     *
     * @return los datos de una cuenta, como cadena
     */
    @Override
    public String toString() {
        StringBuilder toret = new StringBuilder();

        toret.append("Cuenta numero ").append(getNumCuenta());
        toret.append("; Fecha de apertura: ").append(getFechaApertura());

        return toret.toString();
    }

}
