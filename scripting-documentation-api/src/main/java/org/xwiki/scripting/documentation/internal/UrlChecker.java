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

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Check URL for scripting documentation.
 *
 * @version $Id$
 */
public final class UrlChecker
{
    private static final String USER_AGENT = "User-Agent";

    private static final String SCRIPTING_DOCUMENTATION_USER_AGENT = "Scripting Documentation check";

    private static final String HEAD = "HEAD";

    private UrlChecker()
    {
        
    }

    /**
     * Check an URL is valid and exists on the remote server before returning it.
     *
     * @param urlString the url to check.
     * @return the url if the url is accessible.
     */
    public static URL getURL(String urlString)
    {
        try {
            URL url = new URL(urlString);

            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod(HEAD);
            huc.setRequestProperty(USER_AGENT, SCRIPTING_DOCUMENTATION_USER_AGENT);
            huc.setConnectTimeout(10000);
            huc.setReadTimeout(10000);

            if (huc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return url;
            }
        } catch (Exception e) {
            // Ignored
        }
        return null;
    }

    /**
     * Check an URL is a redirected URL, and provide the redirected URL.
     *
     * @param urlString the URL to check.
     * @return the redirection destination if the url is accessible.
     */
    public static URL getRedirectURL(String urlString)
    {
        try {
            URL url = new URL(urlString);

            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod(HEAD);
            huc.setRequestProperty(USER_AGENT, SCRIPTING_DOCUMENTATION_USER_AGENT);
            huc.setConnectTimeout(10000);
            huc.setReadTimeout(10000);
            huc.setInstanceFollowRedirects(false);

            if (huc.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                return new URL(huc.getHeaderField("Location"));
            }
        } catch (Exception e) {
            // Ignored
        }
        return null;
    }
}
