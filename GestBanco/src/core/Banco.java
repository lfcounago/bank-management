/*
 *  Definición de la clase Banco
 *  En un banco tendremos una serie de clientes
 */
package core;

import Exceptions.PosicionIncorrectaException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import nu.xom.*;

public class Banco {

    public static final String TAG_BANCO = "banco";
    public static final String TAG_NOMBREBANCO = "nombre";
    public static final String TAG_CLIENTES = "clientes";

    private final String nombre;
    private final ArrayList<Cliente> clientes; //Clientes del banco
    private int numClientes;
    // El atributo numClientes propociona información de:
    //  1.  numero de clientes existentes en el array clientes en cada momento
    //  2.  posición/indice del elemento del array donde se debería añadir un 
    //      nuevo cliente

    /**
     * Nuevo Banco con un num.max. de clientes.
     *
     * @param nombre
     * @param maxClientes el num. max. de clientes, como entero.
     */
    public Banco(String nombre, int maxClientes) {
        this.nombre = nombre;
        numClientes = 0;
        this.clientes = new ArrayList(maxClientes);
    }

    public Banco(String archivo) throws IOException, ParsingException {
        Builder parser = new Builder();
        Document doc = parser.build(new File(archivo));

        this.nombre = doc.getRootElement().getFirstChildElement(TAG_NOMBREBANCO).getValue();

        Elements clnts = doc.getRootElement().getFirstChildElement(TAG_CLIENTES).getChildElements();
        this.clientes = new ArrayList<>();
        for (int i = 0; i < clnts.size(); i++) {
            try {
                this.numClientes++;
                this.clientes.add(new Cliente(clnts.get(i)));
            } catch (ParsingException e) {
                System.err.println("\nERROR en el cliente " + (i + 1) + " " + e.getMessage());
            }

        }
    }

    public Element toDOM() {
        Element nomb = new Element(TAG_NOMBREBANCO);
        nomb.appendChild(nombre);

        Element clnts = new Element(TAG_CLIENTES);
        for (int i = 0; i < clientes.size(); i++) {
            clnts.appendChild(clientes.get(i).toDOM());
        }

        Element banco = new Element(TAG_BANCO);
        banco.appendChild(nomb);
        banco.appendChild(clnts);

        return banco;
    }

    public void toXML(String nombreArchivo) {
        Element banco = this.toDOM();
        Document doc = new Document(banco);

        try {
            FileOutputStream f = new FileOutputStream(nombreArchivo);
            Serializer serial = new Serializer(f);
            serial.write(doc);
            f.close();
        } catch (IOException exc) {
            System.err.println("ERROR de archivo: " + exc.getMessage());
        }
    }

    /**
     * Devuelve el cliente situado en pos
     *
     * @param pos el lugar del cliente en el vector de clientes
     * @return el objeto Cliente correspondiente.
     * @throws Exceptions.PosicionIncorrectaException
     */
    public Cliente get(int pos) throws PosicionIncorrectaException {
        if (pos >= getNumClientes()) {
            throw new PosicionIncorrectaException("get(): sobrepasa la pos: " + (pos + 1)
                    + " / " + getMaxClientes());
        }

        return clientes.get(pos);
    }

    /**
     * Devuelve el num. de clientes creados.
     *
     * @return el num. de clientes existentes, como entero.
     */
    public int getNumClientes() {
        return clientes.size();
    }

    /**
     * Devuelve el max. de numClientes
     *
     * @return el num. de clientes max., como entero
     */
    public int getMaxClientes() {
        return clientes.size();
    }

    /**
     * Devuelve los el nombre del Banco
     *
     * @return el nombre del banco, como String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Inserta un nuevo cliente
     *
     * @param c el nuevo objeto Cliente
     */
    public void inserta(Cliente c) {
        clientes.add(c);
        numClientes++;
    }

    /**
     * Elimina un cliente
     *
     * @param pos el lugar del cliente en el vector de clientes
     */
    public void elimina(int pos) {
        clientes.remove(pos);
        numClientes--;
    }

    /**
     * Devuelve los datos del Banco
     *
     * @return los datos del banco, como cadena
     */
    @Override
    public String toString() {
        StringBuilder toret = new StringBuilder();
        final int numClientes = getNumClientes();

        toret.append("Nombre del banco: ").append(getNombre()).append("\n");
        if (numClientes > 0) {
            for (int i = 0; i < numClientes; i++) {
                toret.append((i + 1)).append(". ");
                toret.append(clientes.get(i).toString()).append("\n");
            }
        } else {
            toret.append("No hay clientes.");
        }

        return toret.toString();
    }

    /**
     * Devuelve los datos de las cuentas del tipo especificado
     *
     * @param tipo
     * @return los datos del banco, como cadena
     * @throws Exceptions.PosicionIncorrectaException
     */
    public String listarCuentas(char tipo) throws PosicionIncorrectaException {
        StringBuilder toret = new StringBuilder();
        StringBuilder cliente;

        if (numClientes > 0) {
            for (int i = 0; i < numClientes; i++) {
                cliente = new StringBuilder();
                for (int j = 0; j < clientes.get(i).getNumCuentas(); j++) {
                    if ("Corriente".equals(tipo)) {
                        if (clientes.get(i).getCuenta(j) instanceof Corriente) {
                            cliente.append("\t");
                            cliente.append(clientes.get(i).getCuenta(j).toString());
                            cliente.append("\n");
                        }
                    }

                    if ("Ahorro".equals(tipo)) {
                        if (clientes.get(i).getCuenta(j) instanceof Ahorro) {
                            cliente.append("\t");
                            cliente.append(clientes.get(i).getCuenta(j).toString());
                            cliente.append("\n");
                        }
                    }
                }

                if (cliente.length() != 0) {
                    toret.append("El cliente ");
                    toret.append(clientes.get(i).getNombre());
                    toret.append(" tiene las siguientes cuentas del tipo ");
                    toret.append(tipo);
                    toret.append("\n");
                    toret.append(cliente);
                }
            }
        }
        return toret.toString();

    }
}
