package ru.osipov;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){

        String fileName = "data.xml";
        String fileNameJson = "data.json";
        List<Employee> list = parseXML(fileName);

        String json = listToJson(list);

        writeToJSonFile(json, fileNameJson);

        System.out.println("Все сделано!!!");
    }

    static List<Employee> parseXML(String fileName) {
        List<Employee> list = new ArrayList<>();

        try {
            // Создается построитель документа
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создается дерево DOM документа из файла
            Document document = documentBuilder.parse(fileName);
            // Получаем корневой элемент
            Node root = document.getDocumentElement();
//            System.out.println("Корневой элемент: " + root.getNodeName());

            // Просматриваем все подэлементы корневого - т.е. employee
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                // Если нода не текст, то - заходим внутрь
                if (node.getNodeType() != Node.TEXT_NODE) {
                    NodeList nodeProps = node.getChildNodes();
                    Employee employer = new Employee();
                    for (int j = 0; j < nodeProps.getLength(); j++) {
                        Node nodeProp = nodeProps.item(j);
                        // Если нода не текст, то это один из параметров - печатаем
                        if (nodeProp.getNodeType() != Node.TEXT_NODE) {
                            System.out.println(nodeProp.getNodeName() + ":" + nodeProp.getChildNodes().item(0).getTextContent());
                            switch (nodeProp.getNodeName()) {
                                case "id" -> employer.id = Long.parseLong(nodeProp.getChildNodes().item(0).getTextContent());
                                case "firstName" -> employer.firstName = nodeProp.getChildNodes().item(0).getTextContent();
                                case "lastName" -> employer.lastName = nodeProp.getChildNodes().item(0).getTextContent();
                                case "country" -> employer.country = nodeProp.getChildNodes().item(0).getTextContent();
                                case "age" -> employer.age = Integer.parseInt(nodeProp.getChildNodes().item(0).getTextContent());
                            }
                        }
                    }
                    list.add(employer);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            ex.printStackTrace(System.out);
        }
        return list;
    }

    static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return new Gson().toJson(list, listType);
    }

    static void writeToJSonFile(String json, String fileJson) {
        try (FileWriter file = new FileWriter(fileJson)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}