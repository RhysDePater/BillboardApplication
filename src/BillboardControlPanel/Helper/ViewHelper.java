package BillboardControlPanel.Helper;

import org.w3c.dom.css.CSSStyleDeclaration;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Calendar.*;
import java.util.Date;
import java.util.GregorianCalendar;

public class ViewHelper {
    protected static final int DEFAULT_ROW_LENGTH = 5;
    protected static final int DEFAULT_COL_LENGTH = 30;
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 20);
    public static final Font TEXT_FONT = new Font("SansSerif", Font.BOLD, 12);
    public static final Font TINY_FONT = new Font("SansSerif", Font.BOLD, 8);


    public static Dimension getDimension(JFrame jFrame) {
        Dimension dimension = jFrame.getSize();
        return dimension;
    }


    public static JTable createJTable(String[][] tableContents, String[] colNames) {
        JTable jTable = new JTable(tableContents, colNames);
        jTable.getTableHeader().setReorderingAllowed(false);
        jTable.setDefaultEditor(Object.class, null);
        return jTable;
    }

    public static JButton createButton(String str) {
        JButton pnlBtn = new JButton(str);
        return pnlBtn;
    }

    public static JTextField createTextField() {
        JTextField inputText = new JTextField(DEFAULT_COL_LENGTH);
        return inputText;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField password = new JPasswordField(DEFAULT_COL_LENGTH);
        return password;
    }

    public static JTextArea createTextArea(int row, int col) {
        JTextArea textArea = new JTextArea(row, col);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return textArea;
    }

    public static JScrollPane createScrollPane(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        return scrollPane;
    }

    public static JLabel createLabel(String str, Font font) {
        JLabel label = new JLabel();
        label.setText(str);
        label.setFont(font);
        return label;
    }

    public static JPanel createPanel(Color c) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(c);
        return panel;
    }

    /**
     * Creates a textfiled which accepts data in a date time format
     * @return text field in date time format
     */
    public static JFormattedTextField createDateTimeInputField(){
        Date date = new Date();
        JFormattedTextField formatText;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        try{
            formatText = new JFormattedTextField(new MaskFormatter("####-##-## ##:##:##"));
            formatText.setColumns(20);
            formatText.setText(dateString);
        } catch (ParseException e){
            formatText = null;
        }
        return formatText;
    }



}
