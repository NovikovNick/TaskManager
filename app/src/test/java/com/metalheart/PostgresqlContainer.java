package com.metalheart;

import java.sql.Statement;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.junit.Ignore;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.flywaydb.core.api.callback.Event.BEFORE_MIGRATE;

@Ignore
@Slf4j
public class PostgresqlContainer extends PostgreSQLContainer<PostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:11.4";
    private static PostgresqlContainer container;
    private static boolean migrationRun = false;

    private PostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgresqlContainer getInstance() {
        if (container == null) {
            container = new PostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
        System.setProperty("DB_DRIVER", container.getDriverClassName());

        if (!migrationRun) {

            DataSource dataSource = DataSourceBuilder.create()
                .url(container.getJdbcUrl())
                .username(container.getUsername())
                .password(container.getPassword())
                .driverClassName(container.getDriverClassName())
                .build();


            Flyway fly = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:integration.migration", "classpath:db.migration")
                .callbacks(new Callback() {
                    @Override
                    public boolean supports(Event event, Context context) {
                        return event == BEFORE_MIGRATE;
                    }

                    @Override
                    public boolean canHandleInTransaction(Event event, Context context) {
                        return false;
                    }

                    @Override
                    public void handle(Event event, Context context) {
                        initializeDB(context);
                    }
                }).load();
            fly.migrate();
            migrationRun = true;
        }
    }

    private void initializeDB(Context context) {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("CREATE USER metalheart createdb");
        } catch (Exception e) {
            log.error("Error on executing after clean statements", e);
        }
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }

}
