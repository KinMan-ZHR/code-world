package com.jiuaoedu.communicationframework.extension.serializer;

import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class XmlSerializer implements MessageSerializer {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String serialize(Message message) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("message");
            doc.appendChild(root);

            addElement(doc, root, "messageId", message.getMessageId());
            addElement(doc, root, "senderId", message.getSenderId());
            addElement(doc, root, "receiverId", message.getReceiverId());
            addElement(doc, root, "content", message.getContent());
            addElement(doc, root, "type", message.getType().name());
            addElement(doc, root, "timestamp", message.getTimestamp().format(FORMATTER));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException("XML序列化失败", e);
        }
    }

    @Override
    public Message deserialize(String serializedMessage) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(serializedMessage)));

            String senderId = getElementValue(doc, "senderId");
            String receiverId = getElementValue(doc, "receiverId");
            String content = getElementValue(doc, "content");
            MessageType type = MessageType.valueOf(getElementValue(doc, "type"));

            return new Message(senderId, receiverId, content, type);
        } catch (Exception e) {
            throw new RuntimeException("XML反序列化失败", e);
        }
    }

    private void addElement(Document doc, Element parent, String tagName, String value) {
        Element element = doc.createElement(tagName);
        element.setTextContent(value);
        parent.appendChild(element);
    }

    private String getElementValue(Document doc, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}