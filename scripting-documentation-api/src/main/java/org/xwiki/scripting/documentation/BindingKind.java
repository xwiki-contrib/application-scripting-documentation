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

package org.xwiki.scripting.documentation;

/**
 * The kind of binding to allow filtering them by kind.
 *
 * @version $Id$
 */
public class BindingKind
{
    /**
     * Bindings found in the velocity context (to be improved to separate the script context).
     */
    public static final BindingKind VELOCITY = new BindingKind("velocity");

    /**
     * Script services.
     */
    public static final BindingKind SCRIPT_SERVICE = new BindingKind("service");

    /**
     * Plugins (old deprecated way to provide script services).
     */
    public static final BindingKind PLUGIN = new BindingKind("plugin");

    /**
     * The internals of Plugins available with Programming Rights.
     */
    public static final BindingKind INTERNAL_PLUGIN = new BindingKind("internalplugin");

    private final String type;

    /**
     * Create a new binding kind.
     * @param type the unique identifier of this binding kind.
     */
    public BindingKind(String type) {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return type;
    }
}
