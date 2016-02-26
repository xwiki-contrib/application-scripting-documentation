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
import java.net.URL;

/**
 * Represent a Type or a binding with information about it.
 *
 * @version $Id$
 */
public interface Binding
{
    /**
     * @return the name of this binding.
     */
    String getName();

    /**
     * @return the full name of this binding.
     */
    String getFullName();

    /**
     * @return the type of this binding.
     */
    Type getType();

    /**
     * @return the type of this binding.
     */
    BindingKind getKind();

    /**
     * @return true if this binding is from an internal package.
     */
    boolean isInternal();

    /**
     * @return true if this binding is deprecated.
     */
    boolean isDeprecated();

    /**
     * @return true if this binding is from a legacy package.
     */
    boolean isLegacy();

    /**
     * @return a description of this binding. Could be null.
     */
    String getDescription();

    /**
     * @return an URL pointing to the Javadoc of this binding. Could be null if no Javadoc has not been discovered.
     */
    URL getDocLink();

    /**
     * @return the binding resource that provide this binding.
     */
    BindingResource getResource();
}
