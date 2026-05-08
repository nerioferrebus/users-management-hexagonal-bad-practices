package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCredentialsException extends DomainException {

  private static final String CREDENTIALS_INVALID_MESSAGE = "Correo o contraseña incorrectos.";
  private static final String ACCOUNT_NOT_ACTIVE_MESSAGE = "Tu cuenta no está activa. Contacta al administrador.";

  private InvalidCredentialsException(final String message) {
    super(message);
  }

  public static InvalidCredentialsException becauseCredentialsAreInvalid() {
    return new InvalidCredentialsException(CREDENTIALS_INVALID_MESSAGE);
  }

  public static InvalidCredentialsException becauseUserIsNotActive() {
    return new InvalidCredentialsException(ACCOUNT_NOT_ACTIVE_MESSAGE);
  }
}
