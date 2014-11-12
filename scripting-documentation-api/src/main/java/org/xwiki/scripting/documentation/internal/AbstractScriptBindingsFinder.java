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

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.xwiki.localization.LocalizationManager;
import org.xwiki.localization.Translation;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.renderer.PrintRenderer;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.script.service.ScriptService;
import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.BindingCache;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.BindingResource;
import org.xwiki.scripting.documentation.ResourceResolver;
import org.xwiki.scripting.documentation.ScriptBindingsFinder;

import com.xpn.xwiki.plugin.PluginApi;
import com.xpn.xwiki.plugin.XWikiPluginInterface;

/**
 * Abstract base class for scripting binding finder.
 *
 * @version $Id$
 */
public abstract class AbstractScriptBindingsFinder implements ScriptBindingsFinder
{
    private static final String SCRIPTDOC = "scriptdoc.";
    private static final String SCRIPTDOC_INTERNAL = SCRIPTDOC + "internal.";
    private static final String SCRIPTDOC_DEPRECATED = SCRIPTDOC + "deprecated.";

    @Inject
    private ResourceResolver resourceResolver;

    @Inject
    private BindingCache bindingCache;

    @Inject
    @Named("plain/1.0")
    private PrintRenderer plainTextRenderer;

    /**
     * Used to access translations.
     */
    @Inject
    private LocalizationManager localization;

    protected abstract Map<String, Class<?>> getBindings();

    protected abstract String getFullName(String name);

    protected abstract BindingKind getType();

    @Override
    public List<Binding> find()
    {
        List<Binding> bindings = new ArrayList<Binding>();
        for (Map.Entry<String, Class<?>> entry : getBindings().entrySet()) {
            String name = entry.getKey();
            String fullName = getFullName(name);
            Class<?> klass = entry.getValue();

            BindingResource resource = resourceResolver.resolve(klass);
            Binding binding = bindingCache.get(name, resource, getType());
            if (binding == null) {
                binding = newBinding(klass, name, fullName, resource);
            }
            bindings.add(binding);
        }

        return bindings;
    }

    @Override
    public List<Binding> find(BindingKind kind)
    {
        if (kind != getType()) {
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
        BindingResource resource = resourceResolver.resolve(klass);
        Binding binding = bindingCache.get(name, resource, getType());
        if (binding == null) {
            binding = newBinding(klass, name, fullName, resource);
        }
        return binding;
    }

    @Override
    public Binding find(BindingKind kind, String name)
    {
        if (kind != getType()) {
            return null;
        }

        return find(name);
    }

    private Binding newBinding(Class<?> bindingClass, String name, String fullName, BindingResource resource)
    {
        Class<?> klass = bindingClass;

        if (isInternal(klass)) {
            klass = tryGettingPublicSuperClassOrInterface(klass);
        }

        boolean isInternal = isInternal(klass);
        boolean isDeprecated = isDeprecated(klass);
        String description = null;

        String transKey = getType().toString() + '.' + name + ".description";

        Translation translation = this.localization.getTranslation(SCRIPTDOC + transKey,  Locale.getDefault());

        if (translation == null) {
            translation = this.localization.getTranslation(SCRIPTDOC_INTERNAL + transKey,  Locale.getDefault());
            if (translation == null) {
                translation = this.localization.getTranslation(SCRIPTDOC_DEPRECATED + transKey,  Locale.getDefault());
                if (translation != null) {
                    isDeprecated = true;
                } else if (resource == null) {
                    isInternal = true;
                }
            } else {
                isInternal = true;
            }
        }

        if (translation != null) {
            description = translation.getKey();
        }

        Binding binding;
        if (resource != null && resource instanceof ExtensionBindingResource) {
            binding =
                bindingCache.add(
                    new ExtensionBinding(klass, name, fullName, getType(), isInternal, isDeprecated, description,
                        resource));
        } else {
            binding =
                bindingCache.add(
                    new GenericBinding(klass, name, fullName, getType(), isInternal, isDeprecated, description,
                        getDocLink(name)));
        }
        return binding;
    }

    private static boolean isInternal(Class<?> klass) {
        return klass.getCanonicalName().contains(".internal.");
    }

    private static boolean isDeprecated(Class<?> klass)
    {
        for (Annotation annotation :  klass.getAnnotations()) {
            if (annotation instanceof Deprecated) {
                return true;
            }
        }
        return false;
    }

    private URL getDocLink(String name)
    {
        String transKey = getType().toString() + '.' + name + ".docLink";

        Translation translation =
            this.localization.getTranslation(SCRIPTDOC + transKey, Locale.getDefault());
        if (translation == null) {
            translation = this.localization.getTranslation(SCRIPTDOC_INTERNAL + transKey, Locale.getDefault());
            if (translation == null) {
                translation = this.localization.getTranslation(SCRIPTDOC_DEPRECATED + transKey, Locale.getDefault());
            }
        }

        if (translation != null) {
            Block block = translation.render(Locale.getDefault());

            WikiPrinter printer = new DefaultWikiPrinter();
            PrintRenderer renderer = this.plainTextRenderer;
            renderer.setPrinter(printer);
            block.traverse(renderer);

            try {
                return new URL(printer.toString());
            } catch (MalformedURLException e) {
                // ignore
            }
        }

        return null;
    }

    private static Class<?> tryGettingPublicSuperClassOrInterface(Class<?> klass)
    {
        Class<?> result = klass;
        for (Class<?> iface : klass.getInterfaces()) {
            if (!isInternal(iface) && iface != ScriptService.class && iface != XWikiPluginInterface.class) {
                return iface;
            }
        }

        result = tryGettingPublicSuperClass(klass);
        if (result != klass) {
            return result;
        }

        for (Class<?> iface : klass.getInterfaces()) {
            result = tryGettingPublicSuperClassOrInterface(iface);
            if (result != iface) {
                return result;
            }
        }

        return klass;
    }

    private static Class<?> tryGettingPublicSuperClass(Class<?> klass)
    {
        Class<?> result = klass;
        Class<?> superklass = klass.getSuperclass();
        if (superklass != null && !superklass.getCanonicalName().startsWith("java.")
            && superklass != PluginApi.class)
        {
            if (!isInternal(superklass)) {
                return superklass;
            }
            result = tryGettingPublicSuperClassOrInterface(superklass);
            if (result != superklass) {
                return result;
            }
        }

        return klass;
    }
}
