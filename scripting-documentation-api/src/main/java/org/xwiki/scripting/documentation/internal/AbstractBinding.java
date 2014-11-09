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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.xwiki.scripting.documentation.Binding;
import org.xwiki.scripting.documentation.BindingKind;
import org.xwiki.scripting.documentation.BindingResource;

/**
 * Abstract base class for java bindings.
 *
 * @version $Id$
 */
public abstract class AbstractBinding implements Binding
{
    protected static final String HTML_EXT = ".html";

    private final Type type;
    private final String name;
    private final String fullName;
    private final BindingKind kind;
    private final boolean isInternal;
    private final boolean isDeprecated;
    private final String description;
    private final BindingResource bindingResource;

    AbstractBinding(Type type, String name, String fullName, BindingKind kind, String description,
        BindingResource resource)
    {
        this.type = type;
        this.name = name;
        this.fullName = fullName;
        this.kind = kind;

        this.isInternal = getBindingClass().getCanonicalName().contains(".internal.");
        boolean deprecated = false;
        for (Annotation annotation :  getBindingClass().getAnnotations()) {
            if (annotation instanceof Deprecated) {
                deprecated = true;
            }
        }
        isDeprecated = deprecated;

        this.description = description;
        this.bindingResource = resource;
    }

    /**
     * @return the class of this binding.
     */
    public Class<?> getBindingClass()
    {
        return (type instanceof Class<?>) ? (Class<?>) type : null;
    }

    /**
     * @return the resource providing this binding.
     */
    public BindingResource getBindingResource()
    {
        return bindingResource;
    }

    /**
     * @return return the name of the class in the form of a path.
     */
    public String getClassPath()
    {
        return getBindingClass().getName().replace('.', '/');
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getFullName()
    {
        return fullName;
    }

    @Override
    public Type getType()
    {
        return type;
    }

    @Override
    public BindingKind getKind()
    {
        return kind;
    }

    @Override
    public boolean isInternal()
    {
        return isInternal;
    }

    @Override
    public boolean isDeprecated()
    {
        return isDeprecated;
    }

    @Override
    public boolean isLegacy()
    {
        return false;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public BindingResource getResource()
    {
        return bindingResource;
    }
}
