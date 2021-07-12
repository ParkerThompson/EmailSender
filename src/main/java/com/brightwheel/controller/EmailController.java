package com.brightwheel.controller;

import com.brightwheel.entities.Email;
import com.brightwheel.exceptions.InvalidParameterException;
import com.brightwheel.responses.SendGridResponse;
import com.brightwheel.responses.SnailgunResponse;
import com.brightwheel.services.EmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.MethodNotAllowedException;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Value(value = "${url}")
    private String url;

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    @Qualifier("emailService")
    private EmailService emailService;

    /**
     * Send email using configured email service
     * @param email Input message for email
     * @return A response object
     */
    @ApiOperation(value="Send email using configured email service")
    @ApiResponses({
            @ApiResponse(code=200, message = "Success", response = SnailgunResponse.class),
            @ApiResponse(code=201, message = "Success", response = SendGridResponse.class),
            @ApiResponse(code=400, message = "Invalid Parameter", response = InvalidParameterException.class)
    })
    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public ResponseEntity<String> send(
            @ApiParam("An object containing all the email fields")
            @RequestBody Email email) {
        if(email == null) {
            throw new InvalidParameterException("Request body cannot be null");
        }
        return emailService.send(email);
    }

    /**
     * Query status of an asynchronous email
     * @param id The service provided identifier for the email
     * @return A response object the includes a status field
     */
    @ApiOperation(value="Query status of an asynchronous email")
    @ApiResponses({
            @ApiResponse(code=200, message = "Success", response = SnailgunResponse.class),
            @ApiResponse(code=400, message = "Invalid Parameter", response = InvalidParameterException.class),
            @ApiResponse(code=405, message = "Method Not Allowed", response = MethodNotAllowedException.class)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> status(
            @ApiParam("The service provided identifier for the email")
            @PathVariable String id) {
        if(id == null) {
            throw new InvalidParameterException("Id cannot be null");
        }
        if(id.isEmpty()) {
            throw new InvalidParameterException("Id cannot be empty");
        }
        return emailService.getStatus(id);
    }
}
