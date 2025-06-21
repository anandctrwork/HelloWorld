package com.example.calculator.soap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.soap.client.SoapFaultClientException; // Corrected import

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SoapInvokerServiceImplTest {

    @Mock
    private WebServiceTemplate mockWebServiceTemplate;

    private SoapInvokerServiceImpl soapInvokerServiceSpy;

    @BeforeEach
    void setUp() {
        // Create a real instance of the service
        SoapInvokerServiceImpl realService = new SoapInvokerServiceImpl();
        // Spy on the real instance
        soapInvokerServiceSpy = Mockito.spy(realService);

        // When the spy's createAndConfigureWebServiceTemplate is called,
        // make it return our mockWebServiceTemplate instead of creating a real one.
        doReturn(mockWebServiceTemplate)
                .when(soapInvokerServiceSpy)
                .createAndConfigureWebServiceTemplate(anyString(), anyInt());
    }

    @Test
    void testInvokeSoapService_Success() throws Exception {
        String endpointUrl = "http://example.com/soap";
        String requestBody = "Request"; // Simple String for testing
        String expectedResponse = "Response"; // Simple String for testing
        String contextPath = "com.example.test";
        int timeout = 5000;

        when(mockWebServiceTemplate.marshalSendAndReceive(eq(endpointUrl), eq(requestBody), any(WebServiceMessageCallback.class)))
                .thenReturn(expectedResponse);

        Object actualResponse = soapInvokerServiceSpy.invokeSoapService(endpointUrl, requestBody, null, contextPath, timeout);

        assertEquals(expectedResponse, actualResponse);
        verify(mockWebServiceTemplate).marshalSendAndReceive(eq(endpointUrl), eq(requestBody), any(WebServiceMessageCallback.class));
    }

    @Test
    void testInvokeSoapService_WebServiceIOException() {
        String endpointUrl = "http://example.com/soap";
        String requestBody = "Request";
        String contextPath = "com.example.test";
        int timeout = 5000;

        when(mockWebServiceTemplate.marshalSendAndReceive(eq(endpointUrl), eq(requestBody), any(WebServiceMessageCallback.class)))
                .thenThrow(WebServiceIOException.class); // Throw class for simplicity

        assertThrows(WebServiceIOException.class, () -> {
            soapInvokerServiceSpy.invokeSoapService(endpointUrl, requestBody, null, contextPath, timeout);
        });
    }

    @Test
    void testInvokeSoapService_SoapFaultClientException() {
        String endpointUrl = "http://example.com/soap";
        String requestBody = "Request";
        String contextPath = "com.example.test";
        int timeout = 5000;

        // Mock SoapFaultClientException - constructor might need a message or fault object
        SoapFaultClientException mockException = mock(SoapFaultClientException.class);

        when(mockWebServiceTemplate.marshalSendAndReceive(eq(endpointUrl), eq(requestBody), any(WebServiceMessageCallback.class)))
                .thenThrow(mockException);

        assertThrows(SoapFaultClientException.class, () -> {
            soapInvokerServiceSpy.invokeSoapService(endpointUrl, requestBody, null, contextPath, timeout);
        });
    }

    @Test
    void testInvokeSoapService_WithCallback() throws Exception {
        String endpointUrl = "http://example.com/soap";
        String requestBody = "Request";
        String expectedResponse = "Response";
        String contextPath = "com.example.test";
        int timeout = 5000;
        WebServiceMessageCallback mockCallback = mock(WebServiceMessageCallback.class);

        when(mockWebServiceTemplate.marshalSendAndReceive(eq(endpointUrl), eq(requestBody), eq(mockCallback)))
                .thenReturn(expectedResponse);

        soapInvokerServiceSpy.invokeSoapService(endpointUrl, requestBody, mockCallback, contextPath, timeout);

        verify(mockWebServiceTemplate).marshalSendAndReceive(eq(endpointUrl), eq(requestBody), eq(mockCallback));
    }
}
