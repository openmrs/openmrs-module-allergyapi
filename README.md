About the Allergy API Module
===
The Allergy API is a Java-based API that handles patient data in OpenMRS related to allergies. More specifically, the main motivations
behind this API are to easily add allergy objects to patients, to change certain aspects of allergy objects for a patient such as severity and allergens in an organized fashion
(through variables such as allergy ID), and to compare allergies with each other to see if they have similar properties or characteristics.

Developer Documentation
---

Allergy.java
---
The Allergy constructor takes 5 parameters, based on the patient to set, the allergen to set, the severity to set, a comment to be made
and any reactions related with the allergy, **with getters and setters** for the aforementioned parameters.
```java
public Allergy(Patient patient, Allergen allergen, Concept severity, String comment, List<AllergyReaction> reactions) {
		this.patient = patient;
		this.allergen = allergen;
		this.severity = severity;
		this.comment = comment;
		if (reactions != null) {
			this.reactions = reactions;
		}
	}
```
The addReaction method takes an AllergyReaction parameter (see below for documentation) and adds the reaction to the list of reactions.
The method returns true if the reaction was added, and false if otherwise.
```java
public boolean addReaction(AllergyReaction reaction)
```
The removeReaction methods removes an AllergyReaction from the list of reactions. The method return true if the reaction was removed,
false if otherwise.
```java
public boolean removeReaction(AllergyReaction reaction)
```
The hasSameValues method compares two allergies and returns true if the allergies have the same properties. Otherwise it returns false.
```java
public boolean hasSameValues(Allergy allergy)
```
The hasSameReactions method compares the reactions of two allergies and returns true if the values match. Otherwise it returns false.
```java
private boolean hasSameReactions(Allergy allergy)
```
The copy method copies the information from an allergy paramater to the object the method is being acted upon.
```java
public void copy(Allergy allergy) throws InvocationTargetException, IllegalAccessException
```
The hasSameAllergen method compares the allergens of both allergies. If they are the same, the method returns true, else it returns false.
```java
public boolean hasSameAllergen(Allergy allergy)
```

AllergyReaction.java
---
The Allergy Reaction constructor takes 3 parameters, an allergy, reaction, reactionNonCoded, each of which have their own getter
and setter methods. The constructor is primarily used to add to an allergy in Allergy.java.
```java
public AllergyReaction(Allergy allergy, Concept reaction, String reactionNonCoded) {
		this.allergy = allergy;
		this.reaction = reaction;
		this.reactionNonCoded = reactionNonCoded;
	}
```
The hasSameValues method compares two AllergyReaction objects to see if they have the same properties. The method returns true
if the properties the same, false if otherwise.
```java
public boolean hasSameValues(AllergyReaction reaction)
```

Allergen.java
---
The Allergen constructor takes in 3 parameters, depending on the allergenType, as specified in the AllergenType.java class, 
the codedAllergen, and the nonCodedAllergen, with getters and setters for each.
```java
public Allergen(AllergenType allergenType, Concept codedAllergen, String nonCodedAllergen) {
		this.allergenType = allergenType;
		this.codedAllergen = codedAllergen;
		this.nonCodedAllergen = nonCodedAllergen;
	}
```
isSameAllergen returns true if both allergens have the coded or noncoded allergens or both. Otherwise, it returns false.
```java
public boolean isSameAllergen(Allergen allergen) 
```
---

Allergies.java
---
This classes handles lists of allergy objects and modifies the normal List<Allergy> methods in accordance to the constraints
of the Allergy.java class. 


The method containsAllergen returns true if the allergy parameter has at least one common allergen with the allergies in the Collection
parameter. Otherwise the method returns false.
```java
public boolean containsAllergen(Allergy allergy, Collection<? extends Allergy> allergies)
```
Licenses
---
This project is licensed under the Mozilla Public License 2.0 - see the LICENSE file for details

Wiki Pages
---
For more information, refer to:
* https://wiki.openmrs.org/pages/viewpage.action?pageId=48857177
* https://wiki.openmrs.org/display/projects/Allergy+synchronization
* https://issues.openmrs.org/browse/RA-1036
* https://wiki.openmrs.org/display/RES/Reference+Application+2.4+Release+Issue+Tracking
* https://wiki.openmrs.org/display/RES/Reference+Application+2.5+Release+Issue+Tracking

