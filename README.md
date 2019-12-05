
<img src="https://talk.openmrs.org/uploads/default/original/2X/f/f1ec579b0398cb04c80a54c56da219b2440fe249.jpg" alt="OpenMRS"/>

# OpenMRS Allergy API Module
The Allergy API is a Java-based API that handles patient data in OpenMRS related to allergies. More specifically, the main motivations
behind this API are to easily add allergy objects to patients, to change certain aspects of allergy objects for a patient such as severity and allergens in an organized fashion
(through variables such as allergy ID), and to compare allergies with each other to see if they have similar properties or characteristics.


## Build
To build the module from source, clone this repo:

```
https://github.com/openmrs/openmrs-module-allergyapi.git
```

Then navigate into the `openmrs-module-allergyapi` directory and compile the module using Maven:

```
cd openmrs-module-allergyapi && mvn clean install
```

:maple_leaf: Maven and Java 8 need to be installed to successfully build and run 
the tests. :maple_leaf:

## JIRA Issues
---
| Issue | Description |
| ---- | ----------- |
| [RA-357](https://issues.openmrs.org/browse/RA-357) | About the creation of the Allergy API|
|[TRUNK-4747](https://issues.openmrs.org/browse/TRUNK-4747)  | Moving of Allergy API to core platform|
|[RA-371](https://issues.openmrs.org/browse/RA-371)  | Allergy API in reference application distribution |
|[RA-462](https://issues.openmrs.org/browse/RA-462) *Open Issue*| Allergies being added to REST resources|

## Wiki Pages
---
| Page | Description |
| ---- | ----------- |
| [Allergies Reference](https://wiki.openmrs.org/pages/viewpage.action?pageId=48857177) | Refers to much of the functionality of the Allergy API|
| [Synchronizing Allergies](https://wiki.openmrs.org/display/projects/Allergy+synchronization) | Describes OpenMRS Fields and other fields|
| [Adding Rest Resources to Core](https://issues.openmrs.org/browse/RA-1036) | Description of how to add REST resources to OpenMRS core. |
| [2.5 Issue Tracking](https://wiki.openmrs.org/display/RES/Reference+Application+2.5+Release+Issue+Tracking) | Description of issues for various modules in OpenMRS |


## Contribute
OpenMRS is a highly active and engaged open-source community who would love to have more help!
To contribute to APIs like this and more, sign up for an OpenMRS ID in the following link: 

https://openmrs.org/


## License
---
This project is licensed under the [Mozilla Public License 2.0](mozilla.org/en-US/MPL/2.0/) - see the LICENSE file for details





