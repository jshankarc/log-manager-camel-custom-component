package qa.com.cbq.test;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManagerTest extends CamelTestSupport {
	
	private static final Logger log = LoggerFactory.getLogger(LogManagerTest.class);
	
    @Test
    public void testMy() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("file:src/temp?noop=true")
                	.setProperty("ProcCode", simple("NewMessage11"))
                	.setProperty("requestId", simple("Ravi"))
                	.setProperty("DeliveryChannel", simple("RIB"))
                	.convertBodyTo(java.lang.String.class)
            		.toD("logmanager://log?serviceName=MWT-Service&messageType=Rs&logTime=$simple{date:now:yyMMddHHmmssSSS}")
            		.log("${body}")
                  	.to("mock:result");
            }
        };
    }
}
