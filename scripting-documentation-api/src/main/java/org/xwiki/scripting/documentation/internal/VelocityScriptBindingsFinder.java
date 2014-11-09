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

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.velocity.VelocityContext;
import org.xwiki.component.annotation.Component;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.velocity.VelocityManager;

/**
 * Search for bindings in the velocity context.
 *
 * @version $Id$
 */
@Component
@Named("velocity")
@Singleton
public class VelocityScriptBindingsFinder extends AbstractScriptBindingsFinder
{
    /**
     * Used to get the Velocity Context from which we retrieve the list of bound variables.
     */
    @Inject
    private VelocityManager velocityManager;

    /**
     * @return the map of classes bindings in all velocity contexts
     */
    protected Map<String, Class<?>> getBindings()
    {
        VelocityContext vContext = this.velocityManager.getVelocityContext();

        Map<String, Class<?>> bindings = new HashMap<String, Class<?>>();
        while (vContext != null) {
            addAllBinding(vContext, bindings);
            vContext = (VelocityContext) vContext.getChainedContext();
        }

        return bindings;
    }

    @Override
    protected String getFullName(String name)
    {
        return name;
    }

    @Override
    protected BindingKind getType()
    {
        return BindingKind.VELOCITY;
    }

    private static void addAllBinding(VelocityContext context, Map<String, Class<?>> bindings)
    {
        for (Object key : context.getKeys()) {
            bindings.put(key.toString(), context.get(key.toString()).getClass());
        }
    }
}
