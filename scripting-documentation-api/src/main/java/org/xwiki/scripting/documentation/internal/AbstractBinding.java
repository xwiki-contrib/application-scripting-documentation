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

import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.BindingKind;

/**
 * Abstract base class for java bindings.
 *
 * @version $Id$
 */
public abstract class AbstractBinding implements Binding
{
    protected static final String HTML_EXT = ".html";

    private final Type type;
    private final String name;
    private final String fullName;
    private final BindingKind kind;
    private final boolean isInternal;
    private final boolean isDeprecated;
    private final String description;

    AbstractBinding(Type type, String name, String fullName, BindingKind kind, boolean isInternal, boolean isDeprecated,
        String description)
    {
        this.type = type;
        this.name = name;
        this.fullName = fullName;
        this.kind = kind;

        this.isInternal = isInternal;
        this.isDeprecated = isDeprecated;

        this.description = description;
    }

    /**
     * @return the class of this binding.
     */
    public Class<?> getBindingClass()
    {
        return (type instanceof Class<?>) ? (Class<?>) type : null;
    }

    /**
     * @return return the name of the class in the form of a path.
     */
    public String getClassPath()
    {
        return getBindingClass().getName().replace('.', '/');
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getFullName()
    {
        return fullName;
    }

    @Override
    public Type getType()
    {
        return type;
    }

    @Override
    public BindingKind getKind()
    {
        return kind;
    }

    @Override
    public boolean isInternal()
    {
        return isInternal;
    }

    @Override
    public boolean isDeprecated()
    {
        return isDeprecated;
    }

    @Override
    public boolean isLegacy()
    {
        return false;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public boolean equals(Object o)
    {
        return this == o
            || o instanceof AbstractBinding
            && kind.equals(((AbstractBinding) o).kind)
            && name.equals(((AbstractBinding) o).name);
    }

    @Override
    public int hashCode()
    {
        int result = (getType() != null) ? getType().hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + kind.hashCode();
        return result;
    }
}
