package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import bd.BaseDatosException;
import bd.BaseDatosLibros;

public class Ventana extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JCheckBox checkBoxIsbn;
	private JCheckBox checkBoxNombre;
	private JCheckBox checkBoxAutor;
	private JCheckBox checkBoxEditorial;
	private JCheckBox checkBoxTema;
	private JTextField isbn;
	private JTextField nombre;
	private JComboBox<String> autores;
	private JComboBox<String> editoriales;
	private JComboBox<String> temas;
	private JButton consultar;
	private JTable tablaResultados;
	private BaseDatosLibros bd;
	private ResultSetTableModel modelo;

	public Ventana() {
		super("Consulta de libros");

		JPanel panelNorte = new JPanel();
		panelNorte.setLayout(new GridLayout(2, 3, 10, 20));

		checkBoxIsbn = new JCheckBox("ISBN");
		checkBoxIsbn.setBounds(10, 10, 150, 30);
		JPanel panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		isbn = new JTextField();
		isbn.setEditable(true);
		isbn.setPreferredSize(new Dimension(150, 20));
		panelAux.add(checkBoxIsbn);
		panelAux.add(isbn);
		panelNorte.add(panelAux);

		checkBoxNombre = new JCheckBox("Nombre");
		checkBoxNombre.setBounds(10, 10, 150, 30);
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		nombre = new JTextField();
		nombre.setEditable(true);
		nombre.setPreferredSize(new Dimension(150, 20));
		panelAux.add(checkBoxNombre);
		panelAux.add(nombre);
		panelNorte.add(panelAux);

		checkBoxAutor = new JCheckBox("Autor");
		checkBoxAutor.setBounds(10, 10, 150, 30);
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.RIGHT));
		autores = new JComboBox<String>();
		autores.setPreferredSize(new Dimension(150, 30));
		autores.addActionListener(this);
		panelAux.add(checkBoxAutor);
		panelAux.add(autores);
		panelNorte.add(panelAux);

		checkBoxEditorial = new JCheckBox("Editorial");
		checkBoxEditorial.setBounds(10, 10, 150, 30);
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.LEFT));
		editoriales = new JComboBox<String>();
		editoriales.setPreferredSize(new Dimension(150, 30));
		editoriales.addActionListener(this);
		panelAux.add(checkBoxEditorial);
		panelAux.add(editoriales);
		panelNorte.add(panelAux);

		checkBoxTema = new JCheckBox("Tema");
		checkBoxTema.setBounds(10, 10, 150, 30);
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		temas = new JComboBox<String>();
		temas.setPreferredSize(new Dimension(210, 30));
		temas.addActionListener(this);
		panelAux.add(checkBoxTema);
		panelAux.add(temas);
		panelNorte.add(panelAux);

		consultar = new JButton("Consultar");
		consultar.addActionListener(this);
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panelAux.add(consultar);
		panelNorte.add(panelAux);

		this.add(panelNorte, BorderLayout.NORTH);
		JPanel panelCentral = new JPanel();
		inicializarVentana();

		try {
			modelo = new ResultSetTableModel("org.sqlite.JDBC", "jdbc:sqlite:libros.db", bd.CONSULTA_PREDETERMINADA);
		} catch (ClassNotFoundException | SQLException e) {
			JOptionPane.showMessageDialog(this, "No se pudo realizar la consulta.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(EXIT_ON_CLOSE);
		}

		tablaResultados = new JTable(modelo);
		JScrollPane barraDesplazamiento = new JScrollPane(tablaResultados);
		Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
		barraDesplazamiento.setPreferredSize(new Dimension((pantalla.width * 4 / 5) - 30, 100));
		panelAux = new JPanel();
		panelAux.setLayout(new FlowLayout());
		panelAux.add(barraDesplazamiento);
		panelCentral.add(panelAux);
		this.add(panelCentral, BorderLayout.CENTER);

		setSize(pantalla.width * 4 / 5, pantalla.height * 3 / 4);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(consultar)) {
			realizarConsulta();
		}
	}

	private void inicializarCombo() throws BaseDatosException {

		autores.removeAllItems();
		editoriales.removeAllItems();
		temas.removeAllItems();
		ArrayList<String> listaAutores = bd.obtenerAutores();
		ArrayList<String> listaEditoriales = bd.obtenerEditoriales();
		ArrayList<String> listaTemas = bd.obtenerTemas();

		for (String b : listaAutores) {
			autores.addItem(b);
		}
		for (String b : listaEditoriales) {
			editoriales.addItem(b);
		}
		for (String b : listaTemas) {
			temas.addItem(b);
		}
	}

	private void inicializarVentana() {
		try {
			bd = new BaseDatosLibros();
			inicializarCombo();
		} catch (BaseDatosException e1) {
			JOptionPane.showMessageDialog(this, "No se pudo obtener los datos.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void realizarConsulta() {
		String consulta = bd.CONSULTA_PREDETERMINADA;
		if ((!checkBoxIsbn.isSelected() && !checkBoxNombre.isSelected() && !checkBoxAutor.isSelected()
				&& !checkBoxEditorial.isSelected() && !checkBoxTema.isSelected())) {
		} else {
			consulta += " WHERE ";
			if (checkBoxIsbn.isSelected()) {
				consulta += "isbn like '" + isbn.getText() + "%' AND ";
			}
			if (checkBoxNombre.isSelected()) {
				consulta += "nombreLibro LIKE '" + nombre.getText() + "%' AND ";
			}
			if (checkBoxAutor.isSelected()) {
				consulta += "nombreAutor = '" + autores.getSelectedItem() + "' AND ";
			}
			if (checkBoxEditorial.isSelected()) {
				consulta += "nombreEditorial = '" + editoriales.getSelectedItem() + "' AND ";
			}
			if (checkBoxTema.isSelected()) {
				consulta += "nombreTema = '" + temas.getSelectedItem() + "' AND ";
			}
			consulta = consulta.substring(0, consulta.length() - 4);
		}
		try {
			modelo.establecerConsulta(consulta);
		} catch (IllegalStateException | SQLException e) {
			JOptionPane.showMessageDialog(this, "No se pudo realizar la consulta.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(EXIT_ON_CLOSE);
		}
	}

	public static void main(String[] args) {
		new Ventana();
	}

}
