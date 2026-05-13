package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.jcaa.usersmanagement.domain.exception.InvalidUserPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Test: User Password Value Object")
class UserPasswordTest {

  @ParameterizedTest
  @ValueSource(strings = {"password123", "   password123   "})
  @DisplayName("Should normalize and hash password")
  void shouldNormalizeAndHashPassword(final String input) {
    // Act
    final UserPassword result = UserPassword.fromPlainText(input);
    
    // Assert
    assertNotNull(result.value());
    assertNotEquals(input.trim(), result.value());
  }

  @ParameterizedTest
  @ValueSource(strings = {"clave", "    clave     "})
  @DisplayName("Should fail when password is too short after trimming")
  void shouldFailWhenPasswordIsTooShort(final String password) {
    // Act & Assert
    assertThrows(InvalidUserPasswordException.class, () -> UserPassword.fromPlainText(password));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "  ", "\r", "\t", "\n", "\f", "\b", "\0"})
  @DisplayName("Should throw when password is empty or blank")
  void shouldThrowWhenPasswordIsEmptyOrBlank(final String password) {
    // Act & Assert
    assertThrows(InvalidUserPasswordException.class, () -> UserPassword.fromPlainText(password));
  }

  @Test
  @DisplayName("Should throw when password is null")
  void shouldThrowWhenPasswordIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> UserPassword.fromPlainText(null));
  }

  @Test
  @DisplayName("Should verify plain password correctly")
  void shouldVerifyPlainPassword() {
    // Arrange
    final String plainPassword = "mySecurePassword";
    
    // Act
    final UserPassword userPassword = UserPassword.fromPlainText(plainPassword);
    
    // Assert
    assertTrue(userPassword.verifyPlain(plainPassword));
  }

  @Test
  @DisplayName("Should create UserPassword from hash and be equal to original")
  void shouldCreateUserPasswordFromExistingHash() {
    // Arrange
    final String rawPassword = "Abcde1234567";
    final UserPassword originalUserPassword = UserPassword.fromPlainText(rawPassword);
    final String generatedHash = originalUserPassword.value();
    
    // Act
    final UserPassword fromHashUserPassword = UserPassword.fromHash(generatedHash);
    
    // Assert
    assertEquals(
        originalUserPassword,
        fromHashUserPassword,
        "Los objetos UserPassword deberían ser iguales al usar el mismo hash");
    assertTrue(
        fromHashUserPassword.verifyPlain(rawPassword),
        "El objeto creado desde el hash debería poder verificar el password en texto plano");
  }

  @Test
  @DisplayName("equals: Should return false when other is not instance of UserPassword")
  void shouldReturnFalseWhenOtherIsNotInstanceOfUserPassword() {
    // Arrange
    final UserPassword password = UserPassword.fromPlainText("MiPassword123");
    final Object nonUserPassword = mock(Object.class);
    
    // Act & Assert
    assertNotEquals(password, nonUserPassword);
  }

  @Test
  @DisplayName("equals: Should return false when different hash")
  void shouldReturnFalseWhenDifferentHash() {
    // Arrange
    final UserPassword a = UserPassword.fromPlainText("MiPassword123");
    final UserPassword b = UserPassword.fromPlainText("OtroPassword456");
    
    // Act & Assert
    assertNotEquals(a, b);
  }

  @Test
  @DisplayName("hashCode: Should be consistent for same instance")
  void shouldReturnConsistentHashCode() {
    // Arrange
    UserPassword password = UserPassword.fromPlainText("MiPassword123");
    
    // Act
    final int firstHashCode = password.hashCode();
    final int secondHashCode = password.hashCode();
    
    // Assert
    assertEquals(firstHashCode, secondHashCode);
  }

  @Test
  @DisplayName("hashCode: Should be equal for equal objects")
  void shouldHaveSameHashCodeWhenEqual() {
    // Arrange
    final UserPassword a = UserPassword.fromPlainText("MiPassword123");
    final UserPassword b = UserPassword.fromHash(a.value()); // mismo hash => equals true
    
    // Act & Assert
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }
}
