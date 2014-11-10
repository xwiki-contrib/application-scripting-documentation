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
