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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.ScriptBindingsFinder;

/**
 * Default implementation of the scripting binding finder, which ask all available binding finder.
 *
 * @version $Id$
 */
@Component
@Singleton
public class DefaultScriptBindingsFinder implements ScriptBindingsFinder
{
    /**
     * Used to get find binding finders.
     */
    @Inject
    @Named("context")
    private Provider<ComponentManager> componentManager;

    @Override
    public List<Binding> find()
    {
        List<Binding> bindings = new ArrayList<Binding>();
        for (ScriptBindingsFinder finder : getBindingsFinder()) {
            bindings.addAll(finder.find());
        }
        return bindings;
    }

    /**
     * @return all bindings finders.
     */
    protected List<ScriptBindingsFinder> getBindingsFinder()
    {
        try {
            List<ScriptBindingsFinder> finders =
                componentManager.get().getInstanceList((Type) ScriptBindingsFinder.class);
            finders.remove(this);
            return finders;
        } catch (ComponentLookupException e) {
            return Collections.<ScriptBindingsFinder>emptyList();
        }
    }

}
