package com.lucky.service.base.utils;

import com.lucky.service.base.LuckyBaseServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:
 * @Date:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes={LuckyBaseServiceApplication.class})// 指定启动类
public class XmlUtilsTest {

    @Autowired
    private XmlUtils xmlUtils;

    @Test
    public void test()throws Exception{
        String param = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n" +
                "\t\t<root>\n" +
                "\t\t\t<param flag=\"1\">（两个接口都叫DoAsk，用flag来区分）\n" +
                "\t\t\t\t\t<xm>周德静</xm>（姓名）\n" +
                "\t\t\t\t\t<zjhm>341126198702133620</zjhm>（证件号码）\n" +
                "\t\t\t\t\t<cqzh>2019007042</cqzh>（产权证号）\n" +
                "\t\t\t</param>\n" +
                "\t\t</root>";

        List<Element> elementList = xmlUtils.getElementsByNode(param,"param");
        for(Element node : elementList){
            String flag = node.attributeValue("flag");
            Element xmElement = node.element("xm");
            String xm = xmElement.getTextTrim();
            Element zjhmElement = node.element("zjhm");
            String zjhm = xmElement.getTextTrim();
            Element cqzhElement = node.element("cqzh");
            String cgzh = xmElement.getTextTrim();
        }
    }

    @Test
    public void test1()throws Exception{
        SAXReader saxReader = new SAXReader();
        File file = ResourceUtils.getFile("classpath:transaction/xscqResponse.xml");
        Document document = saxReader.read(file);
        String documentStr = document.asXML();
        List<Element> dataList = xmlUtils.getElementsByNode(documentStr,"data");
        Element dataTemplate = dataList.get(0);
        Element dataElement =  document.getRootElement().element("data");
        dataElement.getParent().remove(dataElement);

        for(int i = 0 ; i<=3 ; i++){
            String test1 = document.getRootElement().asXML();
            String test = dataTemplate.asXML();
            document.getRootElement().add(dataTemplate);
//            resultList.add(dataTemplate);
        }
        String result = document.asXML();
        System.out.println(result);
    }
}
