package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonFileReader {
    private static final String JSON_LABLE_NAME = "Name";
    private static final String JSON_LABLE_TYPE = "Type";
    private static final String JSON_LABLE_ELEMENTS = "Elements";
    private static final String JSON_LABLE_DESCRIPTION = "Description";

    public static void main(String[] args) throws IOException {
        String jsonFilePath = "D:\\Work\\01 项目\\06 FAW\\SOA建模与二次开发项目\\二次开发\\ModelStructure.json";
        File file = new File(jsonFilePath);
        getJsonFromFile(file);
    }


    public static void getJsonFromFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file);

        JsonNode swTemplateNode = getJsonNodeByDescription(rootNode, "SoftwareTypePackageStructureTemplate");
        System.out.println("swTemplateNode = " + swTemplateNode);
        JsonNode softwareComponentTypes = getJsonNodeByName(swTemplateNode, "SoftwareComponentTypes");
        System.out.println("softwareComponentTypes = " + softwareComponentTypes);

    }

    /**
     * get JsonNode By Type value
     *
     * @param jsonNode  jsonNode
     * @param typeValue typeValue
     * @return jsonNode
     */
    private static JsonNode getJsonNodeByType(JsonNode jsonNode, String typeValue) {
        if (jsonNode == null || "".equals(typeValue)) {
            return null;
        }
        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                if (node.path(JSON_LABLE_TYPE).asText().equals(typeValue)) {
                    return node;
                } else {
                    JsonNode elementsNode = node.path(JSON_LABLE_ELEMENTS);
                    if (elementsNode.isArray()) {
                        JsonNode elementNodes = getJsonNodeByType(node, typeValue);
                        if (elementNodes != null) {
                            return elementNodes;
                        }
                    }
                }
            }
        } else {
            if (jsonNode.path(JSON_LABLE_TYPE).asText().equals(typeValue)) {
                return jsonNode;
            } else {
                JsonNode elementNodes = jsonNode.path(JSON_LABLE_ELEMENTS);
                if (elementNodes.isArray()) {
                    JsonNode elementNode = getJsonNodeByType(elementNodes, typeValue);
                    if (elementNode != null) {
                        return elementNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * get JsonNode By Description
     *
     * @param jsonNode    jsonNode
     * @param description typeValue
     * @return jsonNode
     */
    private static JsonNode getJsonNodeByDescription(JsonNode jsonNode, String description) {
        if (jsonNode == null || "".equals(description)) {
            return null;
        }
        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                if (node.path(JSON_LABLE_DESCRIPTION).asText().equals(description)) {
                    return node;
                } else {
                    JsonNode elementNodes = node.path(JSON_LABLE_ELEMENTS);
                    if (elementNodes.isArray()) {
                        JsonNode elementNode = getJsonNodeByDescription(node, description);
                        if (elementNode != null) {
                            return elementNode;
                        }
                    }
                }
            }
        } else {
            if (jsonNode.path(JSON_LABLE_DESCRIPTION).asText().equals(description)) {
                return jsonNode;
            } else {
                JsonNode elementNodes = jsonNode.path(JSON_LABLE_ELEMENTS);
                if (elementNodes.isArray()) {
                    JsonNode elementNode = getJsonNodeByDescription(elementNodes, description);
                    if (elementNode != null) {
                        return elementNode;
                    }
                }
            }
        }
        return null;
    }

    /**
     * get JsonNode By Name
     *
     * @param jsonNode jsonNode
     * @param name     jsonNodeName
     * @return jsonNode
     */
    private static JsonNode getJsonNodeByName(JsonNode jsonNode, String name) {
        if (jsonNode == null || "".equals(name)) {
            return null;
        }
        if (jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                if (node.path(JSON_LABLE_NAME).asText().equals(name)) {
                    return node;
                } else {
                    JsonNode elementNodes = node.path(JSON_LABLE_ELEMENTS);
                    if (elementNodes.isArray()) {
                        JsonNode elementNode = getJsonNodeByName(node, name);
                        if (elementNode != null) {
                            return elementNode;
                        }
                    }
                }
            }
        } else {
            if (jsonNode.path(JSON_LABLE_NAME).asText().equals(name)) {
                return jsonNode;
            } else {
                JsonNode elementNodes = jsonNode.path(JSON_LABLE_ELEMENTS);
                if (elementNodes.isArray()) {
                    JsonNode elementNode = getJsonNodeByName(elementNodes, name);
                    if (elementNode != null) {
                        return elementNode;
                    }
                }
            }
        }
        return null;
    }
}
