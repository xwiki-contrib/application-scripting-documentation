/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
    @Override
    protected Map<String, Class<?>> getBindings()
    {
        Map<String, Class<?>> bindings = new HashMap<>();
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
