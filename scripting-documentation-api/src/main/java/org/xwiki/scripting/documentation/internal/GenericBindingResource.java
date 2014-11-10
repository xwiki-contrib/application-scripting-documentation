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
