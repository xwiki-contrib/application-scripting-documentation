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

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.scripting.documentation.BindingKind;

import com.xpn.xwiki.plugin.XWikiPluginInterface;

/**
 * Search for plugins and report internal classes.
 *
 * @version $Id$
 */
@Component
@Named("internalplugin")
@Singleton
public class InternalPluginScriptBindingsFinder extends AbstractPluginScriptBindingsFinder
{
    @Override
    protected Class<?> getPluginClass(XWikiPluginInterface plugin)
    {
        if (plugin != null) {
            return plugin.getClass();
        }
        return null;
    }

    @Override
    protected String getFullName(String name)
    {
        return "xwiki." + name + ".getInternalPlugin()";
    }

    @Override
    protected BindingKind getType()
    {
        return BindingKind.INTERNAL_PLUGIN;
    }
}
