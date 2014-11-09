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

import java.util.List;

import org.xwiki.component.annotation.Role;

/**
 * Find bindings provided to scripts.
 *
 * @version $Id$
 */
@Role
public interface ScriptBindingsFinder
{
    /**
     * @return a list of all bindings found.
     */
    List<Binding> find();
}
