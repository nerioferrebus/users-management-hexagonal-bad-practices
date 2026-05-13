package com.jcaa.usersmanagement;

import com.jcaa.usersmanagement.infrastructure.config.DependencyContainer;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.UserManagementCli;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import java.util.Scanner;
import lombok.extern.java.Log;

@Log
public final class Main {

  public static void main(final String[] args) {
    log.info("Starting Users Management System...");
    run();
  }

  private static DependencyContainer buildContainer() {
    return new DependencyContainer();
  }

  private static ConsoleIO buildConsole(final Scanner scanner) {
    return new ConsoleIO(scanner, System.out);
  }

  private static UserManagementCli buildCli(final DependencyContainer container, final ConsoleIO console) {
    return new UserManagementCli(container.userController(), console);
  }

  private static void run() {
    final DependencyContainer container = buildContainer();
    try (final Scanner scanner = new Scanner(System.in)) {
      final ConsoleIO console = buildConsole(scanner);
      final UserManagementCli cli = buildCli(container, console);
      cli.start();
    }
  }
}