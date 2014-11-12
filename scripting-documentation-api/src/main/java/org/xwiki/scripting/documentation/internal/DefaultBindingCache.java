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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.xwiki.cache.Cache;
import org.xwiki.cache.CacheManager;
import org.xwiki.cache.config.CacheConfiguration;
import org.xwiki.cache.eviction.LRUEvictionConfiguration;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;
import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.BindingCache;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.BindingResource;

/**
 * Default implementation of the binding cache.
 *
 * @version $Id$
 */
@Component
@Singleton
public class DefaultBindingCache implements BindingCache, Initializable
{
    /** Default capacity for security cache. */
    private static final int DEFAULT_CAPACITY = 500;

    /** Cache manager to create the cache. */
    @Inject
    private CacheManager cacheManager;

    /** The cache instance. */
    private Cache<Binding> cache;

    private Cache<Binding> newCache() throws InitializationException
    {
        CacheConfiguration cacheConfig = new CacheConfiguration();
        cacheConfig.setConfigurationId("extension.scriptingdocumentation.binding.cache");
        LRUEvictionConfiguration lru = new LRUEvictionConfiguration();
        lru.setMaxEntries(DEFAULT_CAPACITY);
        cacheConfig.put(LRUEvictionConfiguration.CONFIGURATIONID, lru);
        try {
            return cacheManager.createNewCache(cacheConfig);
        } catch (Exception e) {
            throw new InitializationException(
                String.format("Unable to create the binding cache with a capacity of [%d] entries",
                    lru.getMaxEntries()), e);
        }
    }

    @Override
    public void initialize() throws InitializationException
    {
        cache = newCache();
    }

    private static int getBindingHashCode(String name, BindingResource resource, BindingKind kind)
    {
        int result = (resource != null) ? resource.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + kind.hashCode();
        return result;
    }

    @Override
    public Binding get(String fullName, BindingResource resource, BindingKind kind)
    {
        String id = Integer.toString(getBindingHashCode(fullName, resource, kind));
        Binding binding = cache.get(id);
        if (binding != null) {
            if (!binding.getName().equals(fullName)
                && ((binding.getResource() == null && resource != null)
                  || binding.getResource() != null && !binding.getResource().equals(resource))
                && !binding.getKind().equals(kind)) {
                throw new RuntimeException("Duplicate hash for different binding");
            }
            return binding;
        }
        return null;
    }

    @Override
    public Binding add(Binding newBinding)
    {
        int hash = newBinding.hashCode();
        String id = Integer.toString(hash);
        Binding binding = cache.get(id);
        if (binding != null) {
            return binding;
        }

        int computedHash = getBindingHashCode(newBinding.getFullName(), newBinding.getResource(), newBinding.getKind());
        if (hash != computedHash) {
            throw new RuntimeException("Binding hash does not match computed cache hash.");
        }

        cache.set(id, newBinding);
        return newBinding;
    }
}

