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
