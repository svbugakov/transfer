package transfer.db;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceModule extends AbstractModule {

    @Override
    protected void configure() {
        Names.bindProperties(binder(), loadProperties());

        bind(DataSource.class).toProvider(H2DataSourceProvider.class).in(Scopes.SINGLETON);
        bind(MyService.class);
    }

    static class H2DataSourceProvider implements Provider<DataSource> {

        private final String url;
        private final String username;
        private final String password;

        public H2DataSourceProvider(@Named("url") final String url,
                                    @Named("username") final String username,
                                    @Named("password") final String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        @Override
        public DataSource get() {
            final JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL(url);
            dataSource.setUser(username);
            dataSource.setPassword(password);
            return dataSource;
        }
    }

    static class MyService {
        private final DataSource dataSource;

        @Inject
        public MyService(final DataSource dataSource) {
            this.dataSource = dataSource;
        }

        public void singleUnitOfWork() throws SQLException {

            Connection cn = null;

            try {
                cn = dataSource.getConnection();
                // Use the connection
            }  finally {
                try {
                    cn.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private Properties loadProperties() {
        // Load properties from appropriate place...
        // should contain definitions for:
        // url=...
        // username=...
        // password=...
        return new Properties();
    }
}