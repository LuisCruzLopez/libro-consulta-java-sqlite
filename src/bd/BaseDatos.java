package bd;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BaseDatos {
	protected Connection conexion;
	private String controlador;
	private String url;

	public BaseDatos() {
		controlador = "org.sqlite.JDBC";
		url = "jdbc:sqlite:" + "libros.db";
	}

	protected void realizarConexion() throws BaseDatosException {
		try {
			Class.forName(controlador);
			conexion = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e1) {
			throw new BaseDatosException("El driver no ha sido encontrado.");
		} catch (SQLException e2) {
			throw new BaseDatosException("No se pudo conectar a la base de datos.");
		}
	}

	protected void cerrarConexion() throws BaseDatosException {
		try {
			if (conexion != null) {
				conexion.close();
			}
		} catch (SQLException e3) {
			throw new BaseDatosException("No se pudo desconectar de la base de datos.");
		}

	}

	protected ResultSet realizarConsulta(String consulta) throws BaseDatosException {
		try {
			Statement instruccion = conexion.createStatement();
			ResultSet resultado = instruccion.executeQuery(consulta);
			return resultado;
		} catch (SQLException e4) {
			throw new BaseDatosException("No se pudo realizar la consulta.");
		}

	}

	protected int realizarAccion(String instruccion) throws BaseDatosException {
		try {
			Statement instruccion2 = conexion.createStatement();
			int valor = instruccion2.executeUpdate(instruccion);
			instruccion2.close();
			return valor;
		} catch (SQLException e5) {
			throw new BaseDatosException("No se pudo ejecutar la instrucción.");
		}
	}

	protected ArrayList<String> obtenerTablas() throws BaseDatosException {
		ArrayList<String> nombreTablas = new ArrayList<>();
		try {
			realizarConexion();
			String[] tipoTabla = { "TABLE" };
			ResultSet tablas = conexion.getMetaData().getTables(null, null, null, tipoTabla);
			while (tablas.next()) {
				nombreTablas.add(tablas.getString(3));
			}

		} catch (SQLException e5) {
			throw new BaseDatosException("No se pudo consultar la base de datos.");
		} finally {
			cerrarConexion();
		}

		return nombreTablas;
	}
}
