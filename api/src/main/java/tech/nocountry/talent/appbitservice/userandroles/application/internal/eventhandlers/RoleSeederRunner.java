package tech.nocountry.talent.appbitservice.userandroles.application.internal.eventhandlers;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.userandroles.application.internal.commandservices.usecases.SeedRolesCommandUseCase;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.commands.SeedRolesCommand;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class RoleSeederRunner implements CommandLineRunner {
    private final SeedRolesCommandUseCase seedRolesUseCase;
    private final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();
        flyway.migrate();

        var seedRolesCommand = new SeedRolesCommand();
        seedRolesUseCase.handle(seedRolesCommand);
    }
}