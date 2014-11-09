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

    private UrlChecker() { }

    /**
     * Check an URL is valid and exists on the remote server before returning it.
     *
     * @param urlString the url to check.
     * @return the url if the url is accessible.
     */
    public static URL getURL(String urlString) {
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
    public static URL getRedirectURL(String urlString) {
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
