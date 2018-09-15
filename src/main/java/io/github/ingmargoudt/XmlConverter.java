package io.github.ingmargoudt;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlConverter {

    public static String toXml(Object o) {

        StringWriter stringWriter = new StringWriter();

        try {
            JAXBContext jb = JAXBContext.newInstance(o.getClass());
            Marshaller jaxbMarshaller = jb.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            jaxbMarshaller.marshal(o, System.out);
            jaxbMarshaller.marshal(o, stringWriter);

            return stringWriter.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return "";


    }

    public static <T> T toObject(Class<T> clzz, String content) {
        StringReader stringReader = new StringReader(content);
        JAXBContext jb;
        try {
            jb = JAXBContext.newInstance(clzz);

            Unmarshaller jaxbMarshaller = jb.createUnmarshaller();
            return clzz.cast(jaxbMarshaller.unmarshal(stringReader));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String toXmlSilent(Object o) {
        StringWriter stringWriter = new StringWriter();

        try {
            JAXBContext jb = JAXBContext.newInstance(o.getClass());
            Marshaller jaxbMarshaller = jb.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.marshal(o, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return "";
    }
}
