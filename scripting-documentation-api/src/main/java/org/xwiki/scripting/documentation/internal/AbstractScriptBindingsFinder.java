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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.ScriptBindingsFinder;

/**
 * Abstract base class for scripting binding finder.
 *
 * @version $Id$
 */
public abstract class AbstractScriptBindingsFinder extends AbstractTypeInformationFinder implements ScriptBindingsFinder
{
    protected abstract Map<String, Class<?>> getBindings();

    protected abstract String getFullName(String name);

    @Override
    public List<Binding> find()
    {
        return find((Pattern) null);
    }

    @Override
    public List<Binding> find(BindingKind kind)
    {
        if (!getType().equals(kind)) {
            return null;
        }

        return find();
    }

    @Override
    public Binding find(String name)
    {
        Class<?> klass = getBindings().get(name);

        if (klass == null) {
            return null;
        }

        String fullName = getFullName(name);

        // Make sure that only one thread at a time manipulate the cache to avoid useless duplication work
        synchronized (this.bindingCache) {
            Binding binding = this.bindingCache.get(name, klass, getType());
            if (binding == null) {
                binding = newBinding(klass, name, fullName);
            }

            return binding;
        }
    }

    @Override
    public Binding find(BindingKind kind, String name)
    {
        if (!getType().equals(kind)) {
            return null;
        }

        return find(name);
    }

    @Override
    public List<Binding> find(Pattern regex)
    {
        List<Binding> bindings = new ArrayList<Binding>();

        Set<Map.Entry<String, Class<?>>> entries = getBindings().entrySet();

        // Make sure that only one thread at a time manipulate the cache to avoid useless duplication work
        synchronized (this.bindingCache) {
            for (Map.Entry<String, Class<?>> entry : entries) {
                String name = entry.getKey();

                if (regex == null || regex.matcher(name).matches()) {
                    String fullName = getFullName(name);
                    Class<?> klass = entry.getValue();

                    Binding binding = bindingCache.get(name, klass, getType());
                    if (binding == null) {
                        binding = newBinding(klass, name, fullName);
                    }
                    bindings.add(binding);
                }
            }
        }

        return bindings;
    }

    @Override
    public List<Binding> find(BindingKind kind, Pattern regex)
    {
        if (!getType().equals(kind)) {
            return null;
        }

        return find(regex);
    }

    @Override
    protected Binding newBinding(Class<?> bindingClass, String name, String fullName)
    {
        return this.bindingCache.add(super.newBinding(bindingClass, name, fullName));
    }
}
