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

/**
 * Please comment here
 *
 * @version $Id$
 */

import java.lang.reflect.Type;

import org.xwiki.component.annotation.Role;

/**
 * The binding cache, that store already found bindings to reuse them between requests.
 *
 * @version $Id$
 */
@Role
public interface BindingCache
{
    /**
     * Check if the cache contains a binding matching the given fullname.
     *
     * @param name a binding name.
     * @param bindingType the type of this binding.
     * @param kind the kind of binding.
     * @return the matching binding in the cache, or null if that binding is not in the cache.
     */
    Binding get(String name, Type bindingType, BindingKind kind);

    /**
     * Add the given binding to the cache if not already available in the cache.
     *
     * @param binding the binding to be added.
     * @return the binding from the cache.
     */
    Binding add(Binding binding);
}
