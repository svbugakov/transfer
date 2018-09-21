package transfer.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import transfer.Acc;
import transfer.Account;

public class Configuration extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {

                bind(GuiceContainer.class);
                bind(Acc.class).to(Account.class);
                bindConstant().annotatedWith(Names.named("red")).to("Ivan good");

                System.out.println("Start guice....");
                ResourceConfig rc = new PackagesResourceConfig("transfer");
                for (Class<?> resource : rc.getClasses()) {
                    bind(resource);
                    System.out.println("dsd"+resource.getCanonicalName());
                }

                serve("/rest/*").with(GuiceContainer.class);
            }
        });
    }
}
