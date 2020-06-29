package com.lucky.service.base;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.Test;

/**
 * @Author:
 * @Date:
 */
public class WebServiceTest {
    @Test
    public void testSend1(){

        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://192.168.50.34:8800/ws-api/transactionInquiry");

        // 需要密码的情况需要加上用户名和密码
        // client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME,PASS_WORD));
        Object[] objects = new Object[0];
        String param = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n" +
                "\t\t<root>\n" +
                "\t\t\t<param flag=\"1\">（两个接口都叫DoAsk，用flag来区分）\n" +
                "\t\t\t\t\t<xm>周德静</xm>（姓名）\n" +
                "\t\t\t\t\t<zjhm>341126198702133620</zjhm>（证件号码）\n" +
                "\t\t\t\t\t<cqzh>2019007042</cqzh>（产权证号）\n" +
                "\t\t\t</param>\n" +
                "\t\t</root>";
        try {

            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("transactionInquiry", param);
            System.out.println("返回数据:" + objects[0]);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}
