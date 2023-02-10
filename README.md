# bank-management

El objetivo del programa consiste en gestionar las cuentas de los clientes de una entidad bancaria, permitiendo crear, modificar, eliminar y listar tanto clientes como cuentas bancarias. El menú principal de la aplicación es el siguiente:

Gestión de una entidad bancaria
  1. Inserta un nuevo cliente
  2. Modifica un cliente
  3. Elimina un cliente
  4. Listar clientes
  5. Listar cuentas por tipo
  0. Salir

En el caso de que se elija la opción de modificar se permite cambiar los datos de cliente y se presenta el submenú que permite gestionar las cuentas bancarias del cliente:

Menú de modificación de las cuentas:
  1. Inserta una nueva cuenta
  2. Modifica una cuenta existente
  3. Elimina una cuenta existente
  0. Terminar

La clase Banco es la principal de la lógica de negocio, guardando una colección de clientes.

La clase Cliente guarda toda la información de un cliente incluidas sus cuentas bancarias. 

La clase Cuenta guarda los datos de una cuenta bancaria. Esta clase se convierte en una superclase, mientras que las clases de las que realmente se crearán objetos son las subclases Ahorro y Corriente. 

La clase Fecha se utilizará tanto para especificar la fecha de apertura de una cuenta como la fecha de caducidad (validez) de una tarjeta de crédito. 

Por último, la clase Ilc se encarga de la interfaz-lógica-control del programa.
