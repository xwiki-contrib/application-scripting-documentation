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

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.scripting.documentation.BindingKind;

import com.xpn.xwiki.api.Api;
import com.xpn.xwiki.plugin.XWikiPluginInterface;

/**
 * Search for plugins.
 *
 * @version $Id$
 */
@Component
@Named("plugin")
@Singleton
public class PluginScriptBindingsFinder extends AbstractPluginScriptBindingsFinder
{
    @Override
    protected Class<?> getPluginClass(XWikiPluginInterface plugin)
    {
        if (plugin != null) {
            Api pluginApi = plugin.getPluginApi(plugin, contextProvider.get());
            if (pluginApi != null) {
                return pluginApi.getClass();
            }
        }
        return null;
    }

    @Override
    protected String getFullName(String name)
    {
        return "xwiki." + name;
    }

    @Override
    protected BindingKind getType()
    {
        return BindingKind.PLUGIN;
    }
}
