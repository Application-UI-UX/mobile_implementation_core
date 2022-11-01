## RosJava Core

Updated Version [rosjava_core](https://github.com/Application-UI-UX/rosmobile_core) readme.

Standard Version [rosjava_core](https://github.com/rosjava/rosjava_core) readme.

This package is a generator of rosjava message artifacts for core ros messages for mobile

## Important
The repository has diverged, as the old commits and codes are under the previous License and
the new commits and codes are under New License

----

Latest versions and Maintainer is on Application-UI-UX

### Building and Packaging
The package is published in https://github.com/Application-UI-UX

### Adding Packages

If you would like to add a message dependency to this list, first consider if it is a worthwhile candidate, these libraries 
are not trying  to replace existing more so displaying useful information to the user with controls

* It is a direct dependency for rosjava/android
* It is a popular and stable dependency that will require little maintenance
* Latest versions are on Application-UI-UX and it is noetic

Then to actually add the dependency:

* new release versions are noetic
* add the message dependency to package.xml
* add the message dependency to CMakeLists.txt
* create a pull request
* update the changelog and bump the version number in package.xml
* tag it with the new version number
* release

### Rereleasing Version Changes

When the underlying message dependency version numbers shift, this will
require a rebuild of this package:

* update the changelog and bump the version number in package.xml
* tag it with the new version number
* release

### Maintainer
* Ronaldson Bellande


## License
This SDK is distributed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0), see [LICENSE](https://github.com/Application-UI-UX/rosmobile_build_tools/blob/master/LICENSE) and [NOTICE](https://github.com/Application-UI-UX//blob/master/LICENSE) for more information.
