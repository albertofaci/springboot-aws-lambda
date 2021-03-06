package com.serverless.handler


import com.serverless.lambda.Request
import com.serverless.lambda.Response
import com.serverless.persistence.repository.UserRepository
import groovy.json.JsonOutput
import groovy.transform.CompileStatic
import groovy.util.logging.Log4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@Log4j
@CompileStatic
class GetUsers implements Handler {

  @Autowired
  private UserRepository userRepository

  @Value('${app-config.customField}')
  private String customField

  @Override
  boolean route(final Request request) {
    request.resourcePath() == '/users' && request.httpMethod() == 'GET'
  }

  @Override
  Response respond(final Request request) {
    def allCustomers = userRepository.findAll()
    def response = [
            customField: customField,
            customers: allCustomers
    ]
    Response.builder()
        .statusCode(200)
        .body(JsonOutput.prettyPrint(JsonOutput.toJson(response)))
        .build()
  }
}
