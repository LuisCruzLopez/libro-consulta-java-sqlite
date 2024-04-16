package bd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BaseDatosLibros extends BaseDatos {
	
	public final String CONSULTA_PREDETERMINADA = "SELECT libros.isbn AS ISBN, libros.nombreLibro AS 'Nombre del libro', autor.nombreAutor AS 'Nombre del autor',"
			+ "editorial.nombreEditorial AS Editorial, edicion AS Edición, tema.nombreTema AS Tema "
			+ "FROM libros inner join autor ON autor.idAutor = libros.idAutor "
			+ "inner join editorial ON editorial.idEditorial = libros.idEditorial inner join tema ON tema.idTema = libros.idTema";

	public BaseDatosLibros() {
		try {
			if (!verificarTabla()) {
				realizarConexion();
				crearBD();
				llenarBD();
				cerrarConexion();
			}
		} catch (BaseDatosException e1) {
		}
	}

	private void crearBD() throws BaseDatosException {

		realizarAccion("CREATE TABLE Autor(idAutor INTEGER PRIMARY KEY AUTOINCREMENT, nombreAutor TEXT NOT NULL)");
		realizarAccion(
				"CREATE TABLE Editorial(idEditorial INTEGER PRIMARY KEY AUTOINCREMENT, nombreEditorial TEXT NOT NULL)");
		realizarAccion("CREATE TABLE Tema(idTema INTEGER PRIMARY KEY AUTOINCREMENT, nombreTema TEXT NOT NULL)");
		realizarAccion(
				"CREATE TABLE Libros(isbn TEXT NOT NULL, nombreLibro TEXT NOT NULL, idAutor INTEGER, idEditorial INTEGER, edicion INTEGER, idTema INTEGER, "
						+ "FOREIGN KEY(idTema) REFERENCES Tema(idTema), FOREIGN KEY(idEditorial) REFERENCES Editorial(idEditorial), "
						+ "FOREIGN KEY(idAutor) REFERENCES Autor(idAutor))");
	}

	private void llenarBD() throws BaseDatosException {
		realizarAccion("INSERT INTO Autor VALUES(null,'Jonathan Swift')");
		realizarAccion("INSERT INTO Autor VALUES(null,'Albert Camus')");
		realizarAccion("INSERT INTO Autor VALUES(null,'Ray Bradbury')");
		realizarAccion("INSERT INTO Autor VALUES(null,'Ernesto Sabato')");
		realizarAccion("INSERT INTO Autor VALUES(null,'George Orwell')");

		realizarAccion("INSERT INTO Editorial VALUES(null,'Porrúa')");
		realizarAccion("INSERT INTO Editorial VALUES(null,'DeBolsillo')");
		realizarAccion("INSERT INTO Editorial VALUES(null,'Booket Minotauro')");
		realizarAccion("INSERT INTO Editorial VALUES(null,'Austral')");
		realizarAccion("INSERT INTO Editorial VALUES(null,'Lectorum')");

		realizarAccion("INSERT INTO Tema VALUES(null,'La Perspectiva y la Relatividad')");
		realizarAccion("INSERT INTO Tema VALUES(null,'Existencialismo y el absurdo')");
		realizarAccion("INSERT INTO Tema VALUES(null,'Guerra y el impulso autodestructivo del hombre')");
		realizarAccion("INSERT INTO Tema VALUES(null,'Existencialismo')");
		realizarAccion("INSERT INTO Tema VALUES(null,'Libertad')");

		realizarAccion("INSERT INTO Libros VALUES('9789700739632','Viajes de Gulliver',1,1,13,1)");
		realizarAccion("INSERT INTO Libros VALUES('9786073113052','La peste',2,2,1,2)");
		realizarAccion("INSERT INTO Libros VALUES('9789703707874','Crónicas marcianas',3,3,1,3)");
		realizarAccion("INSERT INTO Libros VALUES('9789703707874','La resistencia',4,4,1,4)");
		realizarAccion("INSERT INTO Libros VALUES('9786073116336','1984',5,5,1,5)");
	}

	public ArrayList<String> obtenerAutores() throws BaseDatosException {
		ArrayList<String> opciones = new ArrayList<String>();
		try {
			realizarConexion();
			ResultSet resultado = realizarConsulta("SELECT * FROM Autor");
			while (resultado.next()) {
				opciones.add(resultado.getString(2));
			}
			resultado.close();
			cerrarConexion();
		} catch (BaseDatosException | SQLException e3) {
			throw new BaseDatosException("No se pudo obtener las opciones.");
		}
		return opciones;
	}

	public ArrayList<String> obtenerEditoriales() throws BaseDatosException {
		ArrayList<String> opciones = new ArrayList<String>();
		try {
			realizarConexion();
			ResultSet resultado = realizarConsulta("SELECT * FROM Editorial");
			while (resultado.next()) {
				opciones.add(resultado.getString(2));
			}
			resultado.close();
			cerrarConexion();
		} catch (BaseDatosException | SQLException e3) {
			throw new BaseDatosException("No se pudo obtener las opciones.");
		}
		return opciones;
	}

	public ArrayList<String> obtenerTemas() throws BaseDatosException {
		ArrayList<String> opciones = new ArrayList<String>();
		try {
			realizarConexion();
			ResultSet resultado = realizarConsulta("SELECT * FROM Tema");
			while (resultado.next()) {
				opciones.add(resultado.getString(2));
			}
			resultado.close();
			cerrarConexion();
		} catch (BaseDatosException | SQLException e3) {
			throw new BaseDatosException("No se pudo obtener las opciones.");
		}
		return opciones;
	}

	private boolean verificarTabla() throws BaseDatosException {
		ArrayList<String> nombreTablas = obtenerTablas();
		int cont = 0;
		for (int i = 0; i < nombreTablas.size(); i++) {
			if (nombreTablas.get(i).equals("Autor")) {
				cont++;
			} else if (nombreTablas.get(i).equals("Editorial")) {
				cont++;
			} else if (nombreTablas.get(i).equals("Tema")) {
				cont++;
			} else if (nombreTablas.get(i).equals("Libros")) {
				cont++;
			}
		}
		if (cont == 4) {
			return true;
		} else {
			return false;
		}
	}

}
