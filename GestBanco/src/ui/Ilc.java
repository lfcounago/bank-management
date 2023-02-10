package ui;

import core.Ahorro;
import core.Banco;
import core.Cliente;
import core.Corriente;
import core.Cuenta;
import core.Fecha;
import Exceptions.NumMinCuentasException;
import Exceptions.PosicionIncorrectaException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Scanner;
import nu.xom.*;

/**
 * Interfaz de lin. de comando
 */
public class Ilc {

    private static final String ARCHIVO_GUARDADO = "clientes.xml";

    /**
     * Realiza el reparto de la funcionalidad ler = lee, evalua, repite
     */
    public void ler() {

        int op = 0;
        boolean bancoCreado = false;
        Banco coleccion = null;
        try {
            coleccion = new Banco("clientes.xml");
            bancoCreado = true;
        } catch (IOException e) {
            System.err.println("\nNo se ha encontrado el archivo. Se ha creado un banco vacio");
        } catch (ParsingException e) {
            System.err.println("\nNo se ha encontrado el archivo. Se ha creado un banco vacio");
        }
        if (!bancoCreado) {
            String nombre = leeCadena("Introduce el nombre del banco: ");

            // Lee el num. max. de clientes
            int numClientesEstimado = leeEntero("Introduce el numero estimado de clientes: ");

            // Prepara
            coleccion = new Banco(nombre, numClientesEstimado);
        }

        // Bucle ppal
        do {
            try {
                System.out.println("\n--------------------------------");
                System.out.println("\nGestión de una entidad bancaria.");

                op = menu(coleccion);

                switch (op) {
                    case 0:
                        System.out.println("Fin gestión banco.");
                    case 1:
                        insertaCliente(coleccion);
                    case 2:
                        modificaCliente(coleccion);
                    case 3:
                        eliminaCliente(coleccion);
                    case 4:
                        System.out.println(coleccion.toString());
                    case 5:
                        System.out.println(listarPorTipoCuenta(coleccion));
                    default:
                        System.out.println("No es correcta esa opción"
                                + " ( " + op + " )");
                }
            } catch (Exception exc) {
                System.err.println("Error. " + exc.getMessage());
            }

        } while (op != 0);

        coleccion.toXML("clientes.xml");

    }

    /**
     * Crea el Banco
     *
     * @return el nombre y el numero máximo de clientes del Banco
     */
    private static Banco crearBanco() {
        int maxClientes = leeEntero("Num. max. clientes: ");
        String nombre = leeCadena("Introduce el nombre del Banco:");

        return new Banco(nombre, maxClientes);
    }

    /**
     * Presenta un menu con las opciones, y permite seleccionar una.
     *
     * @return la opcion seleccionada, como entero
     */
    private int menu(Banco coleccion) {
        int toret;

        do {
            System.out.println("Numero de clientes: "
                    + coleccion.getNumClientes()
                    + " / " + coleccion.getMaxClientes());

            System.out.println(
                    "\n1. Inserta un nuevo cliente\n"
                    + "2. Modifica un cliente\n"
                    + "3. Elimina un cliente\n"
                    + "4. Listar clientes\n"
                    + "5. Listar cuentas por tipo\n"
                    + "0. Salir\n");
            toret = leeEntero("Selecciona: ");
        } while (toret < 0 && toret > 5);

        System.out.println();
        return toret;
    }

    /**
     * Crea un nuevo cliente y lo inserta en la coleccion
     *
     * @param coleccion La coleccion en la que se inserta el cliente.
     */
    private void insertaCliente(Banco coleccion) {
        System.out.println("\n------------");
        System.out.println("\nAlta cliente");

        Cliente c = leeCliente();
        coleccion.inserta(c);
    }

    /**
     * Borra un cliente por su posicion en la colección.
     *
     * @param coleccion La coleccion en la que se elimina el cliente
     */
    private void eliminaCliente(Banco coleccion) {
        System.out.println("\n------------");
        System.out.println("\nBaja cliente\n");

        if (coleccion.getNumClientes() > 0) {
            coleccion.elimina(leePosCliente(coleccion));
        } else {
            System.out.println("La coleccion no contiene clientes.");
        }
    }

