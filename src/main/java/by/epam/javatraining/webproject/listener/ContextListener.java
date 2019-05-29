package by.epam.javatraining.webproject.listener;

import by.epam.javatraining.webproject.util.configuration.ConfigurationData;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getRootLogger();

    public void contextInitialized(ServletContextEvent event) {

        ServletContext servletContext = event.getServletContext();
        initLog4J(servletContext);
        initI18N(servletContext);
    }

    public void contextDestroyed(ServletContextEvent event) {

    }

    private void initI18N(ServletContext servletContext) {

        LOG.debug("I18N subsystem initialization started");

        String localesValue = servletContext.getInitParameter("locales");
        if (localesValue == null || localesValue.isEmpty()) {
            LOG.warn("'locales' init parameter is empty, the default encoding will be used");
        } else {

            List<String> locales = new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(localesValue);

            while (st.hasMoreTokens()) {
                String localeName = st.nextToken();
                locales.add(localeName);
            }

            LOG.debug("Application attribute set: 'locales' = " + locales);
            servletContext.setAttribute("locales", locales);
        }
        LOG.debug("I18N subsystem initialization finished");

    }



    /**
     * Initializes log4j framework.
     *
     * @param servletContext
     *            with <code>log4j.properties</code> file path, from which
     *            <code>log4j</code> will be configured
     */

    private void initLog4J(ServletContext servletContext) {

        try {
            DOMConfigurator.configure(servletContext.getRealPath(ConfigurationData.getString(ConfigurationData.LOG4J_XML)));
        } catch (Exception ex) {
            LOG.error("Cannot configure Log4j", ex);
        }
        LOG.debug("Log4j has been initialized");

    }
}
