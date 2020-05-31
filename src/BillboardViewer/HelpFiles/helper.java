package BillboardViewer.HelpFiles;

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
    //function for making a basic label with a provided string and font
    public static JLabel createLabel(String str, Font font) {
        JLabel label = new JLabel();
        label.setText(str);
        label.setFont(font);
        label.setOpaque(false);
        return label;
    }

    //function for making the info section , with a provided text and font
    public static JTextPane JMultilineLabel(String text, Font font) {
        //create the text pane with a text and font
        JTextPane textArea = new JTextPane();
        textArea.setText(text);
        textArea.setFont(font);

        //set some settings
        textArea.setEditable(false);
        textArea.setCursor(null);
        textArea.setOpaque(false);
        textArea.setFocusable(false);

        //make the text centered
        StyledDocument doc = textArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        return textArea;
    }

    //small helper function for the xml parser
    public static Node xmlNode(String path, Document document) throws XPathExpressionException {
        //simple helper function for when reading the xml document, this is boiler plate code that is used each time when extracting
        //data from the file
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expression = xPath.compile(path);
        Node node = (Node) expression.evaluate(document, XPathConstants.NODE);
        return node;
    }

    //function for dynamically resizing the message to be max size
    public static void setMessageFont(JLabel label, JPanel component)
    {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int componentWidth = component.getWidth();


        // Find out how much the font can grow in width.
        double widthRatio = (double)componentWidth / (double)stringWidth;

        //calculate the new font size
        int newFontSize = (int)(labelFont.getSize() * widthRatio);
        int componentHeight = component.getHeight();

        // Pick a new font size so it will not be larger than the height of label.
        int fontSizeToUse = Math.min(newFontSize, componentHeight);

        // Set the label's font size to the newly determined size.
        label.setFont(new Font(labelFont.getName(), Font.BOLD, fontSizeToUse));
    }

    //function for determining how big the info font size should be
    public static void setInformationFont(JLabel message, JTextPane info)
    {
        Font messageFont = message.getFont();
        Font infoFont = info.getFont();
        int infoFontSize = 0;
        final int MAXSIZE = 50;

        //by default make it half the message
        infoFontSize = messageFont.getSize() / 2;

        //incase the message is massive, make sure the info doesn't get too big
        //in this case it's max size is 50
        if (infoFontSize>MAXSIZE)
        {
            infoFontSize=MAXSIZE;
        }

        //set the font
        info.setFont(new Font(infoFont.getName(), Font.BOLD, infoFontSize));
    }

    //function for getting how big a pic should be when getting scaled
    public static int[] getImgDimensions(int height, int width, double scale, Frame frame)
    {
        int newHeight;
        int newWidth;

        //if it is taller then it is wide
        if(height >= width)
        {
            //find the ratio and then calculate the new values
            double ratio = (double)width/(double)height;
            newHeight = (int) ((double) frame.getHeight() * scale);
            newWidth = (int) ((double) newHeight * ratio);
        }
        else
        {
            //if the image is wider, find the ration for this case ad calculate the sizes
            double ratio = (double)height/(double)width;
            newWidth = (int) ((double) frame.getWidth() * scale);
            newHeight = (int) ((double) newWidth * ratio);
        }
        //return the new sizes in an array
        return (new int[]{newHeight, newWidth});
    }
}


