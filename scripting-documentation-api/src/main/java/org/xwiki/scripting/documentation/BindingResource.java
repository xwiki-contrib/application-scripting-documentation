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

import java.net.URL;

/**
 * A resource that provides one or more bindings (ie: a Maven package).
 *
 * @version $Id$
 */
public interface BindingResource
{
    /**
     * @return the identifier of the resource (ie: Maven coordinates).
     */
    String getId();

    /**
     * @return the identifier in the form of a path that could be used to build documentation paths.
     */
    String getPathId();

    /**
     * @return the base URL of the documentation for this resource.
     */
    String getDocBaseURL();

    /**
     * @return the URL of the documentation for this resource package.
     */
    URL getDocLink();
}
