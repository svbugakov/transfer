package transfer.db;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class H2DataSourceProvider implements Provider<DataSource> {
    private final String url;
    private final String username;
    private final String password;
    private Logger logger = LoggerFactory.getLogger(H2DataSourceProvider.class);

    @Inject
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
        logger.debug("create datasource" +
                "url={}, username={}", url, username);
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
