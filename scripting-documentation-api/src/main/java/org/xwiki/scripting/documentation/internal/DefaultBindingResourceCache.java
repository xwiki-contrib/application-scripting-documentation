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
import org.xwiki.scripting.documentation.BindingResource;
import org.xwiki.scripting.documentation.BindingResourceCache;

/**
 * Default implementation of the binding resource cache.
 *
 * @version $Id$
 */
@Component
@Singleton
public class DefaultBindingResourceCache implements BindingResourceCache, Initializable
{
    /** Default capacity for security cache. */
    private static final int DEFAULT_CAPACITY = 100;

    /** Cache manager to create the cache. */
    @Inject
    private CacheManager cacheManager;

    /** The cache instance. */
    private Cache<BindingResource> cache;

    private Cache<BindingResource> newCache() throws InitializationException
    {
        CacheConfiguration cacheConfig = new CacheConfiguration();
        cacheConfig.setConfigurationId("extension.scriptingdocumentation.resource.cache");
        LRUEvictionConfiguration lru = new LRUEvictionConfiguration();
        lru.setMaxEntries(DEFAULT_CAPACITY);
        cacheConfig.put(LRUEvictionConfiguration.CONFIGURATIONID, lru);
        try {
            return cacheManager.createNewCache(cacheConfig);
        } catch (Exception e) {
            throw new InitializationException(
                String.format("Unable to create the resource binding cache with a capacity of [%d] entries",
                    lru.getMaxEntries()), e);
        }
    }

    @Override
    public void initialize() throws InitializationException
    {
        cache = newCache();
    }

    @Override
    public BindingResource get(BindingResource resource)
    {
        String id = Integer.toString(resource.hashCode());
        BindingResource res = cache.get(id);
        if (res != null) {
            if (!res.equals(resource)) {
                throw new RuntimeException("Duplicate hash for different resource");
            }
            return res;
        }
        return null;
    }

    @Override
    public BindingResource add(BindingResource resource)
    {
        BindingResource res = get(resource);
        if (res != null) {
            return res;
        }

        String id = Integer.toString(resource.hashCode());
        cache.set(id, resource);
        return  resource;
    }
}
