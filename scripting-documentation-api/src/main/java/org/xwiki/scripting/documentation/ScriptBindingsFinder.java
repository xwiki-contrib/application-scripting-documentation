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

import java.util.List;
import java.util.regex.Pattern;

import org.xwiki.component.annotation.Role;

/**
 * Find bindings provided to scripts.
 *
 * @version $Id$
 */
@Role
public interface ScriptBindingsFinder
{
    /**
     * @return a list of all bindings found.
     */
    List<Binding> find();

    /**
     * @param kind a binding kind.
     * @return a list of all bindings of given kind.
     */
    List<Binding> find(BindingKind kind);

    /**
     * @param name a binding name.
     * @return first bindings of given name.
     */
    Binding find(String name);

    /**
     * @param kind a binding kind.
     * @param name a binding name.
     * @return the bindings matching the given kind and name. Null if none where found.
     */
    Binding find(BindingKind kind, String name);

    /**
     * @param regex a pattern to match binding name.
     * @return a list of all bindings matching regex.
     */
    List<Binding> find(Pattern regex);

    /**
     * @param kind a binding kind.
     * @param regex a binding name.
     * @return a list of all bindings of the given kind matching regex.
     */
    List<Binding> find(BindingKind kind, Pattern regex);
}
