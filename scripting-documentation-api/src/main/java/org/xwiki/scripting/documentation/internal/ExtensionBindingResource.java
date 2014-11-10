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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.xwiki.extension.ResolveException;
import org.xwiki.extension.repository.ExtensionRepositoryManager;
import org.xwiki.extension.repository.result.IterableResult;
import org.xwiki.extension.version.Version;
import org.xwiki.extension.version.internal.DefaultVersion;

import com.google.common.base.Predicates;

/**
 * XWiki Extensions binding resource.
 *
 * @version $Id$
 */
public class ExtensionBindingResource extends AbstractBindingResource
{
    private static final String JAVADOC_JAR = "-javadoc.jar";

    private final ExtensionRepositoryManager extensionRepositoryManager;

    private boolean resolved;
    private String groupId;
    private String artifactId;
    private String legacyArtifactId;
    private String version;
    private URL moduleDocLink;

    private static final class MavenCoord
    {
        private final String groupId;
        private final String artifactId;
        private final String version;

        private MavenCoord(String groupId, String artifactId, String version)
        {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        public String getGroupId()
        {
            return groupId;
        }

        public String getArtifactId()
        {
            return artifactId;
        }

        public String getVersion()
        {
            return version;
        }
    }

    ExtensionBindingResource(Type type, ExtensionRepositoryManager extensionRepositoryManager)
    {
        super(type);
        this.extensionRepositoryManager = extensionRepositoryManager;
    }

    @Override
    public String getId()
    {
        return getGroupId() + ':' + artifactId + ':' + version;
    }

    @Override
    public String getPathId()
    {
        return getGroupId().replace('.', '/') + '/' + artifactId + '/' + version;
    }

    @Override
    public URL getDocLink()
    {
        if (!resolved) {
            resolve();
        }
        return moduleDocLink;
    }

    /**
     * @return the identifier in the form of a path that could be used to build documentation paths to the legacy
     *         version of this resource.
     */
    public String getLegacyPathId()
    {
        return getGroupId().replace('.', '/') + '/' + legacyArtifactId + '/' + version;
    }

    @Override
    public String getArchiveBrowserBaseURL() {
        return "http://nexus.xwiki.org/nexus/service/local/repositories/public/archive/";
    }

    @Override
    public String getJavadocJarName()
    {
        return artifactId + '-' + version + JAVADOC_JAR;
    }

    /**
     * @return the name of the javadoc archive for the legacy version of this resource.
     */
    public String getLegacyJavadocJarName()
    {
        return legacyArtifactId + '-' + version + JAVADOC_JAR;
    }

    /**
     * @return the base URL of the documentation for the legacy version of this resource.
     */
    public String getLegacyDocBaseURL()
    {
        return getArchiveBrowserBaseURL() + getLegacyJavadocPathName() + "/!/";
    }

    /**
     * @return the path for accessing the javadoc archive of the legacy version of this resource on site providing
     *         access to it.
     */
    public String getLegacyJavadocPathName()
    {
        return getLegacyPathId() + '/' + getLegacyJavadocJarName();
    }

    /**
     * @return return the maven groupid of this resource.
     */
    public String getGroupId()
    {
        if (!resolved) {
            resolve();
        }
        return groupId;
    }

    /**
     * @return return the maven artifactid of this resource.
     */
    public String getArtifactId()
    {
        if (!resolved) {
            resolve();
        }
        return artifactId;
    }

    /**
     * @return return the maven artifactid of the legacy version of this resource.
     */
    public String getLegacyArtifactId()
    {
        if (!resolved) {
            resolve();
        }
        return legacyArtifactId;
    }

    /**
     * @return return the maven version of this resource.
     */
    public String getVersion()
    {
        if (!resolved) {
            resolve();
        }
        return version;
    }

    private void resolve()
    {
        resolved = true;

        for (String pomFile : getPomPropertyFiles()) {
            URL pomFileUrl = this.classLoader.getResource(pomFile);
            if (pomFileUrl == null) {
                continue;
            }

            MavenCoord mavenCoord = readPomPropertyFile(pomFileUrl);

            if (mavenCoord.getArtifactId() != null) {
                if (mavenCoord.getArtifactId().contains("-legacy-")) {
                    legacyArtifactId = mavenCoord.getArtifactId();
                    if (groupId == null) {
                        groupId = mavenCoord.getGroupId();
                    }
                    if (version == null) {
                        version = mavenCoord.getVersion();
                    }
                } else {
                    groupId = mavenCoord.getGroupId();
                    artifactId = mavenCoord.getArtifactId();
                    version = mavenCoord.getVersion();
                }
            }
        }

        if (version != null) {
            version = getLastReleasedVersion();
        }

        moduleDocLink =
            UrlChecker.getRedirectURL("http://extensions.xwiki.org/xwiki/bin/view/Main/WebHome?id=" + getId());
    }

    private String getLastReleasedVersion()
    {
        int is = version.indexOf("-SNAPSHOT");
        if (is == -1) {
            return version;
        }

        Version lastReleaseVersion = null;
        try {
            IterableResult<Version> versions =
                extensionRepositoryManager.resolveVersions(groupId + ':' + artifactId, 0, -1);
            Version currentVersion = new DefaultVersion(version);
            for (Version v : versions) {
                if (v.getType() != Version.Type.SNAPSHOT
                    && (lastReleaseVersion == null || v.compareTo(lastReleaseVersion) > 0)
                    && v.compareTo(currentVersion) < 0)
                {
                    lastReleaseVersion = v;
                }
            }
        } catch (ResolveException e) {
            // ignore;
        }

        if (lastReleaseVersion !=  null) {
            return lastReleaseVersion.getValue();
        } else {
            return version;
        }
    }

    private Set<String> getPomPropertyFiles()
    {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setScanners(new ResourcesScanner());
        configurationBuilder.setUrls(url);
        configurationBuilder.filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix("META-INF.maven")));

        Reflections reflections = new Reflections(configurationBuilder);
        return reflections.getResources(Predicates.equalTo("pom.properties"));
    }

    private static MavenCoord readPomPropertyFile(URL url)
    {
        String groupId = null;
        String artifactId = null;
        String version = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String line = reader.readLine();
            while (line != null && (groupId == null || artifactId == null || version == null)) {
                if (line.startsWith("groupId=")) {
                    groupId = line.substring(8);
                } else if (line.startsWith("artifactId=")) {
                    artifactId = line.substring(11);
                } else if (line.startsWith("version=")) {
                    version = line.substring(8);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            // Ignored
        }

        return new MavenCoord(groupId, artifactId, version);
    }
}
