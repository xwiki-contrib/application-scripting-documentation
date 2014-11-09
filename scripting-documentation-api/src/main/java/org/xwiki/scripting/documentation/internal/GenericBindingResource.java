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

import java.lang.reflect.Type;
import java.net.URL;

/**
 * A generic binding resource used in comparisons.
 *
 * @version $Id$
 */
public class GenericBindingResource extends AbstractBindingResource
{
    GenericBindingResource(Type type)
    {
        super(type);
    }

    @Override
    public String getId()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPathId()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getArchiveBrowserBaseURL()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavadocJarName()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public URL getDocLink()
    {
        throw new UnsupportedOperationException();
    }
}
