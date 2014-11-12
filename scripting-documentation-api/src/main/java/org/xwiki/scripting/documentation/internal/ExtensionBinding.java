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
import java.net.URL;

import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.BindingResource;

/**
 * Represent a direct binding available to a script.
 *
 * @version $Id$
 */
public class ExtensionBinding extends AbstractBinding
{
    private final boolean isLegacy;
    private final BindingResource resource;

    /**
     * A wrapper of BindingResource to switch them to legacy mode.
     */
    private static final class LegacyResourceWrapper implements BindingResource
    {
        private final ExtensionBindingResource resource;

        LegacyResourceWrapper(ExtensionBindingResource resource)
        {
            this.resource = resource;
        }

        @Override
        public String getId()
        {
            return resource.getGroupId() + ':' + resource.getLegacyArtifactId() + ':' + resource.getVersion();
        }


        @Override
        public String getPathId()
        {
            return resource.getLegacyPathId();
        }

        @Override
        public String getDocBaseURL()
        {
            return resource.getLegacyDocBaseURL();
        }

        @Override
        public URL getDocLink()
        {
            return resource.getDocLink();
        }

        @Override
        public int hashCode()
        {
            return resource.hashCode();
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) {
                return true;
            }

            Object obj = o;
            if (o instanceof LegacyResourceWrapper) {
                obj = ((LegacyResourceWrapper) o).resource;
            }

            return resource.equals(obj);
        }
    }

    ExtensionBinding(Type type, String name, String fullName, BindingKind kind, boolean isInternal,
        boolean isDeprecated, String description, BindingResource resource)
    {
        super(type, name, fullName, kind, isInternal, isDeprecated, description);
        if (!(resource instanceof ExtensionBindingResource)) {
            throw new IllegalArgumentException("Invalid binding resource type [" + getResource().getClass()
                + "] for this binding type.");
        }
        ExtensionBindingResource bindingResource = (ExtensionBindingResource) resource;
        this.isLegacy = isDeprecated()
            && UrlChecker.getURL(bindingResource.getLegacyDocBaseURL() + getClassPath() + HTML_EXT) != null;

        this.resource = (this.isLegacy) ? new LegacyResourceWrapper(bindingResource)
                                        : resource;
    }

    @Override
    public boolean isLegacy()
    {
        return isLegacy;
    }

    @Override
    public URL getDocLink()
    {
        try {
            return new URL(resource.getDocBaseURL() + getClassPath() + HTML_EXT);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BindingResource getResource()
    {
        return this.resource;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ExtensionBinding)) {
            return false;
        }

        ExtensionBinding that = (ExtensionBinding) o;

        return resource.equals(that.resource) && super.equals(o);
    }

    @Override
    public int hashCode()
    {
        int result = 31 * resource.hashCode() + getName().hashCode();
        result = 31 * result + getKind().hashCode();
        return result;
    }
}
