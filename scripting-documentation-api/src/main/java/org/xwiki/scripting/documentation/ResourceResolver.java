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