    /**
     * Modifica un cliente existente.
     *
     * @param coleccion La coleccion de la cual modificar un cliente.
     */
    private void modificaCliente(Banco coleccion) throws Exception {
        System.out.println("\n--------------------");
        System.out.println("\nModificación cliente");

        if (coleccion.getNumClientes() > 0) {
            this.modificaCliente(coleccion.get(leePosCliente(coleccion)));
        } else {
            System.out.println("La coleccion no contiene clientes.");
        }
    }

    /**
     * Lee del teclado los datos de un cliente
     *
     * @return El objeto Cliente creado
     */
    private Cliente leeCliente() {
        System.out.println("\nIntroduce los datos del nuevo cliente:");

        String nombre = leeCadena("\tNombre: ");
        String dni = leeCadena("\tD.N.I.: ");
        String domicilio = leeCadena("\tDomicilio: ");

        int numCuentas = 0;
        do {
            numCuentas = leeEntero("\tNumero de cuentas: ");
            if (numCuentas < 1) {
                System.out.println("El número de cuentas no puede ser "
                        + "inferior a 1. Por favor, introdúdelo de nuevo.");
            }
        } while (numCuentas < 1);

        ArrayList<Cuenta> cuentas = leerCuentas(numCuentas);

        return new Cliente(dni, nombre, domicilio, cuentas);
    }

    /**
     * Lee los datos de las cuentas bancarias
     *
     * @param numCuentas numero de cuentas a leer
     * @return Array con las cuentas bancarias creadas
     */
    private ArrayList<Cuenta> leerCuentas(int numCuentas) {
        ArrayList<Cuenta> cuentas = new ArrayList(numCuentas);

        for (int i = 0; i < numCuentas; i++) {
            System.out.println("\n\tDatos de la cuenta número " + (i + 1));
            cuentas.add(leerCuenta());
        }

        return cuentas;
    }

    /**
     * Lee del teclado los datos de una cuenta bancaria
     *
     * @return El objeto Cuenta creado
     */
    private Cuenta leerCuenta() {
        String numCuenta = leeCadena("\tNumero de cuenta: ");
        Fecha fechaApertura = leerFecha("\tFecha de apertura: ");

        char tipo = leeTipoCuenta("Introduce el tipo de cuenta: ");

        Cuenta cuenta = null;

        switch (tipo) {
            case 'A':
                cuenta = leerCuentaAhorro(numCuenta, fechaApertura);
                break;
            case 'C':
                cuenta = leerCuentaCorriente(numCuenta, fechaApertura);
                break;
        }

        return cuenta;

    }

    /**
     * Lee del teclado los datos de la cuenta ahorro
     *
     * @return El objeto Ahorro creado
     */
    private Ahorro leerCuentaAhorro(String numCuenta, Fecha fechaApertura) {
        double interes = leeReal("\tInterés: ");

        return new Ahorro(interes, numCuenta, fechaApertura);
    }

    /**
     * Lee del teclado los datos de la cuenta corriente
     *
     * @return El objeto Corriente creado
     */
    private Corriente leerCuentaCorriente(String numCuenta, Fecha fechaApertura) {
        String numTarjeta = leeCadena("\tNumero de trajeta: ");
        Fecha fechaCaducidad = leerFecha("\tFecha de caducidad tarjeta: ");

        return new Corriente(numTarjeta, fechaCaducidad, numCuenta, fechaApertura);
    }

    /**
     * Modifica los datos de un cliente
     *
     * @param c Objeto Cliente a modificar
     */
    private void modificaCliente(Cliente c) throws NumMinCuentasException, PosicionIncorrectaException {

        System.out.println("\nModificando los datos del siguiente cliente:");
        System.out.println(c);
        System.out.println();

        modificaClienteGeneral(c);
        gestionCuentas(c);

    }

