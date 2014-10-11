
raxml: benchmarks for automata learning
==============================================

This repository contains benchmarks for automata learning algorithms.
The benchmarks are register automaton models and come from a variety
of sources and application domains. They range from explanatory
examples that have been  discussed in the literature, to manually
written specifications of data structures,  to models that were
inferred from actual systems (e.g., the Biometric Passport and the SIP
protocol). For further documentation on the benchmarks please checkout
the papers in the *./papers* folder.

The benchmarks distributed under the terms of the BSD license (see
license.txt).

The Java parser for the benchmarks is distributed under LGPLv3 (see
http://www.gnu.de/documents/lgpl.en.html).

How To Use
==============================================

This repository contains a set of benchmark register automata in an xml format.
The corresponding schema is provided as well. A description of the intended
semantics of the automaton format will hopefully be added in the future.
Until then, the repository provides a Java implementation that can parse
the provided benchmarks and execute data words on these automata. 

* The *./benchmarks* folder contains benchmarks, scripts that create xml files
  for data structures of varying size, and the xsd schema.
* The folder *./rajava* contains a Java parser for the benchmarks.
* The folder *./papers* contains additonal documentation.


How To Use
==============================================

There is some documentation available at
http://www.mbsd.cs.ru.nl/automata
for the different benchmarks.
