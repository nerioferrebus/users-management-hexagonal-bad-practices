package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
// VIOLACIÓN Regla 9 (Hexagonal): el dominio importa una clase de infraestructura.
// Las dependencias siempre deben ir hacia el centro — nunca desde el dominio hacia afuera.
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.Value;

@Value
public class UserModel {

  UserId id;
  UserName name;
  UserEmail email;
  UserPassword password;
  UserRole role;
  UserStatus status;

  public static UserModel create(
      final UserId id,
      final UserName name,
      final UserEmail email,
      final UserPassword password,
      final UserRole role) {
    return new UserModel(id, name, email, password, role, UserStatus.PENDING);
  }

  public UserModel activate() {
    return new UserModel(id, name, email, password, role, UserStatus.ACTIVE);
  }

  public UserModel deactivate() {
    return new UserModel(id, name, email, password, role, UserStatus.INACTIVE);
  }

  // VIOLACIÓN Regla 9 (Hexagonal): método de conversión a entidad de infraestructura dentro del dominio.
  // El dominio NO debe saber nada sobre cómo se persisten sus datos.
  public UserEntity toEntity() {
    return new UserEntity(
        id.value(),
        name.value(),
        email.value(),
        password.value(),
        role.name(),
        status.name(),
        null,
        null);
  }
}
