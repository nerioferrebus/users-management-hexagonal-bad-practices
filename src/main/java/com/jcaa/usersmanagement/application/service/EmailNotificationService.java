package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.out.EmailSenderPort;
import com.jcaa.usersmanagement.domain.exception.EmailSenderException;
import com.jcaa.usersmanagement.domain.model.EmailDestinationModel;
import com.jcaa.usersmanagement.domain.model.UserModel;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
public final class EmailNotificationService {

  private static final String SUBJECT_CREATED = "Tu cuenta ha sido creada — Gestión de Usuarios";
  private static final String SUBJECT_UPDATED =
      "Tu cuenta ha sido actualizada — Gestión de Usuarios";

  private static final String TOKEN_NAME     = "name";
  private static final String TOKEN_EMAIL    = "email";
  private static final String TOKEN_PASSWORD = "password";
  private static final String TOKEN_ROLE     = "role";
  private static final String TOKEN_STATUS   = "status";

  private final EmailSenderPort emailSenderPort;

  public void notifyUserCreated(final UserModel user, final String plainPassword) {
    sendNotification(
        user,
        SUBJECT_CREATED,
        "user-created.html",
        Map.of(TOKEN_PASSWORD, plainPassword)
    );
  }

  public void notifyUserUpdated(final UserModel user) {
    sendNotification(
        user,
        SUBJECT_UPDATED,
        "user-updated.html",
        Map.of(TOKEN_STATUS, user.getStatus().name())
    );
  }

  private void sendNotification(
      final UserModel user,
      final String subject,
      final String templateName,
      final Map<String, String> additionalTokens) {

    final java.util.Map<String, String> tokens = new java.util.HashMap<>();
    tokens.put(TOKEN_NAME, user.getName().value());
    tokens.put(TOKEN_EMAIL, user.getEmail().value());
    tokens.put(TOKEN_ROLE, user.getRole().name());
    tokens.putAll(additionalTokens);

    final String templateContent = loadTemplate(templateName);
    final String body = renderTemplate(templateContent, tokens);
    final EmailDestinationModel destination = buildDestination(user, subject, body);

    sendEmail(destination);
  }



  private static EmailDestinationModel buildDestination(
      final UserModel user, final String subject, final String body) {
    return new EmailDestinationModel(
        user.getEmail().value(), user.getName().value(), subject, body);
  }

  private String loadTemplate(final String templateName) {
    final String path = "/templates/" + templateName;
    try (InputStream inputStream = openResourceStream(path)) {
      if (Objects.isNull(inputStream)) {
        throw EmailSenderException.becauseSendFailed(
            new IllegalStateException("Template not found: " + path));
      }
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (final IOException ioException) {
      throw EmailSenderException.becauseSendFailed(ioException);
    }
  }

  InputStream openResourceStream(final String path) {
    return getClass().getResourceAsStream(path);
  }

  private static String renderTemplate(String template, final Map<String, String> values) {
    String result = template;
    for (final Map.Entry<String, String> tokenEntry : values.entrySet()) {
      final String token = "{{" + tokenEntry.getKey() + "}}";
      result = result.replace(token, tokenEntry.getValue());
    }
    return result;
  }

  private void sendEmail(final EmailDestinationModel destination) {
    emailSenderPort.send(destination);
  }
}
