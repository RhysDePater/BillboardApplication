package BillboardViewer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.xpath.*;
import java.awt.*;

public class helper {
    public static JLabel createLabel(String str, Font font) {
        JLabel label = new JLabel();
        label.setText(str);
        label.setFont(font);
        label.setOpaque(false);
        return label;
    }

    public static JTextPane JMultilineLabel(String text, Font font) {

        JTextPane textArea = new JTextPane();
        textArea.setText(text);
        textArea.setFont(font);

        textArea.setEditable(false);
        textArea.setCursor(null);
        textArea.setOpaque(false);
        textArea.setFocusable(false);

        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

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
        int componentWidth = component.getWidth();


// Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = component.getHeight();

// Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

// Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.BOLD, newFontSize));
    }

    public static void setInformationFont(JLabel message, JTextPane info)
    {
        Font messageFont = message.getFont();
        Font infoFont = info.getFont();
        int infoFontSize = 0;

        infoFontSize = messageFont.getSize() / 2;

        info.setFont(new Font(infoFont.getName(), Font.BOLD, infoFontSize));
    }

    public static int[] getImgDimensions(int height, int width, double scale, Frame frame)
    {
        int newHeight;
        int newWidth;

        if(height >= width)
        {
            double ratio = (double)width/(double)height;
            newHeight = (int) ((double) frame.getHeight() * scale);
            newWidth = (int) ((double) newHeight * ratio);
        }
        else
        {
            double ratio = (double)height/(double)width;
            newWidth = (int) ((double) frame.getWidth() * scale);
            newHeight = (int) ((double) newWidth * ratio);
        }
        return (new int[]{newHeight, newWidth});
    }
}


