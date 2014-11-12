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

import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.BindingResource;

/**
 * Please comment here
 *
 * @version $Id$
 */
public class GenericBindings extends AbstractBinding
{
    private final URL docLink;

    GenericBindings(Type type, String name, String fullName, BindingKind kind, boolean isInternal, boolean isDeprecated,
        String description)
    {
        this(type, name, fullName, kind, isInternal, isDeprecated, description, null);
    }

    GenericBindings(Type type, String name, String fullName, BindingKind kind, boolean isInternal, boolean isDeprecated,
        String description, URL docLink)
    {
        super(type, name, fullName, kind, isInternal, isDeprecated, description);
        this.docLink = docLink;
    }

    @Override
    public URL getDocLink()
    {
        return docLink;
    }

    @Override
    public BindingResource getResource()
    {
        return null;
    }
}
