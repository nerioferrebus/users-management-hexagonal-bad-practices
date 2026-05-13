package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidUserNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Test: User Name Value Object")
class UserNameTest {

  @ParameterizedTest
  @ValueSource(strings = {"John Arrieta", "   John Arrieta   ", "John Arrieta \t"})
  @DisplayName("Should create User Name with trimmed value when input has whitespaces")
  void shouldValidateUserNameMinimumLength(final String userName) {
    // Arrange
    final String correctUserName = "John Arrieta";
    
    // Act
    final UserName userNameVo = new UserName(userName);
    
    // Assert
    assertEquals(correctUserName, userNameVo.value());
  }

  // -- Flujo con excepciones y ramas de validación ---

  @Test
  @DisplayName("Should throw NullPointerException when User Name is null")
  void shouldValidateUserNameIsNotNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> new UserName(null));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"", "  ", "\t", "\n", "\r", "\f", "\b", "Jo", "Ty  ", "", "   Cy ", "Ed\t"})
  @DisplayName("Should throw InvalidUserNameException when User Name is empty or too short")
  void shouldValidateUserNameIsNotEmptyAndMinimumLength(final String userName) {
    // Act & Assert
    assertThrows(InvalidUserNameException.class, () -> new UserName(userName));
  }
}
