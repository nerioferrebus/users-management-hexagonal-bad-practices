package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidUserIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Test: User ID Value Object")
class UserIdTest {

  @ParameterizedTest
  @ValueSource(strings = {" user123 ", "  user123  ", "user123\t"})
  @DisplayName("Should create User ID with trimmed value when input has whitespaces")
  void shouldCreateUserIdWithTrimmedValue(String input) {
    // Arrange
    final String correctUserId = "user123";
    
    // Act
    final UserId userId = new UserId(input);
    
    // Assert
    assertEquals(correctUserId, userId.value());
  }

  @Test
  @DisplayName("Should throw NullPointerException when User ID is null")
  void shouldThrowNullPointerExceptionWhenUserIdIsNull() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> new UserId(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b"})
  @DisplayName("Should throw InvalidUserIdException when User ID is empty or blank")
  void shouldThrowIllegalArgumentExceptionWhenUserIdIsEmpty(String input) {
    // Act & Assert
    assertThrows(InvalidUserIdException.class, () -> new UserId(input));
  }
}
