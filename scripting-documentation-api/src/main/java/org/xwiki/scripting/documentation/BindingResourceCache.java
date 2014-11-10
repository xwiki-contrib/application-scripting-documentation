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

import org.xwiki.component.annotation.Role;

/**
 * The binding resource cache, that store already found binding resources to share them between bindings.
 *
 * @version $Id$
 */
@Role
public interface BindingResourceCache
{
    /**
     * Check if the cache contains a resource matching the Id of the given resource.
     *
     * @param resource a resource to be matched in the cache.
     * @return the matching resource in the cache, or null if that resource is not in the cache.
     */
    BindingResource get(BindingResource resource);

    /**
     * Add the given resource to the cache if not already available in the cache.
     *
     * @param resource the resource to be added.
     * @return the resource from the cache.
     */
    BindingResource add(BindingResource resource);
}