    /**
     * Modifica los datos de un cliente
     *
     * @param c Objeto Cliente a modificar
     */
    public void modificaClienteGeneral(Cliente c) {
        String nombre = leeCadena("\tNombre [" + c.getNombre() + "]: ", true);
        if (!nombre.isEmpty()) {
            c.setNombre(nombre);
        }

        String dni = leeCadena("\tDNI [" + c.getDni() + "]: ", true);
        if (!dni.isEmpty()) {
            c.setDni(dni);
        }

        String domicilio = leeCadena("\tDomicilio [" + c.getDomicilio() + "]: ",
                true);
        if (!domicilio.isEmpty()) {
            c.setDomicilio(domicilio);
        }
    }

    /**
     * Ejecuta el método preseleccionado en el menú
     *
     * @param c Objeto Cliente a modificar
     * @throws Exceptions.NumMinCuentasException
     * @throws Exceptions.PosicionIncorrectaException
     */
    public void gestionCuentas(Cliente c) throws NumMinCuentasException, PosicionIncorrectaException {
        int op = 0;
        int posCuenta;

        do {
            System.out.println("\nMenú de modificación de las cuentas:");

            op = menuEdicionCuentas();

            switch (op) {
                case 0:
                    System.out.println("Fin.");
                    break;
                case 1:
                    c.nuevaCuenta(leerCuenta());
                    break;
                case 2:
                    posCuenta = leePosCuenta(c.getNumCuentas());
                    modificaCuenta(c, posCuenta);
                    break;
                case 3:
                    c.eliminaCuenta(leePosCuenta(c.getNumCuentas()));
                    break;
                default:
                    System.out.println("No es correcta esa opción ( "
                            + op + " )");
            }
        } while (op != 0);
    }

    /**
     * Modifica los datos de una cuenta
     *
     * @param cuenta Objeto Cuenta a modificar
     */
    private void modificaCuenta(Cliente c, int posCuenta) throws PosicionIncorrectaException, NumMinCuentasException {
        Cuenta cuenta = c.getCuenta(posCuenta);

        double interes;
        Ahorro ahorro;

        Corriente corriente;
        String numTarjeta;
        Fecha fechaCaducidad;

        switch (leeTipoCuenta("Introduce el tipo de cuenta: ")) {
            case 'A':
                if (cuenta instanceof Corriente) {
                    interes = leeReal("\tIntroduce el interes");
                    ahorro = new Ahorro(interes, cuenta.getNumCuenta(),
                            cuenta.getFechaApertura());
                    modificaCuentaBasica(ahorro);
                    c.nuevaCuenta(ahorro);
                    c.eliminaCuenta(posCuenta);
                    break;
                } else {
                    modificaCuentaBasica(cuenta);
                    ahorro = (Ahorro) cuenta;
                    modificaCuentaAhorro(ahorro);
                }
                break;

            case 'C':
                if (cuenta instanceof Ahorro) {
                    numTarjeta = leeCadena("\tIntroduce numero tarjeta:");
                    fechaCaducidad = leerFecha("\tIntroduce la fecha de caducidad");
                    corriente = new Corriente(numTarjeta, fechaCaducidad, cuenta.getNumCuenta(),
                            cuenta.getFechaApertura());
                    modificaCuentaBasica(corriente);
                    c.nuevaCuenta(corriente);
                    c.eliminaCuenta(posCuenta);

                } else {
                    corriente = (Corriente) cuenta;
                    modificaCuentaBasica(corriente);
                    modificaCuentaCorriente(corriente);
                }
                break;
        }

    }

    /**
     * Modifica del teclado los datos de la cuenta ahorro
     *
     */
    private static void modificaCuentaAhorro(Ahorro a) {
        String temp;

        temp = leeCadena("\tIntroduce el interes:" + a.getInteres() + ":", true);
        if (!temp.isEmpty()) {
            a.setInteres(Double.parseDouble(temp));
        }
    }

    /**
     * Modifica del teclado los datos de la cuenta corriente
     *
     */
    private static void modificaCuentaCorriente(Corriente c) {
        String temp;

        temp = leeCadena("\tNumero tarjeta [" + c.getNumTarjeta() + "]: ", true);
        if (!temp.isEmpty()) {
            c.setNumTarjeta(temp);
        }

        c.setFechaCaducidad(modificarFecha("\tFecha de caducidad ["
                + c.getFechaCaducidad() + "]: ",
                c.getFechaCaducidad()));
    }

