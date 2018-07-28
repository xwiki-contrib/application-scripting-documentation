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
import javax.script.ScriptContext;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.xwiki.component.descriptor.ComponentDescriptor;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;
import org.xwiki.component.descriptor.DefaultComponentDependency;
import org.xwiki.component.descriptor.DefaultComponentDescriptor;
import org.xwiki.localization.LocalizationManager;
import org.xwiki.rendering.renderer.PrintRenderer;
import org.xwiki.script.ScriptContextManager;
import org.xwiki.scripting.documentation.BindingCache;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.ResourceResolver;
import org.xwiki.scripting.documentation.ScriptBindingsFinder;
import org.xwiki.velocity.VelocityContextFactory;
import org.xwiki.velocity.VelocityManager;

/**
 * Search for bindings in the velocity context.
 *
 * @version $Id$
 */
public class TemplateScriptBindingsFinder extends AbstractVelocityScriptBindingFinder
{
    /**
     * Used to get the current Velocity Context from which we retrieve the list of bound variables.
     */
    @Inject
    private VelocityManager velocityManager;

    /**
     * Used to get the Script Context from which we retrieve the list of bindings to exclude from velocity context.
     */
    @Inject
    private ScriptContextManager scriptContextManager;

    /**
     * @return the descriptor used to register this component
     * @since 1.2
     */
    static ComponentDescriptor<ScriptBindingsFinder> getComponentDescriptor()
    {
        DefaultComponentDescriptor<ScriptBindingsFinder> descriptor =
            new DefaultComponentDescriptor<ScriptBindingsFinder>();

        descriptor.setRoleType(ScriptBindingsFinder.class);
        descriptor.setRoleHint("template");
        descriptor.setImplementation(TemplateScriptBindingsFinder.class);
        descriptor.setInstantiationStrategy(ComponentInstantiationStrategy.SINGLETON);

        DefaultComponentDependency<VelocityManager> velocityManagerDependency =
            new DefaultComponentDependency<VelocityManager>();
        velocityManagerDependency.setName("velocityManager");
        velocityManagerDependency.setRoleType(VelocityManager.class);
        descriptor.addComponentDependency(velocityManagerDependency);

        DefaultComponentDependency<ScriptContextManager> scriptContextManagerDependency =
            new DefaultComponentDependency<ScriptContextManager>();
        velocityManagerDependency.setName("scriptContextManager");
        velocityManagerDependency.setRoleType(ScriptContextManager.class);
        descriptor.addComponentDependency(scriptContextManagerDependency);

        DefaultComponentDependency<VelocityContextFactory> velocityContextFactoryDependency =
            new DefaultComponentDependency<VelocityContextFactory>();
        velocityManagerDependency.setName("velocityContextFactory");
        velocityManagerDependency.setRoleType(VelocityContextFactory.class);
        descriptor.addComponentDependency(velocityContextFactoryDependency);

        DefaultComponentDependency<Logger> loggerDependency = new DefaultComponentDependency<Logger>();
        velocityManagerDependency.setName("logger");
        velocityManagerDependency.setRoleType(Logger.class);
        descriptor.addComponentDependency(loggerDependency);

        DefaultComponentDependency<BindingCache> bindingCacheDependency =
            new DefaultComponentDependency<BindingCache>();
        velocityManagerDependency.setName("bindingCache");
        velocityManagerDependency.setRoleType(BindingCache.class);
        descriptor.addComponentDependency(bindingCacheDependency);

        DefaultComponentDependency<ResourceResolver> resourceResolverDependency =
            new DefaultComponentDependency<ResourceResolver>();
        velocityManagerDependency.setName("resourceResolver");
        velocityManagerDependency.setRoleType(ResourceResolver.class);
        descriptor.addComponentDependency(resourceResolverDependency);

        DefaultComponentDependency<PrintRenderer> plainTextRendererDependency =
            new DefaultComponentDependency<PrintRenderer>();
        velocityManagerDependency.setName("plainTextRenderer");
        velocityManagerDependency.setRoleHint("plain/1.0");
        velocityManagerDependency.setRoleType(PrintRenderer.class);
        descriptor.addComponentDependency(plainTextRendererDependency);

        DefaultComponentDependency<LocalizationManager> localizationDependency =
            new DefaultComponentDependency<LocalizationManager>();
        velocityManagerDependency.setName("localization");
        velocityManagerDependency.setRoleType(LocalizationManager.class);
        descriptor.addComponentDependency(localizationDependency);

        return descriptor;
    }

    /**
     * @return the map of classes bindings in all velocity contexts
     */
    protected Map<String, Class<?>> getBindings()
    {
        VelocityContext templateContext = this.velocityManager.getVelocityContext();
        ScriptContext scriptContext = this.scriptContextManager.getScriptContext();
        VelocityContext vContext = getVelocityContext();
        Map<String, Class<?>> scriptBindings = new HashMap<String, Class<?>>();

        if (scriptContext != null) {
            for (Map.Entry<String, Object> entry : scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).entrySet()) {
                scriptBindings.put(entry.getKey(), entry.getValue().getClass());
            }
        }

        if (vContext != null) {
            while (vContext != null) {
                addAllBinding(vContext, scriptBindings);
                vContext = (VelocityContext) vContext.getChainedContext();
            }
        }

        Map<String, Class<?>> bindings = new HashMap<String, Class<?>>();
        while (templateContext != null) {
            addAllBinding(templateContext, scriptBindings, bindings);
            templateContext = (VelocityContext) templateContext.getChainedContext();
        }

        return bindings;
    }

    @Override
    protected String getFullName(String name)
    {
        return name;
    }

    @Override
    protected BindingKind getType()
    {
        return BindingKind.TEMPLATE;
    }

    private static void addAllBinding(VelocityContext vcontext, Map<String, Class<?>> scriptBindings,
        Map<String, Class<?>> bindings)
    {
        for (Object key : vcontext.getKeys()) {
            String name = key.toString();
            Class<?> klass = vcontext.get(name).getClass();
            Class<?> scriptBinding = scriptBindings.get(name);
            if (scriptBinding == null || !klass.equals(scriptBinding)) {
                bindings.put(name, klass);
            }
        }
    }
}
