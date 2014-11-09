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
            String key = entry.getKey();
            Class<?> klass = entry.getValue();

            BindingResource resource = resourceResolver.resolve(klass);
            if (resource != null && resource instanceof ExtensionBindingResource) {
                Binding binding = bindingCache.get(key, resource);
                if (binding == null) {
                    binding =
                        bindingCache.add(new ExtensionBinding(klass, key, getFullName(key), getType(), null, resource));
                }
                bindings.add(binding);
            }
        }

        return bindings;
    }
}