    /**
     * Modifica la cuenta
     *
     */
    private static void modificaCuentaBasica(Cuenta cuenta) {
        String numCuenta = leeCadena("\tNúmero de cuenta [Actual = "
                + cuenta.getNumCuenta() + "]", true);

        if (!numCuenta.isEmpty()) {
            cuenta.setNumCuenta(numCuenta);
        }

        cuenta.setFechaApertura(modificarFecha("\tFecha de apertura ["
                + cuenta.getFechaApertura() + "]: ", cuenta.getFechaApertura()));
    }

    /**
     * Modifica la fecha
     *
     */
    private static Fecha modificarFecha(String msg, Fecha fecha) {
        int dia;
        int mes;
        int anho;
        String temp;
        Calendar today = Calendar.getInstance();

        int diaComparar = 0;
        System.out.println(msg);

        do {
            temp = leeCadena("\t\tIntroduce anho [" + fecha.getAnho() + "]: ", true);
            if (temp.isEmpty()) {
                anho = fecha.getAnho();
            } else {
                anho = Integer.parseInt(temp);
            }
        } while (anho < 1 || anho > today.get(Calendar.YEAR));

        do {
            temp = leeCadena("\t\tIntroduce mes [" + fecha.getMes() + "]: ", true);
            if (temp.isEmpty()) {
                mes = fecha.getMes();
            } else {
                mes = Integer.parseInt(temp);
            }
        } while (mes < 1 || mes > 12);

        do {
            temp = leeCadena("\t\tIntroduce dia [" + fecha.getDia() + "]: ", true);
            if (temp.isEmpty()) {
                dia = fecha.getDia();
            } else {
                dia = Integer.parseInt(temp);
            }
            switch (mes) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    diaComparar = 31;
                    break;
                case 2:
                case 4:
                case 6:
                case 9:
                case 11:
                    diaComparar = 30;
                    break;
            }
        } while (dia < 1 || dia > diaComparar);

