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

import java.lang.reflect.Type;

import org.xwiki.component.annotation.Role;

/**
 * Find the resource providing a given binding.
 *
 * @version $Id$
 */
@Role
public interface ResourceResolver
{
    /**
     * Find the resource providing a given binding.
     *
     * @param type the type to search for.
     * @return the binding resource, or null if none where found.
     */
    BindingResource resolve(Type type);
}
