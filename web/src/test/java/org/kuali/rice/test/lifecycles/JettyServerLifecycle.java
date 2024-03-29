/**
 * Copyright 2004-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.test.lifecycles;

import java.net.BindException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.test.launch.JettyLauncher;


/**
 * A lifecycle for running a jetty web server.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JettyServerLifecycle implements Lifecycle {
    private static final Logger LOG = Logger.getLogger(JettyServerLifecycle.class);

    private static final HashMap<Integer, Config> WEBAPP_CONFIGS = new HashMap<Integer, Config>();

    public static Config getWebappConfig(int port) {
        return WEBAPP_CONFIGS.get(port);
    }

    /**
     * Enum for dealing with the webapp's Config
     */
    public static enum ConfigMode {
        /**
         * Do nothing
         */
        NONE,
        /**
         * Override the Config for the context class loader
         */
        OVERRIDE,
        /**
         * Merge the webapp's Config into the existing context class loader config
         */
        MERGE
    }

    /**
     * By default we set the JettyServer to test mode
     */
    private boolean testMode = true;
    private boolean started;
    private ConfigMode configMode = ConfigMode.OVERRIDE;
    private boolean addWebappResourceLoaders = true;


	protected JettyLauncher jettyServer;

	public JettyServerLifecycle() {
		this(8080, null);
	}

	public JettyServerLifecycle(int port) {
		this(port, null, null);
	}

	public JettyServerLifecycle(int port, String contextName) {
		this(port, contextName, null);
	}

	public JettyServerLifecycle(int port, String contextName, String relativeWebappRoot) {
		jettyServer = new JettyLauncher(port, contextName, relativeWebappRoot);
        jettyServer.setFailOnContextFailure(true);
		jettyServer.setTestMode(testMode);
	}

    public void setTestMode(boolean t) {
        this.testMode = t;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public ConfigMode getConfigMode() {
        return this.configMode;
    }

    public void setConfigMode(ConfigMode configMode) {
        this.configMode = configMode;
    }

    public boolean isAddWebappResourceLoaders() {
        return this.addWebappResourceLoaders;
    }

    public void setAddWebappResourceLoaders(boolean addWebappResourceLoaders) {
        this.addWebappResourceLoaders = addWebappResourceLoaders;
    }

	public boolean isStarted() {
		return started;
	}

	public void start() throws Exception {
	    try {
	        jettyServer.start();

	    } catch (RuntimeException re) {
	        // add some handling to make port conflicts more easily identified
	        if (RiceUtilities.findExceptionInStack(re, BindException.class) != null) {
	            LOG.error("JettyServerLifecycle encountered BindException on port: " + jettyServer.getPort() + "; check logs for test junit.framework.Assert.failures or and the config for duplicate port specifications.");
	        }
	        throw re;
	    }

	    ClassLoader webappClassLoader = jettyServer.getContext().getClassLoader();
	    if (addWebappResourceLoaders) {
	        ResourceLoader rl = GlobalResourceLoader.getResourceLoader(webappClassLoader);
            if (rl == null) {
                throw new RuntimeException("Could not find resource loader for workflow test harness web app for: " + webappClassLoader);
            }
            //GlobalResourceLoader.addResourceLoaderFirst();
            GlobalResourceLoader.addResourceLoader(rl);
	    }
	    //org.kuali.rice.core.api.config.property.Config webappConfig = ConfigContext.getCurrentContextConfig();
        org.kuali.rice.core.api.config.property.Config webappConfig = ConfigContext.getConfig(webappClassLoader);
	    WEBAPP_CONFIGS.put(new Integer(jettyServer.getPort()), webappConfig);
	    if (ConfigMode.OVERRIDE == configMode) {
            // this overrides the test harness classloader config with the webapp's config...


            ConfigContext.overrideConfig(Thread.currentThread().getContextClassLoader(), webappConfig);
        } else if (ConfigMode.MERGE == configMode) {
            Config curCtxConfig = ConfigContext.getCurrentContextConfig();
            if (webappConfig != null) {
                curCtxConfig.putProperties(webappConfig.getProperties());
                curCtxConfig.putObjects(webappConfig.getObjects());
            }
        }

		started = true;
	}

	public void stop() throws Exception {
	    LOG.info("Shutting down jetty: " + jettyServer);
	    try {
	    	if (jettyServer != null && jettyServer.isStarted()) {
	    		jettyServer.stop();
	    		WEBAPP_CONFIGS.remove(jettyServer.getPort());
	    	}
	    } catch (Exception e) {
	        LOG.error("Error shutting down Jetty " + jettyServer.getContextName(), e);
	    }
		started = false;
	}
}
