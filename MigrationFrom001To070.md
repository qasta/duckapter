There was a lot of work and API changes from the first release 0.0.1 to 0.7.0. This is also the reason why the version number rose so much. If you were using previous release you need to change your code this way:

All annotations from previous release are still there besides the @Original which was considered as too dangerous. All checker annotations now reside in org.duckapter.annotation package.

The base duck testing and typing methods from Duckapter facade class were changed to the ones found in org.duckapter.Duck class.

| **Old method** | **New method** |
|:---------------|:---------------|
| eu.ebdit.duckapter.Duckapter.canAdaptInstance(Object, Class) | org.duckapter.Duck.test(Object, Class)|
| eu.ebdit.duckapter.Duckapter.canAdaptClass(Class, Class) | org.duckapter.Duck.test(Class, Class)|
| eu.ebdit.duckapter.Duckapter.adaptInstance(Object, Class) | org.duckapter.Duck.type(Object, Class)|
| eu.ebdit.duckapter.Duckapter.adaptClass(Class, Class) | org.duckapter.Duck.type(Class, Class)|