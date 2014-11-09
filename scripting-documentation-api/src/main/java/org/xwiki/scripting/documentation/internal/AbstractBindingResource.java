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

import org.reflections.util.ClasspathHelper;
import org.xwiki.scripting.documentation.BindingResource;

/**
 * Abstract base class for java binding resource.
 *
 * @version $Id$
 */
public abstract class AbstractBindingResource implements BindingResource
{
    protected final ClassLoader classLoader;

    protected final URL url;

    AbstractBindingResource(Type type)
    {
        Class<?> klass = (Class<?>) type;
        this.classLoader = klass.getClassLoader();
        this.url = ClasspathHelper.forClass(klass, this.classLoader);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof AbstractBindingResource)) {
            return false;
        }

        return classLoader.equals(((AbstractBindingResource) o).classLoader)
            && url.equals(((AbstractBindingResource) o).url);
    }

    @Override
    public int hashCode()
    {
        int result = classLoader.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String getDocBaseURL()
    {
        return getArchiveBrowserBaseURL() + getJavadocPathName() + "/!/";
    }

    /**
     * @return the path for accessing the javadoc archive of this resource on site providing access to it.
     */
    public String getJavadocPathName()
    {
        return getPathId() + '/' + getJavadocJarName();
    }

    /**
     * @return the base URL of the site providing access to the javadoc archive of this resource.
     */
    public abstract String getArchiveBrowserBaseURL();

    /**
     * @return the name of the javadoc archive for this resource.
     */
    public abstract String getJavadocJarName();
}
