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
