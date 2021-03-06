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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.component.manager.ComponentRepositoryException;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.extension.CoreExtension;
import org.xwiki.extension.repository.CoreExtensionRepository;
import org.xwiki.extension.version.Version;
import org.xwiki.extension.version.internal.DefaultVersion;
import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.ScriptBindingsFinder;

/**
 * Default implementation of the scripting binding finder, which ask all available binding finder.
 *
 * @version $Id$
 */
@Component
@Singleton
public class DefaultScriptBindingsFinder implements ScriptBindingsFinder, Initializable
{
    private static final Version VERSION_8_3 = new DefaultVersion("8.3");

    /**
     * Used to get find binding finders.
     */
    @Inject
    @Named("context")
    private Provider<ComponentManager> contextComponentManager;

    @Inject
    private ComponentManager thisComponentManager;

    @Inject
    private CoreExtensionRepository coreRepository;

    @Inject
    private Logger logger;

    @Override
    public void initialize() throws InitializationException
    {
        // TemplateScriptBindingsFinder does not really bring much value after 8.3 so we register it dynamically in
        // older distributions
        CoreExtension oldcore = this.coreRepository.getCoreExtension("org.xwiki.platform:xwiki-platform-oldcore");
        if (oldcore.getId().getVersion().compareTo(VERSION_8_3) < 0) {
            try {
                this.thisComponentManager.registerComponent(TemplateScriptBindingsFinder.getComponentDescriptor());
            } catch (ComponentRepositoryException e) {
                this.logger.error("Failed to register component []", TemplateScriptBindingsFinder.class, e);
            }
        }
    }

    @Override
    public List<Binding> find()
    {
        List<Binding> bindings = new ArrayList<Binding>();
        for (ScriptBindingsFinder finder : getBindingsFinder()) {
            bindings.addAll(finder.find());
        }
        return bindings;
    }

    @Override
    public List<Binding> find(BindingKind kind)
    {
        ScriptBindingsFinder finder = getBindingsFinder(kind);
        if (finder == null) {
            return Collections.emptyList();
        }
        return finder.find();
    }

    @Override
    public Binding find(String name)
    {
        List<Binding> bindings = new ArrayList<Binding>();
        for (ScriptBindingsFinder finder : getBindingsFinder()) {
            Binding binding = finder.find(name);
            if (binding != null) {
                return binding;
            }
        }
        return null;
    }

    @Override
    public Binding find(BindingKind kind, String name)
    {
        ScriptBindingsFinder finder = getBindingsFinder(kind);
        if (finder != null) {
            Binding binding = finder.find(name);
            if (binding != null) {
                return binding;
            }
        }
        return null;
    }

    @Override
    public List<Binding> find(Pattern regex)
    {
        List<Binding> bindings = new ArrayList<Binding>();
        for (ScriptBindingsFinder finder : getBindingsFinder()) {
            bindings.addAll(finder.find(regex));
        }
        return bindings;
    }

    @Override
    public List<Binding> find(BindingKind kind, Pattern regex)
    {
        ScriptBindingsFinder finder = getBindingsFinder(kind);
        if (finder == null) {
            return Collections.emptyList();
        }
        return finder.find(regex);
    }

    /**
     * @return all bindings finders.
     */
    private List<ScriptBindingsFinder> getBindingsFinder()
    {
        try {
            List<ScriptBindingsFinder> finders =
                contextComponentManager.get().getInstanceList((Type) ScriptBindingsFinder.class);
            finders.remove(this);
            return finders;
        } catch (ComponentLookupException e) {
            return Collections.<ScriptBindingsFinder>emptyList();
        }
    }

    private ScriptBindingsFinder getBindingsFinder(BindingKind kind)
    {
        try {
            return contextComponentManager.get().getInstance((Type) ScriptBindingsFinder.class, kind.toString());
        } catch (ComponentLookupException e) {
            return null;
        }
    }
}
