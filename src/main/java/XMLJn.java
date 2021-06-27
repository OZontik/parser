
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class XMLJn {

    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        writeString("data2.json", json);
    }

    private static List<Employee> parseXML(String s) {
        List<Employee> result = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("data.xml"));
            Node root = doc.getDocumentElement();
            System.out.println("Корневой элемент: " + root.getNodeName());
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.println("Текущий элемент: " + node.getNodeName());
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;
                    result.add(new Employee(
                            Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent()),
                            element.getElementsByTagName("firstName").item(0).getTextContent(),
                            element.getElementsByTagName("lastName").item(0).getTextContent(),
                            element.getElementsByTagName("country").item(0).getTextContent(),
                            Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent())
                    ));
                }
            }
            return result;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeString(String fileName, String json) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }
}
