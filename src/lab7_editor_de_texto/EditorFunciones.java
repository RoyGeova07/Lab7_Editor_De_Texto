/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab7_editor_de_texto;

import java.awt.Font;
import java.awt.Color;
import java.io.*;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;


/**
 *
 * @author royum
 */
public class EditorFunciones {
    
    private String contenidoTexto;
    private Font fuenteActual;
    private Color colorActual;

    public EditorFunciones() {
        contenidoTexto = "";
        fuenteActual = new Font("Arial", Font.PLAIN, 12);
        colorActual = Color.BLACK;
    }

   
    public void abrirArchivoConFormato(File archivo, JTextPane areaTexto) throws Exception {
        RTFEditorKit editorKit = new RTFEditorKit();
        StyledDocument doc = new DefaultStyledDocument();
        try (FileInputStream fis = new FileInputStream(archivo)) {
            editorKit.read(fis, doc, 0);
        }
        areaTexto.setStyledDocument(doc);
    }
  
    public void guardarArchivoConFormato(File archivo, JTextPane areaTexto) throws Exception {
        RTFEditorKit editorKit = new RTFEditorKit();
        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            editorKit.write(fos, areaTexto.getStyledDocument(), 0, areaTexto.getStyledDocument().getLength());
        }
    }

   
    public void actualizarFuente(String nombreFuente, int estilo, int tamaño) {
        fuenteActual = new Font(nombreFuente, estilo, tamaño);
    }

    public void actualizarColor(Color color) {
        colorActual = color;
    }

    // Getters
    public Font getFuenteActual() {
        return fuenteActual;
    }

    public Color getColorActual() {
        return colorActual;
    }
        

}
