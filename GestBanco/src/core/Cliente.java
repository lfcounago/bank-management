/*
 * Definicion de la clase Cliente
 */
package core;

import Exceptions.NumMinCuentasException;
import Exceptions.PosicionIncorrectaException;
import java.util.ArrayList;
import nu.xom.*;

public class Cliente {

    public static final String TAG_DNI = "dni";
    public static final String TAG_NOMBRE = "nombre";
    public static final String TAG_DOMICILIO = "domicilio";
    public static final String TAG_CUENTAS = "cuentas";
    public static final String TAG_CLIENTE = "cliente";

    private String dni;             // D.N.I. del cliente
    private String nombre;          // Nombre del cliente
    private String domicilio;       // Domicilio del cliente 
    private ArrayList<Cuenta> cuentas; // Cuentas bancarias del cliente

    /**
     * Crea un nuevo cliente, con sus datos: nombre, domicilio, año y las
     * cuentas que tiene.
     *
     * @param dni D.N.I. del cliente
     * @param nombre nombre completo del cliente
     * @param domicilio el domicilio del cliente
     * @param cuentas array con las cuentas que tiene el cliente
     */
    public Cliente(String dni, String nombre, String domicilio,
            ArrayList<Cuenta> cuentas) {
        this.dni = dni;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.cuentas = cuentas;
    }

    public Cliente(Element cliente) throws ParsingException {
        if (cliente.getFirstChildElement(TAG_DNI) == null) {
            throw new ParsingException("falta el atributo dni");
        }

        if (cliente.getFirstChildElement(TAG_NOMBRE) == null) {
            throw new ParsingException("falta el atributo nombre");
        }

        if (cliente.getFirstChildElement(TAG_DOMICILIO) == null) {
            throw new ParsingException("falta el atributo domicilio");
        }

        if (cliente.getFirstChildElement(TAG_CUENTAS) == null) {
            throw new ParsingException("falta el atributo cuentas");
        }

        this.dni = cliente.getFirstChildElement(TAG_DNI).getValue();
        this.nombre = cliente.getFirstChildElement(TAG_NOMBRE).getValue();
        this.domicilio = cliente.getFirstChildElement(TAG_DOMICILIO).getValue();

        this.cuentas = new ArrayList<>();
        Elements elements = cliente.getFirstChildElement(TAG_CUENTAS).getChildElements();
        for (int i = 0; i < cliente.getFirstChildElement(TAG_CUENTAS).getChildCount(); i++) {
            if (elements.get(i).getLocalName().equals(Ahorro.TAG_CUENTAAHORRO)) {
                cuentas.add(new Ahorro(elements.get(i)));
            }

            if (elements.get(i).getLocalName().equals(Corriente.TAG_CUENTACORRIENTE)) {
                cuentas.add(new Corriente(elements.get(i)));
            }
        }
    }

    public Element toDOM() {
        Element dn = new Element(TAG_DNI);
        dn.appendChild(dni);

        Element nomb = new Element(TAG_NOMBRE);
        nomb.appendChild(nombre);

        Element domic = new Element(TAG_DOMICILIO);
        domic.appendChild(domicilio);

        Element cts = new Element(TAG_CUENTAS);
        for (int i = 0; i < cuentas.size(); i++) {
            cts.appendChild(cuentas.get(i).toDOM());
        }

        Element cliente = new Element(TAG_CLIENTE);
        cliente.appendChild(dn);
        cliente.appendChild(nomb);
        cliente.appendChild(domic);
        cliente.appendChild(cts);

        return cliente;
    }

    /**
     * Devuelve el D.N.I. del cliente
     *
     * @return el dni del cliente, como String.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Cambia el D.N.I. del cliente
     *
     * @param d el dni del cliente
     */
    public void setDni(String d) {
        dni = d;
    }

    /**
     * Devuelve el nombre del cliente
     *
     * @return El valor como cadena
     *
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia el nombre del cliente
     *
     * @param nombre El nuevo valor, como cadena
     */
    public void setNombre(String nombre) {
        this.nombre = nombre.trim();
    }

    /**
     * Devuelve el domicilio del cliente
     *
     * @return El valor como cadena
     *
     */
    public String getDomicilio() {
        return domicilio;
    }

    /**
     * Cambia el domicilio del cliente
     *
     * @param domicilio El nuevo valor, como cadena
     */
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio.trim();
    }

    /**
     * Añade una nueva cuenta al listado de cuentas del cliente
     *
     * @param cuenta la nueva Cuenta
     */
    public void nuevaCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    /**
     * Devuelve el número de cuentas del cliente
     *
     * @return el número de cuentas del cliente
     */
    public int getNumCuentas() {
        return cuentas.size();
    }

    /**
     * Devuelve el objeto de tipo Cuenta que se encuentra en la posición
     * especificada (empezando en 0)
     *
     * @param cuenta el índice de la cuenta (empezando en 0)
     * @return la Cuenta que se encuentra en la posición especificada
     * @throws Exceptions.PosicionIncorrectaException
     */
    public Cuenta getCuenta(int cuenta) throws PosicionIncorrectaException {
        if ((cuenta < 0) || (cuenta >= getNumCuentas())) {
            throw new PosicionIncorrectaException("La posicion indicada no es valida.");
        }

        return this.cuentas.get(cuenta);
    }

    /**
     * Elimina la Cuenta de la posición especificada (empezando en 0)
     *
     * @param pos el índice de la cuenta que se quiere eliminar (empezando en 0)
     * @throws Exceptions.NumMinCuentasException
     */
    public void eliminaCuenta(int pos) throws NumMinCuentasException {
        if (getNumCuentas() == 1) {
            throw new NumMinCuentasException("El cliente debe tener como minimo "
                    + "una cuenta");
        } else {
            cuentas.remove(pos);
        }
    }

    /**
     * Devuelve los datos del Cliente
     *
     * @return los datos del cliente, como cadena
     */
    @Override
    public String toString() {
        StringBuilder toret = new StringBuilder();

        toret.append("Cliente ").append(getNombre());
        toret.append(" con DNI ").append(getDni());
        toret.append(" y domicilio en ").append(getDomicilio());
        toret.append(". \n\n \tDatos de sus cuentas:  ");

        for (Cuenta cuenta : this.cuentas) {
            toret.append("\n\t\t").append(cuenta.toString());
        }

        return toret.toString();
    }
}
