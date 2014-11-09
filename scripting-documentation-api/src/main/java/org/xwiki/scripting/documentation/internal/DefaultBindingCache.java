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
import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.BindingCache;
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

    private static int getBindingHashCode(String name, BindingResource resource)
    {
        int result = resource.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public Binding get(String name, BindingResource resource)
    {
        String id = Integer.toString(getBindingHashCode(name, resource));
        Binding binding = cache.get(id);
        if (binding != null) {
            if (!binding.getName().equals(name) && !binding.getResource().equals(resource)) {
                throw new RuntimeException("Duplicate hash for different binding");
            }
            return binding;
        }
        return null;
    }

    @Override
    public Binding add(Binding newBinding)
    {
        String id = Integer.toString(getBindingHashCode(newBinding.getName(), newBinding.getResource()));
        Binding binding = cache.get(id);
        if (binding != null) {
            return binding;
        }

        cache.set(id, newBinding);
        return newBinding;
    }
}

