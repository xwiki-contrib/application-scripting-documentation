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

import java.lang.reflect.Type;
import java.net.URL;

import org.reflections.util.ClasspathHelper;
import org.xwiki.scripting.documentation.BindingResource;
import org.xwiki.scripting.documentation.ResourceResolver;

/**
 * Base class for resource resolvers.
 *
 * @version $Id$
 */
public abstract class AbstractResourceResolver implements ResourceResolver
{
    @Override
    public BindingResource resolve(Type type)
    {
        if (type == null || !(type instanceof Class< ? >)) {
            return null;
        }

        Class< ? > klass = (Class< ? >) type;

        ClassLoader classLoader = ((Class< ? >) type).getClassLoader();
        if (classLoader == null) {
            return null;
        }

        URL url = ClasspathHelper.forClass(((Class< ? >) type), classLoader);
        if (url == null) {
            return null;
        }

        return internalResolve(classLoader, url);
    }

    @Override
    public BindingResource resolve(ClassLoader classLoader, URL url)
    {
        if (classLoader == null || url == null) {
            return null;
        }

        return internalResolve(classLoader, url);
    }

    protected abstract BindingResource internalResolve(ClassLoader classLoader, URL url);
}
