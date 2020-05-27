package BillboardViewer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.xpath.*;
import java.awt.*;

public class helper {
    public static JLabel createLabel(String str, Font font) {
        JLabel label = new JLabel();
        label.setText(str);
        label.setFont(font);
        return label;
    }

    public static JTextArea JMultilineLabel(String text, Font font, int Height, int Width){

        JTextArea textArea = new JTextArea();
        textArea.setText(text);
        textArea.setSize(Height,Width);
        textArea.setEditable(false);
        textArea.setCursor(null);
        textArea.setOpaque(false);
        textArea.setFocusable(false);
        textArea.setFont(font);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

//        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
//        textArea.setAlignmentY(JLabel.CENTER_ALIGNMENT);
        return textArea;
    }

    public static Node xmlNode(String path, Document document) throws XPathExpressionException {

            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression expression = xPath.compile(path);
            Node node = (Node) expression.evaluate(document, XPathConstants.NODE);
            return node;

    }

    public static void setMessageFont(JLabel label, Frame component)
    {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        System.out.println(stringWidth);
        int componentWidth = component.getWidth();
        System.out.println(componentWidth);

// Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;
        System.out.println(widthRatio);

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        System.out.println(newFontSize);
        int componentHeight = component.getHeight();
        System.out.println(componentHeight);

// Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);
        System.out.println(fontSizeToUse);

// Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.BOLD, newFontSize));
    }

    public static void setInformationFont(JLabel message, JTextArea info)
    {
        Font messageFont = message.getFont();
        Font infoFont = info.getFont();
        int infoFontSize = 0;

        infoFontSize = messageFont.getSize() / 2;

        info.setFont(new Font(infoFont.getName(), Font.BOLD, infoFontSize));
    }
}
