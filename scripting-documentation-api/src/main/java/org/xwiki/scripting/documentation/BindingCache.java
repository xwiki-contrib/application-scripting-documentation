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

/**
 * Please comment here
 *
 * @version $Id$
 */

import org.xwiki.component.annotation.Role;

/**
 * The binding cache, that store already found bindings to reuse them between requests.
 *
 * @version $Id$
 */
@Role
public interface BindingCache
{
    /**
     * Check if the cache contains a binding matching the given fullname.
     *
     * @param name a binding name.
     * @param resource the resource providing the binding.
     * @return the matching binding in the cache, or null if that binding is not in the cache.
     */
    Binding get(String name, BindingResource resource);

    /**
     * Add the given binding to the cache if not already available in the cache.
     *
     * @param binding the binding to be added.
     * @return the binding from the cache.
     */
    Binding add(Binding binding);
}
