package com.serverless

import com.amazonaws.serverless.exceptions.ContainerInitializationException
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.serverless.lambda.Request
import com.serverless.lambda.Response
import com.serverless.service.DispatcherService
import groovy.transform.Memoized
import groovy.util.logging.Log4j
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.context.support.XmlWebApplicationContext

public class SpringLambdaHandler
        implements RequestHandler<AwsProxyRequest, AwsProxyResponse>
{
  SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
  boolean isinitialized = false;

  public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context)
  {
    if (!isinitialized) {
      isinitialized = true;
      try {
        XmlWebApplicationContext wc = new XmlWebApplicationContext();
        wc.setConfigLocation("classpath:/staticAppContext.xml");
        handler = SpringLambdaContainerHandler.getAwsProxyHandler(wc);
      } catch (ContainerInitializationException e) {
        e.printStackTrace();
        return null;
      }
    }
    AwsProxyResponse res = handler.proxy(awsProxyRequest, context);
    return res;
  }
}