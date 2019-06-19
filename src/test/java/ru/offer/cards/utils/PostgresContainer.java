package ru.offer.cards.utils;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {
    private static String POSRGRES_VERSION = "postgres:11";
    private static PostgresContainer container;

    public PostgresContainer() {
        super(POSRGRES_VERSION);
    }

    public static PostgresContainer getInstance() {
        if (container == null) {
            container = new PostgresContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        // Do not stop container here. Allow JVM stop it when tests finished.
    }
}
