package com.jcaa.usersmanagement.domain.model;

import lombok.Value;
import java.util.Objects;

@Value
public class EmailDestinationModel {

  private static final String EMAIL_REQUIRED_MESSAGE = "El email del destinatario es requerido.";
  private static final String NAME_REQUIRED_MESSAGE = "El nombre del destinatario es requerido.";
  private static final String SUBJECT_REQUIRED_MESSAGE = "El asunto es requerido.";
  private static final String BODY_REQUIRED_MESSAGE = "El cuerpo del mensaje es requerido.";

  String destinationEmail;
  String destinationName;
  String subject;
  String body;

  public EmailDestinationModel(
      final String destinationEmail,
      final String destinationName,
      final String subject,
      final String body) {
    this.destinationEmail = validateNotBlank(destinationEmail, EMAIL_REQUIRED_MESSAGE);
    this.destinationName  = validateNotBlank(destinationName,  NAME_REQUIRED_MESSAGE);
    this.subject          = validateNotBlank(subject,          SUBJECT_REQUIRED_MESSAGE);
    this.body             = validateNotBlank(body,             BODY_REQUIRED_MESSAGE);
  }

  private static String validateNotBlank(final String value, final String errorMessage) {
    Objects.requireNonNull(value, errorMessage);
    if (value.trim().isEmpty()) {
      throw new IllegalArgumentException(errorMessage);
    }
    return value;
  }
}
