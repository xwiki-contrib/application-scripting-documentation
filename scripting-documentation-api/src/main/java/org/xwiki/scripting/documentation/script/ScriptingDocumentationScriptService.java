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

package org.xwiki.scripting.documentation.script;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.script.service.ScriptService;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.ScriptBindingsFinder;

/**
 * Make it easy to access scripting documentation finders from scripts.
 *
 * @version $Id$
 */
@Component
@Named("scriptdoc")
@Singleton
public class ScriptingDocumentationScriptService implements ScriptService
{
    /**
     * Used to get find binding finders.
     */
    @Inject
    @Named("context")
    private Provider<ComponentManager> componentManager;

    /**
     * @return a list of available kinds of bindings finders.
     */
    public Collection<BindingKind> getAvailableBindingKinds()
    {
        Set<String> kinds = getBindingsFinderKinds();
        Collection<BindingKind> result = new ArrayList<BindingKind>(kinds.size());
        for (String kind : kinds) {
            if (!"default".equals(kind)) {
                result.add(new BindingKind(kind));
            }
        }
        return result;
    }

    /**
     * @param kind the kind.
     * @return a binding kind corresponding to kind.
     */
    public BindingKind getBindingKind(String kind)
    {
        return new BindingKind(kind);
    }

    /**
     * @return the default bindings finder.
     */
    public ScriptBindingsFinder getBindingsFinder() {
        return getBindingsFinder(null);
    }

    /**
     * @param kind the kind of the bindings finder to return.
     * @return the bindings finder appropriate for the given kind, null if none where found.
     */
    public ScriptBindingsFinder getBindingsFinder(BindingKind kind)
    {
        String hint = null;
        if (kind != null) {
            hint = kind.toString();
        }
        try {
            return componentManager.get().getInstance((Type) ScriptBindingsFinder.class, hint);
        } catch (ComponentLookupException e) {
            return null;
        }
    }

    /**
     * @return all bindings finder kinds.
     */
    private Set<String> getBindingsFinderKinds()
    {
        try {
            return componentManager.get().getInstanceMap(ScriptBindingsFinder.class).keySet();
        } catch (ComponentLookupException e) {
            return Collections.<String>emptySet();
        }
    }


}
