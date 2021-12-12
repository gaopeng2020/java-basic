package xml;

import ept.commonapi.EPTUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

public class XmlTest_dom4j {
    private static File lastChooserDir = null;

    public static void main(String[] args) throws DocumentException, IOException {
        String diretory = "D:\\Users\\Lenovo\\Documents\\DaVinci\\SAIC\\MPD_MCU\\PPV";
        String xmlName = "MPD_1.3 - 副本.arxml";

        final FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("xml(*.arxml,*.xml)", "arxml", "xml");
        final File file = EPTUtils.fileSelector(extensionFilter);


//        File file = new File(diretory + "\\" + xmlName);
//        SAXReader saxReader = new SAXReader();
//        final Document doc = saxReader.read(file);
//        final Element rootElement = doc.getRootElement();
//
//        final Element communication = getArPackagesByNameFromArxml(rootElement, "Communication");
//        final Element signals = getElementsByNameFromArPackages(communication, "Signals");
//        final List<Element> signalElements = getSpecifiedElementsByTypeFromElements(signals, "I-SIGNAL");
//        for (Element signal : signalElements) {
//            addSignalTypeForSignals(signal);
//        }
////        addSignalTypeForSignals(Signals);
//        String newXmlName = "new_"+xmlName;
//        writeXml2File(doc,diretory+"\\"+newXmlName);
    }

    public static List<Element> getSpecifiedElementsByTypeFromElements(Element element, String type) {
        List<Element> elements = new LinkedList<>();
        if (element == null) {
            return elements;
        }
        final Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()) {
            final Element nextElement = iterator.next();
            if (type.equals(nextElement.getName())) {
                elements.add(nextElement);
            }
        }
        return elements;
    }

    public static void addSignalTypeForSignals(Element element) {
        if (element == null || !"I-SIGNAL".equals(element.getName())) {
            return;
        }
        //查看element是否符合添加I-SIGNAL-TYPE的条件
        final Iterator<Element> iterator = element.elementIterator();
        while (iterator.hasNext()) {
            final String elementType = iterator.next().getName();
            if ("DATA-TRANSFORMATIONS".equals(elementType) || "TRANSFORMATION-I-SIGNAL-PROPSS".equals(elementType)
                    || "I-SIGNAL-TYPE".equals(elementType)) {
                return;
            }
        }
        //将I-SIGNAL-TYPE插入到DATA-TYPE-POLICY的后面
        int order = 0;
        final Iterator<Element> it = element.elementIterator();
        while (it.hasNext()) {
            final String elementType = it.next().getName();
            if ("SHORT-NAME".equals(elementType) || "DESC".equals(elementType)
                    || "LONG-NAME".equals(elementType) || "DATA-TYPE-POLICY".equals(elementType)) {
                order++;
            }
        }
        //add QName,to remove xmlns attribute
        final QName qName = QName.get("I-SIGNAL-TYPE", element.getNamespace());
        final Element signalTypeElement = DocumentHelper.createElement(qName);

        signalTypeElement.setText("PRIMITIVE");
        signalTypeElement.remove(signalTypeElement.attribute("xmlns"));
        element.elements().add(order, signalTypeElement);
    }

    public static Element getElementByNameFromElements(Element element, String name) {
        if (element == null || "".equals(name)) {
            return null;
        }
        final Iterator<Element> elementsIterator = element.elementIterator();
        while (elementsIterator.hasNext()) {
            final Element subElement = elementsIterator.next();
            if (name.equals(subElement.element("SHORT-NAME").getText())) {
                return subElement;
            }
        }
        return null;
    }

    public static Element getElementsByNameFromArPackages(final Element element, String name) {
        if (element == null || "".equals(name)) {
            return null;
        }
        final Iterator<Element> arPackageElementIterator = element.elementIterator();
        while (arPackageElementIterator.hasNext()) {
            final Element arPackageElement = arPackageElementIterator.next();
            if (name.equals(arPackageElement.element("SHORT-NAME").getText())) {
                return arPackageElement.element("ELEMENTS");
            }
        }
        return null;
    }

    public static Element getArPackagesByNameFromArxml(final Element element, String name) {
        if (element == null || "".equals(name)) {
            return null;
        }
        final Element arPackagesElement = element.element("AR-PACKAGES");
        final Iterator<Element> arPackagesElementIterator = arPackagesElement.elementIterator();
        while (arPackagesElementIterator.hasNext()) {
            final Element arPackageElement = arPackagesElementIterator.next();
            if (name.equals(arPackageElement.element("SHORT-NAME").getText())) {
                return arPackageElement.element("AR-PACKAGES");
            }
        }
        return null;
    }

    public static void writeXml2File(Document document, String filePath) {
        File xmlFile = new File(filePath);
        XMLWriter writer = null;
        final OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        try {
            if (xmlFile.exists()) {
                xmlFile.delete();
            }
            writer = new XMLWriter(new FileOutputStream(xmlFile), outputFormat);
            writer.write(document);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
