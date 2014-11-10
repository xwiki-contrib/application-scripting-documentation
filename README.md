Scripting Documentation
=======================

The concept of this application is to provide scripting documentation by dynamically searching the current wiki 
instance for available bindings. Using the information from Maven pom's, those bindings get linked to their respective
documentations both on the XWiki Extension Repository and the XWiki Nexus Repository that hold Javadoc packages.

Compare to the actual Scripting Reference Documentation, the usage of this application does not require any work
effort to generate the documentation. It is based on what we already release. The current API does not allow access 
to older versions, but this could be implemented in the future, at least, up to version 3.5 where there have been a
couple of changes that are not easier to cope with.
However, it has already a special handling for SNAPSHOT version (for which javadoc is not generated) to point to the
latest release preceding that SNAPSHOT version.

This is a work in progress, the actual API is quite complete, but the UI is very basic.