        return new Fecha(dia, mes, anho);
    }

    /**
     * Visualiza el menu de gestión de cuentas y obtiene la opción deseada
     *
     * @return int Opción del menú elegida
     */
    private int menuEdicionCuentas() {
        int toret;

        do {
            System.out.println(
                    "\n1. Inserta una nueva cuenta\n"
                    + "2. Modifica una cuenta existente\n"
                    + "3. Elimina una cuenta existente\n"
                    + "0. Terminar\n");
            toret = leeEntero("Selecciona: ");
        } while (toret < 0 && toret > 3);

        System.out.println();
        return toret;
    }

    /**
     * Lista las cuentas del banco por tipo
     *
     * @param coleccion El Banco del que se listan las cuentas.
     * @return coleccion.listarCuentas(tipo)
     * @throws Exceptions.PosicionIncorrectaException
     */
    public String listarPorTipoCuenta(Banco coleccion) throws PosicionIncorrectaException {
        char tipo = leeTipoCuenta("Indica el tipo de cuentas a listar: ");

        return coleccion.listarCuentas(tipo);
    }


    /* -------------------------------------------------------------*/
    //  METODOS PARA LA LECTURA DE DATOS DEL TECLADO
    /* -------------------------------------------------------------*/
    /**
     * Lee un caracter del teclado
     *
     * @return leeCadena
     */
    private static char leeCaracter() {
        return leeCadena("").toUpperCase().charAt(0);
    }

    /**
     * Lee una cadena del teclado
     *
     * @param mensaje Literal que especifica lo que el usuario debe introducir
     * @return String cadena leida del teclado
     */
    private static String leeCadena(String mensaje) {
        return leeCadena(mensaje, false);
    }

    /**
     * Lee una cadena del teclado
     *
     * @param mensaje Literal que especifica lo que el usuario debe introducir
     * @param permiteVacia True: campo no obligatorio; False: campo obligatorio
     * @return String cadena leida del teclado
     */
    private static String leeCadena(String mensaje, boolean permiteVacia) {
        String leer;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.print(mensaje);
            leer = scanner.nextLine().trim();
            if (!permiteVacia && leer.length() == 0) {
                System.out.println("La cadena introducida no puede estar vacía. "
                        + "Por favor, introdúcela de nuevo.");
            }
        } while ((permiteVacia == false) && leer.length() == 0);

        return leer;
    }

    /**
     * Lee un entero del teclado
     *
     * @param mensaje Literal que especifica lo que el usuario debe introducir
     * @return int Entero leido del teclado
     */
    private static int leeEntero(String mensaje) {
        Scanner scanner = new Scanner(System.in);
        boolean esValido = false; // True: entero leido correctamente
        int leer = 0;

        do {
            try {
                System.out.print(mensaje);
                leer = Integer.parseInt(scanner.nextLine().trim());
                esValido = true;
            } catch (NumberFormatException e) {
                System.err.println("La cadena introducida no se puede "
                        + "convertir a número entero. Por favor, "
                        + "introdúcela de nuevo.");
            }
        } while (!esValido);

        return leer;
    }

    /**
     * Lee un real del teclado
     *
     * @param mensaje Literal que especifica lo que el usuario debe introducir
     * @return double Reak leido del teclado
     */
    private static double leeReal(String mensaje) {
        Scanner scanner = new Scanner(System.in);
        boolean esValido = false; // True: entero leido correctamente
        double leer = 0;

        do {
            try {
                System.out.print(mensaje);
                leer = Double.parseDouble(scanner.nextLine().trim());
                esValido = true;
            } catch (NumberFormatException e) {
                System.err.println("La cadena introducida no se puede "
                        + "convertir a número real. Por favor, "
                        + "introdúcela de nuevo.");
            }
        } while (!esValido);

        return leer;
    }

    /**
     * Lee del teclado la posición de un cliente en la colección
     *
     * @param coleccion La colección de la que se obtiene el max.
     * @return la posición del cliente, como entero.
     */
    private int leePosCliente(Banco coleccion) {
        final int numClientes = coleccion.getNumClientes();
        int toret;

        do {
            toret = leeEntero("Introduzca posición del cliente (1..."
                    + numClientes + "): ");
        } while (toret < 1 || toret > numClientes);

        return toret - 1;
    }

    /**
     * Lee del teclado la fecha de apertura de una cuenta
     *
     * @param mensaje Literal que especifica lo que el usuario debe introducir
     * @return el objeto creado Fecha
     */
    private static Fecha leerFecha(String msg) {
        int dia;
        int mes;
        int anho;
        String temp;
        Calendar today = Calendar.getInstance();

        int diaComparar = 0;
        System.out.println(msg);

        do {
            anho = leeEntero("\t\tIntroduce anho: ");
        } while (anho < 1 || anho > today.get(Calendar.YEAR));

        do {
            mes = leeEntero("\t\tIntroduce mes: ");
        } while (mes < 1 || mes > 12);

        do {
            dia = leeEntero("\t\tIntroduce dia");
            switch (mes) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    diaComparar = 31;
                    break;
                case 2:
                case 4:
                case 6:
                case 9:
                case 11:
                    diaComparar = 30;
                    break;
            }
        } while (dia < 1 || dia > diaComparar);

        return new Fecha(dia, mes, anho);
    }

    /**
     * Lee del teclado el tipo de cuenta
     *
     * @param msg Literal que se muestra al usuario
     * @return enum Tipo de cuenta
     */
    private char leeTipoCuenta(String mensaje) {
        char opc;

        do {
            System.out.println(mensaje);
            System.out.println("\tTipo de cuenta (A: Ahorro, C: Corriente)");
            opc = leeCaracter();
        } while ((opc != 'A') && (opc != 'C'));

        return opc;
    }

    /**
     * Lee la posición de la cuenta a tratar
     *
     * @param numCuentas numero de cuentas existentes
     * @return int Posición/indice donde esta la cuenta a tratar
     */
    private int leePosCuenta(int numCuentas) {
        int toret;

        do {
            toret = leeEntero("Introduzca posición de la cuenta (1..."
                    + numCuentas + "): ");
        } while (toret < 1 || toret > numCuentas);

        return toret - 1;
    }
}
