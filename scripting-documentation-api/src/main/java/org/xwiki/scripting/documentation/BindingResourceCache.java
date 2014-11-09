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

package org.xwiki.scripting.documentation;

import org.xwiki.component.annotation.Role;

/**
 * The binding resource cache, that store already found binding resources to share them between bindings.
 *
 * @version $Id$
 */
@Role
public interface BindingResourceCache
{
    /**
     * Check if the cache contains a resource matching the Id of the given resource.
     *
     * @param resource a resource to be matched in the cache.
     * @return the matching resource in the cache, or null if that resource is not in the cache.
     */
    BindingResource get(BindingResource resource);

    /**
     * Add the given resource to the cache if not already available in the cache.
     *
     * @param resource the resource to be added.
     * @return the resource from the cache.
     */
    BindingResource add(BindingResource resource);
}
