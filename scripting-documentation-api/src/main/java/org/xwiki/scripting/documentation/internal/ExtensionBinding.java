/*
 * Copyright 2010-2014 SOFTEC sa. All rights reserved.
 *
 * This software is copyrighted. The software may not be copied,
 * reproduced, translated, or reduced to any electronic medium
 * or machine-readable form without the prior written consent of
 * SOFTEC sa.
 *
 * THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO SOFTEC S.A.
 * AND MAY NOT BE REPRODUCED, PUBLISHED, OR DISCLOSED TO OTHERS
 * WITHOUT COMPANY AUTHORIZATION.
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

    ExtensionBinding(Type type, String name, String fullName, BindingKind kind, String description,
        BindingResource resource)
    {
        super(type, name, fullName, kind, description, resource);
        if (!(super.getResource() instanceof ExtensionBindingResource)) {
            throw new IllegalArgumentException("Invalid binding resource type [" + getResource().getClass()
                + "] for this binding type.");
        }

        this.isLegacy = isDeprecated()
            && UrlChecker.getURL(getExtensionBindingResource().getLegacyDocBaseURL() + getClassPath() + HTML_EXT)
                != null;

        this.resource = (this.isLegacy) ? new LegacyResourceWrapper(getExtensionBindingResource())
                                        : super.getResource();
    }

    private ExtensionBindingResource getExtensionBindingResource()
    {
        return (ExtensionBindingResource) super.getResource();
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
            return new URL(getResource().getDocBaseURL() + getClassPath() + HTML_EXT);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BindingResource getResource()
    {
        return this.resource;
    }
}
