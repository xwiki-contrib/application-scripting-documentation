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

import java.util.Map;

import javax.inject.Inject;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.xwiki.velocity.VelocityContextFactory;
import org.xwiki.velocity.XWikiVelocityException;

/**
 * Abstract base class for velocity bindings finders.
 *
 * @version $Id$
 */
public abstract class AbstractVelocityScriptBindingFinder extends AbstractScriptBindingsFinder
{
    /**
     * Used to get the Velocity Context from which we retrieve the list of bound variables.
     */
    @Inject
    private VelocityContextFactory velocityContextFactory;

    @Inject
    private Logger logger;

    protected VelocityContext getVelocityContext()
    {
        try {
            return this.velocityContextFactory.createContext();
        } catch (XWikiVelocityException e) {
            logger.error("Unable to get a new velocity context.");
        }
        return null;
    }

    protected void addAllBinding(VelocityContext vcontext, Map<String, Class<?>> bindings)
    {
        for (Object key : vcontext.getKeys()) {
            String name = key.toString();
            bindings.put(name, vcontext.get(name).getClass());
        }
    }
}
