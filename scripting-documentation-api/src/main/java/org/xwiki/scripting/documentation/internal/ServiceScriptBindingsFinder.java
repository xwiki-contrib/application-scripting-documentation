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
    protected Map<String, Class<?>> getBindings()
    {
        Map<String, Class<?>> bindings = new HashMap<String, Class<?>>();

        for (ComponentDescriptor<?> descriptor
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
