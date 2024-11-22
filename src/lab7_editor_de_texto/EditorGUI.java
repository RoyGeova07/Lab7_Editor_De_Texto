/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_editor_de_texto;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.undo.*;//libreria para las acciones de rehacer y deshacer
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 *
 * @author royum
 */
public class EditorGUI extends JFrame {

    private JTextPane areaTexto;
    private JComboBox<String> fuentes;
    private JComboBox<Integer> tamaños;
    private JButton btnColor, btnGuardar, btnAbrir;
    private JButton btnNegrita, btnCursiva, btnSubrayado;
    private JButton btnDeshacer, btnRehacer;
    private EditorFunciones funciones;
    private UndoManager gestorDeshacer;

    public EditorGUI() {

        funciones = new EditorFunciones();
        gestorDeshacer = new UndoManager();
        setTitle("Editor de Texto Mejorado");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //aqui se configura el area de texto
        areaTexto = new JTextPane();
        areaTexto.getDocument().addUndoableEditListener(e -> {
            gestorDeshacer.addEdit(e.getEdit());
            actualizarEstadoBotones();
        });
        JScrollPane scroll = new JScrollPane(areaTexto);
        add(scroll, BorderLayout.CENTER);

        JPanel barraSuperior = crearBarraSuperior();
        JPanel barraEstilos = crearBarraEstilos();
        add(barraSuperior, BorderLayout.NORTH);
        add(barraEstilos, BorderLayout.WEST);

        // Mostrar la ventana
        setVisible(true);
    }

    private JPanel crearBarraSuperior() {

        JPanel barraSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Fuentes y tamaños
        fuentes = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        tamaños = new JComboBox<>(new Integer[]{12, 14, 16, 18, 20, 24, 28, 32, 36, 48});
        fuentes.addActionListener(e -> aplicarFormato());
        tamaños.addActionListener(e -> aplicarFormato());

        btnColor = new JButton("Color");
        btnGuardar = new JButton("Guardar");
        btnAbrir = new JButton("Abrir");
        btnColor.addActionListener(e -> seleccionarColor());
        btnGuardar.addActionListener(e -> guardarArchivo());
        btnAbrir.addActionListener(e -> abrirArchivo());

        //aqui se agrega los componentes
        barraSuperior.add(new JLabel("Fuente:"));
        barraSuperior.add(fuentes);
        barraSuperior.add(new JLabel("Tamaño:"));
        barraSuperior.add(tamaños);
        barraSuperior.add(btnColor);
        barraSuperior.add(btnGuardar);
        barraSuperior.add(btnAbrir);

        return barraSuperior;
    }

    private JPanel crearBarraEstilos() {

        JPanel barraEstilos = new JPanel();
        barraEstilos.setLayout(new BoxLayout(barraEstilos, BoxLayout.Y_AXIS));
        barraEstilos.setBorder(BorderFactory.createTitledBorder("Estilos"));

        btnNegrita = crearBotonEstilo("N", Font.BOLD, e -> alternarNegrita());
        btnCursiva = crearBotonEstilo("I", Font.ITALIC, e -> alternarCursiva());
        btnSubrayado = crearBotonEstilo("U", Font.PLAIN, e -> alternarSubrayado());

        //aqui botones de deshacer y rehacer
        btnDeshacer = new JButton("Deshacer");
        btnRehacer = new JButton("Rehacer");
        btnDeshacer.addActionListener(e -> deshacer());
        btnRehacer.addActionListener(e -> rehacer());
        btnDeshacer.setEnabled(false);
        btnRehacer.setEnabled(false);

        barraEstilos.add(btnNegrita);
        barraEstilos.add(Box.createVerticalStrut(5)); // Espaciado entre botones
        barraEstilos.add(btnCursiva);
        barraEstilos.add(Box.createVerticalStrut(5));
        barraEstilos.add(btnSubrayado);
        barraEstilos.add(Box.createVerticalStrut(10));
        barraEstilos.add(btnDeshacer);
        barraEstilos.add(Box.createVerticalStrut(5));
        barraEstilos.add(btnRehacer);

        return barraEstilos;
    }

