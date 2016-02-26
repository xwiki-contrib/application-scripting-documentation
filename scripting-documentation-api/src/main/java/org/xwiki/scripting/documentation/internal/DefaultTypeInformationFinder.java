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

import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.TypeInformationFinder;

/**
 * Implementation of {@link TypeInformationFinder}.
 *
 * @version $Id$
 */
@Component
@Singleton
public class DefaultTypeInformationFinder extends AbstractTypeInformationFinder implements TypeInformationFinder
{
    @Override
    protected BindingKind getType()
    {
        return BindingKind.JAVA;
    }

    @Override
    public Binding find(Type type)
    {
        String name = ((Class< ? >) type).getSimpleName();

        Binding binding = bindingCache.get(name, type, getType());
        if (binding != null) {
            return binding;
        }

        binding = newBinding((Class< ? >) type, ((Class< ? >) type).getSimpleName(),
            ((Class< ? >) type).getCanonicalName());

        if (binding.getResource() != null || binding.getDocLink() != null) {
            return bindingCache.add(binding);
        }

        return null;
    }

    @Override
    public Binding find(String name)
    {
        Class< ? > klass = null;

        try {
            klass = Class.forName(name, false, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            // Ignored
        }

        if (klass  != null) {
            return find(klass);
        }

        return null;
    }
}
