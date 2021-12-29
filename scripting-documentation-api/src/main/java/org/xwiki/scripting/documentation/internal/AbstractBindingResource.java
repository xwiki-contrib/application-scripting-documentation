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

import java.net.URL;

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

    AbstractBindingResource(ClassLoader classLoader, URL url)
    {
        this.classLoader = classLoader;
        this.url = url;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractBindingResource)) {
            return false;
        }

        return classLoader.equals(((AbstractBindingResource) o).classLoader)
            && url.toString().equals(((AbstractBindingResource) o).url.toString());
    }

    @Override
    public int hashCode()
    {
        int result = classLoader.hashCode();
        result = 31 * result + url.toString().hashCode();
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
