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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.BindingCache;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.BindingResource;
import org.xwiki.scripting.documentation.ResourceResolver;
import org.xwiki.scripting.documentation.ScriptBindingsFinder;

/**
 * Abstract base class for scripting binding finder.
 *
 * @version $Id$
 */
public abstract class AbstractScriptBindingsFinder implements ScriptBindingsFinder
{
    @Inject
    private ResourceResolver resourceResolver;

    @Inject
    private BindingCache bindingCache;

    protected abstract Map<String, Class<?>> getBindings();

    protected abstract String getFullName(String name);

    protected abstract BindingKind getType();

    @Override
    public List<Binding> find()
    {
        List<Binding> bindings = new ArrayList<Binding>();
        for (Map.Entry<String, Class<?>> entry : getBindings().entrySet()) {
            String name = entry.getKey();
            String fullName = getFullName(name);
            Class<?> klass = entry.getValue();

            BindingResource resource = resourceResolver.resolve(klass);
            if (resource != null && resource instanceof ExtensionBindingResource) {
                Binding binding = bindingCache.get(fullName, resource);
                if (binding == null) {
                    binding =
                        bindingCache.add(new ExtensionBinding(klass, name, fullName, getType(), null, resource));
                }
                bindings.add(binding);
            }
        }

        return bindings;
    }
}
