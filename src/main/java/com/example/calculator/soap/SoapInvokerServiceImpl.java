package com.example.calculator.soap;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.soap.client.SoapFaultClientException;
// javax.xml.transform.Source might be needed for raw response handling, not used in this basic implementation.

/**
 * Service implementation for invoking SOAP web services using Spring WS.
 * This class implements the {@link SoapInvokerService} interface and utilizes
 * {@link WebServiceTemplate} for the actual SOAP communication.
 * It handles the configuration of JAXB marshalling/unmarshalling and HTTP message sending,
 * including timeout settings.
 *
 * The service is designed to be generic, allowing invocation of various SOAP services
 * by specifying endpoint URLs, request/response types, JAXB context paths, and timeouts.
 */
@Service
public class SoapInvokerServiceImpl implements SoapInvokerService {

    /**
     * Invokes a SOAP web service endpoint using a dynamically configured {@link WebServiceTemplate}.
     * <p>
     * This method sets up a JAXB marshaller for the given context path and configures
     * connection and read timeouts for the SOAP call. It then uses Spring's
     * {@link WebServiceTemplate#marshalSendAndReceive(String, Object, WebServiceMessageCallback)}
     * method to perform the actual SOAP request-response exchange.
     * <p>
     * Specific exceptions like {@link WebServiceIOException} for network issues and
     * {@link SoapFaultClientException} for server-side SOAP faults are caught and re-thrown,
     * allowing callers to implement specific error handling logic.
     *
     * @param <T_REQ>           The generic type of the request payload object.
     * @param <T_RESP>          The generic type of the expected response payload object.
     * @param endpointUrl       The URL of the target SOAP service endpoint.
     * @param requestBody       The request object to be marshalled and sent.
     * @param requestCallback   An optional callback to customize the SOAP message before sending (e.g., adding headers). Can be {@code null}.
     * @param contextPath       The JAXB context path required for marshalling the request and unmarshalling the response.
     *                          This should point to the packages containing JAXB annotated classes.
     * @param timeoutInMillis   The timeout in milliseconds for both establishing a connection and reading the response.
     * @return The unmarshalled response object of type T_RESP.
     * @throws WebServiceIOException if a network error (e.g., connection timeout, host not found) occurs.
     * @throws SoapFaultClientException if the server returns a SOAP fault.
     * @throws RuntimeException if there's an issue initializing the JAXB marshaller or the message sender.
     * @throws Exception for any other unexpected errors during the SOAP invocation (e.g., marshalling/unmarshalling issues not covered by specific Spring exceptions).
     */
    @Override
    public <T_REQ, T_RESP> T_RESP invokeSoapService(
            String endpointUrl,
            T_REQ requestBody,
            WebServiceMessageCallback requestCallback,
            String contextPath,
            int timeoutInMillis) throws Exception {

        WebServiceTemplate webServiceTemplate = createAndConfigureWebServiceTemplate(contextPath, timeoutInMillis);

        try {
            // The actual SOAP call
            @SuppressWarnings("unchecked")
            T_RESP response = (T_RESP) webServiceTemplate.marshalSendAndReceive(endpointUrl, requestBody, requestCallback);
            return response;
        } catch (WebServiceIOException e) {
            // Handle network/IO issues
            throw e;
        } catch (SoapFaultClientException e) {
            // Handle SOAP faults from the server
            throw e;
        } catch (Exception e) {
            // Handle other unexpected issues
            throw e;
        }
    }

    /**
     * Creates, configures, and returns a {@link WebServiceTemplate} instance for a specific SOAP invocation.
     * <p>
     * This method centralizes the creation and configuration of the {@code WebServiceTemplate},
     * including setting up the JAXB marshaller/unmarshaller with the provided {@code contextPath}
     * and configuring the HTTP message sender with the specified {@code timeoutInMillis}
     * for both connection and read operations.
     * <p>
     * The visibility of this method is package-private (default) to allow for easier testing,
     * enabling test classes within the same package to spy on this service and override this
     * method to provide a mocked {@code WebServiceTemplate}.
     *
     * @param contextPath     The JAXB context path (e.g., "com.example.your.schemas") necessary for
     *                        marshalling the request and unmarshalling the response.
     * @param timeoutInMillis The timeout value in milliseconds to be applied to connection
     *                        and read operations of the SOAP message sender.
     * @return A fully configured {@code WebServiceTemplate} instance ready for use.
     * @throws RuntimeException if the JAXB marshaller fails to initialize for the given context path.
     */
    WebServiceTemplate createAndConfigureWebServiceTemplate(String contextPath, int timeoutInMillis) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(contextPath);
        try {
            marshaller.afterPropertiesSet(); // Initialize marshaller
        } catch (Exception e) {
            // Consider logging this or wrapping in a more specific runtime exception
            throw new RuntimeException("Failed to initialize JAXB marshaller for contextPath: " + contextPath, e);
        }

        WebServiceTemplate webServiceTemplate = new WebServiceTemplate(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);

        HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
        messageSender.setConnectionTimeout(timeoutInMillis);
        messageSender.setReadTimeout(timeoutInMillis);
        // It's good practice to initialize the messageSender, though not strictly required for basic timeouts.
        // For more complex configurations (e.g. HttpClient) it becomes more important.
        // messageSender.afterPropertiesSet(); // Uncomment if using more complex HttpComponentsMessageSender features.

        webServiceTemplate.setMessageSender(messageSender);
        return webServiceTemplate;
    }
}
