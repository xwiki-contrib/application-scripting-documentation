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
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.scripting.documentation.BindingResource;
import org.xwiki.scripting.documentation.BindingResourceCache;
import org.xwiki.scripting.documentation.ResourceResolver;

/**
 * Default implementation of the resource resolver.
 *
 * @version $Id$
 */
@Component
@Singleton
public class DefaultResourceResolver implements ResourceResolver
{
    @Inject
    private BindingResourceCache cache;

    @Inject
    private ComponentManager componentManager;

    @Override
    public BindingResource resolve(Type type)
    {
        if (type == null || !(type instanceof Class<?>)) {
            return null;
        }

        Class<?> klass = (Class<?>) type;
        if (klass.getClassLoader() == null) {
            return null;
        }

        BindingResource resource = cache.get(new GenericBindingResource(klass));
        if (resource != null) {
            return resource;
        }

        for (ResourceResolver resolver : getResolvers()) {
            resource = resolver.resolve(klass);
            if (resource != null) {
                return cache.add(resource);
            }
        }

        return null;
    }

    List<ResourceResolver> getResolvers()
    {
        try {
            List<ResourceResolver> resolvers = componentManager.getInstanceList(ResourceResolver.class);
            resolvers.remove(this);
            return resolvers;
        } catch (ComponentLookupException e) {
            return Collections.emptyList();
        }
    }
}
