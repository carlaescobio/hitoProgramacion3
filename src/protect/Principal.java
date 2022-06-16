package protect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Principal {

	public static void main(String[] args) {
		Scanner lector = new Scanner(System.in);
		int respuesta=0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("No se ha encontrado el driver para MySQL");
			return;
		}
		
		System.out.println("Se ha cargado el Driver de MySQL");
		String cadenaConexion = "jdbc:mysql://localhost:3306/productos";
		String user = "root";
		String pass = ""; 
		Connection con;
		try {
			con = DriverManager.getConnection(cadenaConexion, user, pass);
		} catch (SQLException e) {
			System.out.println("Error en la conexión con la BD");
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("Se ha establecido la conexión con la Base de datos");
		
		do {
		
		System.out.println("Lista de productos");
		System.out.println(" ");
		System.out.println("Código de producto - Nombre - Fecha de Envasado - Unidades - Precio - Disponibilidad ");
		
		try {
			Statement sentencia = con.createStatement();
			ResultSet rs = sentencia.executeQuery("select * from producto ");
			while (rs.next()) {
				System.out.print(rs.getString("idProducto"));
				System.out.print(" - "); 
				System.out.print(rs.getString("nombre"));
				System.out.print(" - "); 
				System.out.print(rs.getString("fechaEnvasado"));
				System.out.print(" - "); 
				System.out.print(rs.getString("unidades"));
				System.out.print(" - "); 
				System.out.print(rs.getString("precio"));
				System.out.print(" - "); 
				System.out.print(rs.getString("disponible"));
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Error al realizar el listado de productos");
			System.out.println(e.getMessage());
		}
		

		System.out.println(" ");
		System.out.println("Selecciona una opción");
		System.out.println(" ");
		System.out.println("1. Añadir productos");
		System.out.println("2. Eliminar productos");
		System.out.println("3. Actualizar productos");
		System.out.println("5. Mostrar productos");
		System.out.println("5. Salir");
		System.out.println("----------------------");
		
		respuesta= lector.nextInt();
		
		switch(respuesta) {
		case 1:
			System.out.println(" ");
			System.out.println("Has seleccionado añadir un producto");
			System.out.println("Inserta el codigo del producto");
			String codigoProducto= lector.nextLine();
			System.out.println("Inserta el nombre del producto");
			String nombre= lector.nextLine();
			System.out.println("Inserta la fecha de envasado");
			String fecha= lector.nextLine();
			System.out.println("Inserta las unidades");
			String unidades= lector.nextLine();
			System.out.println("Inserta el precio");
			String precio= lector.nextLine();
			System.out.println("Inserta esu disponibilidad (0-1)");
			String disponibilidad= lector.nextLine();
				try {
					Statement sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					ResultSet rs = sentencia.executeQuery("select * from producto");
					rs.moveToInsertRow(); 
					rs.updateString("codigoProducto", codigoProducto); 
					rs.updateString("nombre", nombre); 
					rs.updateString("fecha", fecha); 
					rs.updateString("unidades", unidades); 
					rs.updateString("precio", precio); 
					rs.updateString("disponibilidad", disponibilidad); 
					rs.insertRow();
					
				} catch (SQLException e) {
					System.out.println("Se ha producido un error");
					System.out.println(e.getMessage());
				}
			break;
		case 2:
			System.out.println(" ");
			System.out.println("Has seleccionado eliminar un producto");
			System.out.println(" ");
			System.out.println("Inserta el id del producto");
			String idProducto=lector.nextLine();
			try {
				Statement sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = sentencia.executeQuery("select * from producto where codigoProducto='"+idProducto+"'");
				boolean existe = rs.next();
				if (existe) {
					rs.deleteRow();
				}
			
			} catch (SQLException e) {
				System.out.println("Se ha producido un error");
				System.out.println(e.getMessage());
			}
			break;
		case 3:
			System.out.println(" ");
			System.out.println("Has seleccionado modificar un producto");
			System.out.println(" ");
			System.out.println("Inserta el id del producto");
			idProducto=lector.nextLine();
			System.out.println(" ");
			System.out.println("Selecciona lo que quieres modificar");
			System.out.println("--------------------------");
			System.out.println("1. Unidades");
			System.out.println("2. Disponibilidad");
			System.out.println("--------------------------");
			int Modificar=lector.nextInt();
			if(Modificar==1) {
				System.out.println("Inserta el número nuevo de unidades");
				String nuevaU=lector.nextLine();
				try {
					Statement sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					ResultSet rs = sentencia.executeQuery("select * from producto where codigoProducto = '"+idProducto+"'");
					boolean existe = rs.next();
					if (existe) {
						rs.updateString("Unidades:", nuevaU);
						rs.updateRow();
					}
				} catch (SQLException e) {
					System.out.println("Se ha producido un error");
					System.out.println(e.getMessage());
				}
			}  else if(Modificar==2) {
				System.out.println("Inserta su disponibilidad (0-1)");
				Double Unidades=lector.nextDouble();
				try {
					Statement sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					ResultSet rs = sentencia.executeQuery("select * from producto where codigoProducto = '"+idProducto+"'");
					boolean existe = rs.next();
					if (existe) {
						rs.updateDouble("Disponibilidad", Unidades);
						rs.updateRow();
					}
				} catch (SQLException e) {
					System.out.println("Se ha producido un error");
					System.out.println(e.getMessage());
				}
			}
			
			break;
		case 5:
			
			break;
		default:
			System.out.println("Opción incorrecta. Inténtalo de nuevo");
		}
		
		} while (respuesta!=4);
		
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("No se ha podido cerrar la conexión con la BD");
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("Hasta Pronto!!");
	}
	
}