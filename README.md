#readme

Tensorics is a java framework for data processing of multidimensional data. 
The core object is a Tensor which represents a collection of values of arbitrary
types, which are addressed by coordinates in an N-dimensional space. The framework
provides several core features, which make it easy to work with big amounts of 
numerical data and drastically simplify repetitive tasks:

---
* Tensors of arbitrary dimensionality as central object.
* Tensors can have elements of any (java) type.
* Structural and numerical operations on tensors.
* Java internal DSL (fluent API) for all operations on scalars and tensors.
* Quantities (value - unit pair).
* Full support for Tensors of quantities.
* Error and Validity propagation for quantities and tensors of quantities.
* Scripting of all functionality with deferred execution, which opens the 
possibilities for parallel processing and massive distribution of calculations. 

---
 
IMPORTANT: Both, the current implementation as well as this document, are work in progress. 
The main purpose of the actual version is provide some functionality of every of the above 
mentioned categories and proofing the concepts of their interplay. Still, already the available 
subset of features should have useful applications in many contexts. Almost no effort put on 
profiling and performance optimization. Therefore, it might well be that some operations are 
quite inefficient and/or memory consuming for big objects. Such improvements are definitely planned 
for later iterations. Any contributions are welcome.