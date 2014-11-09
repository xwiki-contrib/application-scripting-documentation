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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.extension.repository.ExtensionRepositoryManager;
import org.xwiki.scripting.documentation.BindingResource;
import org.xwiki.scripting.documentation.ResourceResolver;

/**
 * Find the extension resource corresponding to a given type.
 *
 * @version $Id$
 */
@Component
@Named("extension")
@Singleton
public class ExtensionResourceResolver implements ResourceResolver
{
    @Inject
    private ExtensionRepositoryManager extensionRepositoryManager;

    @Override
    public BindingResource resolve(Type type)
    {
        ExtensionBindingResource resource = new ExtensionBindingResource(type, extensionRepositoryManager);

        if (resource.getGroupId() == null || UrlChecker.getURL(resource.getDocBaseURL() + "index.html") == null) {
            return null;
        }

        return resource;
    }
}
