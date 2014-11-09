/*
 * Copyright 2010-2014 SOFTEC sa. All rights reserved.
 *
 * This software is copyrighted. The software may not be copied,
 * reproduced, translated, or reduced to any electronic medium
 * or machine-readable form without the prior written consent of
 * SOFTEC sa.
 *
 * THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO SOFTEC S.A.
 * AND MAY NOT BE REPRODUCED, PUBLISHED, OR DISCLOSED TO OTHERS
 * WITHOUT COMPANY AUTHORIZATION.
 */

package org.xwiki.scripting.documentation.internal;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.plugin.XWikiPluginInterface;
import com.xpn.xwiki.plugin.XWikiPluginManager;

/**
 * Abstract base class for scripting binding finder for plugins.
 *
 * @version $Id$
 */
public abstract class AbstractPluginScriptBindingsFinder extends AbstractScriptBindingsFinder
{
    /**
     * Used to get find script services.
     */
    @Inject
    protected Provider<XWikiContext> contextProvider;

    @Inject
    private Logger logger;

    protected abstract Class<?> getPluginClass(XWikiPluginInterface plugin);

    /**
     * @return the map of classes bindings in all velocity contexts
     */
    protected Map<String, Class<?>> getBindings()
    {
        Map<String, Class<?>> bindings = new HashMap<String, Class<?>>();
        XWikiPluginManager pluginManager = contextProvider.get().getWiki().getPluginManager();

        for (String name : pluginManager.getPlugins()) {
            Class<?> plugin = getPluginClass(pluginManager.getPlugin(name));
            if (plugin != null) {
                bindings.put(name, plugin);
            } else {
                logger.debug("Plugin class not found: {}", name);
            }
        }

        return bindings;
    }
}
