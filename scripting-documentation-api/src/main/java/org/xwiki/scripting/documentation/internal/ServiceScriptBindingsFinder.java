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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.descriptor.ComponentDescriptor;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.script.service.ScriptService;
import org.xwiki.scripting.documentation.BindingKind;

/**
 * Search for script services.
 *
 * @version $Id$
 */
@Component
@Named("service")
@Singleton
public class ServiceScriptBindingsFinder extends AbstractScriptBindingsFinder
{
    /**
     * Used to get find script services.
     */
    @Inject
    @Named("context")
    private Provider<ComponentManager> componentManager;

    /**
     * @return the map of classes bindings in all velocity contexts
     */
    protected Map<String, Class< ? >> getBindings()
    {
        Map<String, Class< ? >> bindings = new HashMap<String, Class<?>>();

        for (ComponentDescriptor< ? > descriptor
            : componentManager.get().getComponentDescriptorList((Type) ScriptService.class)) {
            bindings.put(descriptor.getRoleHint(), descriptor.getImplementation());
        }

        return bindings;
    }

    @Override
    protected String getFullName(String name)
    {
        return "services." + name;
    }

    @Override
    protected BindingKind getType()
    {
        return BindingKind.SCRIPT_SERVICE;
    }
}
