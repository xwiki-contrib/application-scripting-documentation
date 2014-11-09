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

import java.lang.reflect.Type;
import java.net.URL;

/**
 * Represent a binding and provide information about it.
 *
 * @version $Id$
 */
public interface Binding
{
    /**
     * @return the name of this binding.
     */
    String getName();

    /**
     * @return the full name of this binding.
     */
    String getFullName();

    /**
     * @return the type of this binding.
     */
    Type getType();

    /**
     * @return the type of this binding.
     */
    BindingKind getKind();

    /**
     * @return true if this binding is from an internal package.
     */
    boolean isInternal();

    /**
     * @return true if this binding is deprecated.
     */
    boolean isDeprecated();

    /**
     * @return true if this binding is from a legacy package.
     */
    boolean isLegacy();

    /**
     * @return a description of this binding. Could be null.
     */
    String getDescription();

    /**
     * @return an URL pointing to the Javadoc of this binding. Could be null if no Javadoc has not been discovered.
     */
    URL getDocLink();

    /**
     * @return the binding resource that provide this binding.
     */
    BindingResource getResource();
}
