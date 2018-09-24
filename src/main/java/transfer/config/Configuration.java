package transfer.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import transfer.model.Account;
import transfer.db.DaoAccount;
import transfer.db.DaoAccountImpl;
import transfer.db.H2DataSourceProvider;


import javax.sql.DataSource;
import java.util.Properties;

public class Configuration extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {

                bind(GuiceContainer.class);
                bind(Account.class);
                bind(DaoAccount.class).to(DaoAccountImpl.class);

                Names.bindProperties(binder(), loadProperties());

                bind(DataSource.class).toProvider(H2DataSourceProvider.class)
                        .in(Scopes.SINGLETON);

                ResourceConfig rc = new PackagesResourceConfig("transfer");
                for (Class<?> resource : rc.getClasses()) {
                    bind(resource);
                }

                serve("/rest/*").with(GuiceContainer.class);
            }
        });
    }

    private Properties loadProperties() {
        Properties prop = new Properties();
        prop.setProperty("url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;" +
                "INIT=RUNSCRIPT FROM 'create.sql';LOCK_TIMEOUT=200000");
        prop.setProperty("username", "sa");
        prop.setProperty("password", "");
        return prop;
    }
}
