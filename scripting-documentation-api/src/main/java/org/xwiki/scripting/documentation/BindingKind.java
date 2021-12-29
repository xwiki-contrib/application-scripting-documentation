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
 * The kind of binding to allow filtering them by kind.
 *
 * @version $Id$
 */
public class BindingKind
{
    /**
     * Bindings found in the velocity context (excluding those in script context).
     */
    public static final BindingKind VELOCITY = new BindingKind("velocity");

    /**
     * Bindings found in the velocity context (excluding those in script context).
     */
    public static final BindingKind TEMPLATE = new BindingKind("template");

    /**
     * Bindings found in the script context.
     */
    public static final BindingKind SCRIPT = new BindingKind("script");

    /**
     * Script services.
     */
    public static final BindingKind SCRIPT_SERVICE = new BindingKind("service");

    /**
     * Plugins (old deprecated way to provide script services).
     */
    public static final BindingKind PLUGIN = new BindingKind("plugin");

    /**
     * The internals of Plugins available with Programming Rights.
     */
    public static final BindingKind INTERNAL_PLUGIN = new BindingKind("internalplugin");

    /**
     * Any java class (not necessarily bound anywhere).
     */
    public static final BindingKind JAVA = new BindingKind("java");

    private final String type;

    /**
     * Create a new binding kind.
     * @param type the unique identifier of this binding kind.
     */
    public BindingKind(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return type;
    }
}
