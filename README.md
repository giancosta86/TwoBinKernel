# TwoBinKernel

*Scala library for two-dimensional packing*


## Introduction

**TwoBinKernel** is the very heart of [TwoBinPack](https://github.com/giancosta86/TwoBinPack), providing a shared model and a set of utilities to deal with *two-dimensional packing problems*.


It is *a Scala library*, inspired by **Functional Programming** (especially *Elm*, *Erlang* and *Haskell*) but still maintaining *a hybrid OOP-FP nature*.

Last but not least, you can even employ it to introduce TwoBinPack's model into your own applications!



## Requirements

Scala 2.11.8 or later and Java 8u101 or later are recommended to employ TwoBinKernel.


## Referencing the library

TwoBinKernel is available on [Hephaestus](https://bintray.com/giancosta86/Hephaestus) and can be declared as a Gradle or Maven dependency; please refer to [its dedicated page](https://bintray.com/giancosta86/Hephaestus/TwoBinKernel).

Alternatively, you could download the JAR file from Hephaestus and manually add it to your project structure.

Finally, TwoBinKernel is also a standard [OSGi](http://www.slideshare.net/giancosta86/introduction-to-osgi-56290394) bundle which you can employ in your OSGi architectures! ^\_\_^


## Core concepts

**TwoBinKernel**'s root package is **info.gianlucacosta.twobinpack**, having a few notable subpackages:

* **core**: essential concepts such as **Problem** and **Solution**

* **io**: shared I/O utilities - for example, to read/write model entities from/to files

* **rendering**: ScalaFX controls for rendering problems and solutions. The most important classes are **Frame**, **AxesPane** and **BlockGalleryPane**


For further information, please consult the library's Scaladoc.



## Further references

* [TwoBinPack](https://github.com/giancosta86/TwoBinPack)

* [Facebook page](https://www.facebook.com/TwoBinPack-234021307010796)

* [Helios-fx](https://github.com/giancosta86/Helios-fx)

* [Scala](http://scala-lang.org/)

* [ScalaFX](http://scalafx.org/)

* [Elm](http://elm-lang.org/)

* [Haskell](https://www.haskell.org/)
