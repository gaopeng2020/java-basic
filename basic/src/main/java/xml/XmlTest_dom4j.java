package xml;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Iterator;

public class XmlTest_dom4j {
    public static void main(String[] args) throws DocumentException {
        String diretory = "E:\\SVN\\上汽智驾私网网络配置项目\\03_项目实施及监控\\07_网络配置\\02_交付物\\PPV\\IPS";
        String xmlName = "IPS_PPV_V1.1_20211025.arxml";

        File file = new File(diretory+"\\"+xmlName);
        SAXReader saxReader = new SAXReader();
        final Document doc = saxReader.read(file);
        final Element rootElement = doc.getRootElement();

        final Element communication = getArPackagesByNameFromArxml(rootElement, "Communication");
        final Element Signals = getElementsByNameFromArPackages(communication,"Signals");

        addSignalTypeForSignals(Signals);
        String newXmlName = "new_"+xmlName;
        writeXml2File(doc,diretory+"\\"+newXmlName);
    }

    private static void addSignalTypeForSignals(Element element) {
        if (element == null){
            return;
        }
        final Iterator<Element> signalElements = element.elementIterator();
        while (signalElements.hasNext()){
            //add QName,to remove xmlns attribute
            final QName qName = QName.get("I-SIGNAL-TYPE", element.getNamespace());
            final Element signalElement = signalElements.next();
            final Element signalTypeElement = DocumentHelper.createElement(qName);

            signalTypeElement.setText("PRIMITIVE");
            signalTypeElement.remove(signalTypeElement.attribute("xmlns"));
            signalElement.elements().add(3,signalTypeElement);
        }
    }

    private static Element getElementByNameFromElements(Element element, String name) {
        if (element == null || "".equals(name)){
            return null;
        }
        final Iterator<Element> elementsIterator = element.elementIterator();
        while(elementsIterator.hasNext()){
            final Element subElement = elementsIterator.next();
            if (name.equals(subElement.element("SHORT-NAME").getText())){
                System.out.println("subElement = " + subElement.element("SHORT-NAME").getText());
                return subElement;
            }
        }
        return null;
    }

    private static Element getElementsByNameFromArPackages(final Element element, String name) {
        if (element == null || "".equals(name)){
            return null;
        }
        final Iterator<Element> arPackageElementIterator = element.elementIterator();
        while(arPackageElementIterator.hasNext()){
            final Element arPackageElement = arPackageElementIterator.next();
            if (name.equals(arPackageElement.element("SHORT-NAME").getText())){
                System.out.println("ELEMENTS = " + arPackageElement.element("SHORT-NAME").getText());
                return arPackageElement.element("ELEMENTS");
            }
        }
        return null;
    }

    private static Element getArPackagesByNameFromArxml(final Element element, String name) {
        if (element == null || "".equals(name)){
            return null;
        }
        final Element arPackagesElement = element.element("AR-PACKAGES");
        final Iterator<Element> arPackagesElementIterator = arPackagesElement.elementIterator();
        while(arPackagesElementIterator.hasNext()){
            final Element arPackageElement = arPackagesElementIterator.next();
            if (name.equals(arPackageElement.element("SHORT-NAME").getText())){
                System.out.println("AR-PACKAGES = " + arPackageElement.element("SHORT-NAME").getText());
                return arPackageElement.element("AR-PACKAGES");
            }
        }
        return null;
    }

    public static void writeXml2File(Document document, String filePath){
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
