package com.example.calculator.soap;

import org.springframework.ws.client.core.WebServiceMessageCallback;

/**
 * Defines a generic contract for invoking SOAP web services.
 * This service is responsible for sending a SOAP request and receiving a response,
 * handling the necessary marshalling, unmarshalling, and connection configurations.
 */
public interface SoapInvokerService {

    /**
     * Invokes a specified SOAP web service endpoint with the given request and configuration.
     *
     * @param <T_REQ>           The generic type of the request payload object. This object will be marshalled to XML.
     * @param <T_RESP>          The generic type of the expected response payload object. The XML response will be unmarshalled to this type.
     * @param endpointUrl       The fully qualified URL of the SOAP service endpoint to which the request will be sent.
     * @param requestBody       The request payload object of type T_REQ. This object will be marshalled by JAXB.
     * @param requestCallback   An optional {@link WebServiceMessageCallback} that can be used to manipulate the SOAP request message
     *                          (e.g., to add SOAP headers) before it is sent. Can be {@code null} if no custom callback is needed.
     * @param contextPath       The JAXB context path, which is a colon-separated list of Java package names that contain schema-derived classes
     *                          and JAXB-annotated classes. This path is essential for the JAXB marshaller/unmarshaller to correctly
     *                          process the request and response objects. Example: "com.example.myschema.request:com.example.myschema.response".
     * @param timeoutInMillis   The timeout in milliseconds for both the connection establishment and the read operation.
     *                          A value of 0 means infinite timeout.
     * @return The response payload object of type T_RESP, unmarshalled from the SOAP response.
     * @throws org.springframework.ws.client.WebServiceIOException if a network error (e.g., connection timeout, host not found) occurs.
     * @throws org.springframework.ws.soap.client.SoapFaultClientException if the server returns a SOAP fault (e.g., application-level error).
     * @throws org.springframework.oxm.MarshallingFailureException if an error occurs during marshalling the request object.
     * @throws org.springframework.oxm.UnmarshallingFailureException if an error occurs during unmarshalling the response object.
     * @throws Exception for any other unexpected errors during the SOAP invocation process (e.g., configuration issues, reflection errors).
     *                   It's recommended that implementations catch specific exceptions and re-throw them or wrap them as appropriate.
     */
    <T_REQ, T_RESP> T_RESP invokeSoapService(
            String endpointUrl,
            T_REQ requestBody,
            WebServiceMessageCallback requestCallback,
            String contextPath,
            int timeoutInMillis) throws Exception;
}