    private JButton crearBotonEstilo(String texto, int estiloFuente, ActionListener listener) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", estiloFuente, 14));
        boton.addActionListener(listener);
        return boton;
    }

    //funcion para negrita
    private void alternarNegrita() {

        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();

        if (inicio == fin) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un texto para aplicar el formato.");
            return;
        }

        for (int i = inicio; i < fin; i++) {
            Element elemento = doc.getCharacterElement(i);
            AttributeSet atributosExistentes = elemento.getAttributes();

            // Crear un nuevo conjunto de atributos basado en los existentes
            SimpleAttributeSet nuevosAtributos = new SimpleAttributeSet(atributosExistentes);
            boolean esNegrita = StyleConstants.isBold(atributosExistentes);
            StyleConstants.setBold(nuevosAtributos, !esNegrita);

            // Aplicar los atributos actualizados
            doc.setCharacterAttributes(i, 1, nuevosAtributos, true);
        }
    }

    //funcion cursiva
    private void alternarCursiva() {

        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();

        if (inicio == fin) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un texto para aplicar el formato.");
            return;
        }

        for (int i = inicio; i < fin; i++) {
            Element elemento = doc.getCharacterElement(i);
            AttributeSet atributosExistentes = elemento.getAttributes();

            //aqui se crea un nuevo conjunto de atributos basado en los existentes
            SimpleAttributeSet nuevosAtributos = new SimpleAttributeSet(atributosExistentes);
            boolean esCursiva = StyleConstants.isItalic(atributosExistentes);
            StyleConstants.setItalic(nuevosAtributos, !esCursiva);

            //aqui se aplica los atributos actualizados
            doc.setCharacterAttributes(i, 1, nuevosAtributos, true);
        }
    }

// aqui funcion para el subrayado
    private void alternarSubrayado() {

        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();

        if (inicio == fin) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un texto para aplicar el formato.");
            return;
        }

        for (int i = inicio; i < fin; i++) {
            Element elemento = doc.getCharacterElement(i);
            AttributeSet atributosExistentes = elemento.getAttributes();

            SimpleAttributeSet nuevosAtributos = new SimpleAttributeSet(atributosExistentes);
            boolean esSubrayado = StyleConstants.isUnderline(atributosExistentes);
            StyleConstants.setUnderline(nuevosAtributos, !esSubrayado);

            doc.setCharacterAttributes(i, 1, nuevosAtributos, true);
        }
    }

    //funcion para deshacer
    private void deshacer() {
        if (gestorDeshacer.canUndo()) {
            gestorDeshacer.undo();
            actualizarEstadoBotones(); // funcion para actualizarlo
        }
    }

    private void rehacer() {
        if (gestorDeshacer.canRedo()) {
            gestorDeshacer.redo();
            actualizarEstadoBotones();
        }
    }

    private void actualizarEstadoBotones() {
        btnDeshacer.setEnabled(gestorDeshacer.canUndo());
        btnRehacer.setEnabled(gestorDeshacer.canRedo());
    }

    private void aplicarFormato() {
        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();

        if (inicio == fin) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un texto para aplicar el formato.");
            return;
        }

        SimpleAttributeSet estilo = new SimpleAttributeSet();

        String fuente = (String) fuentes.getSelectedItem();
        if (fuente != null) {
            StyleConstants.setFontFamily(estilo, fuente);
        }

        Integer tamaño = (Integer) tamaños.getSelectedItem();
        if (tamaño != null) {
            StyleConstants.setFontSize(estilo, tamaño);
        }

        doc.setCharacterAttributes(inicio, fin - inicio, estilo, false);
    }

    private void seleccionarColor() {
        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();

        if (inicio == fin) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un texto para aplicar el color.");
            return;
        }

        Color color = JColorChooser.showDialog(this, "Seleccionar Color", Color.BLACK);
        if (color != null) {
            SimpleAttributeSet estilo = new SimpleAttributeSet();
            StyleConstants.setForeground(estilo, color);

            doc.setCharacterAttributes(inicio, fin - inicio, estilo, false);
        }
    }

    private void guardarArchivo() {
        JFileChooser selector = new JFileChooser();
        int opcion = selector.showSaveDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = selector.getSelectedFile();
            try {
                funciones.guardarArchivoConFormato(archivo, areaTexto);
                JOptionPane.showMessageDialog(this, "Archivo guardado con exito.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage());
            }
        }
    }

    private void abrirArchivo() {
        JFileChooser selector = new JFileChooser();
        int opcion = selector.showOpenDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = selector.getSelectedFile();

            try {

                funciones.abrirArchivoConFormato(archivo, areaTexto);
                JOptionPane.showMessageDialog(this, "Archivo abierto con exito.");

                //aqui se reinicia el UndoManager despues de cargar un archivo
                gestorDeshacer.discardAllEdits(); //este sirve para limpiar historial de deshacer/rehacer
                actualizarEstadoBotones(); //este para deshabilitar botones

                //aqui se agrega un nuevo UndoableEditListener despues de abrir
                areaTexto.getDocument().addUndoableEditListener(e -> {
                    gestorDeshacer.addEdit(e.getEdit());
                    actualizarEstadoBotones();
                });

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new EditorGUI();
    }
}
