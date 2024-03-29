# GPFramework (0.1)
## An extensible Java framework for tree-based Genetic Programming

### Introduction

GPFramework is a flexible Java framework for quick prototyping of Genetic Programming
systems. Its main purpose has been, so far, to study Genetic Programming computational
complexity from an experimental point of view (results of this research have been accepted
for publishing in "Urli T., Wagner M. and Neumann F. (2012). Experimental Supplements to 
the Computational Complexity Analysis of Genetic Programming for Problems Modelling Isolated 
Program Semantics. In Proceedings of PPSN 2012 - 12th International Conference on Parallel 
Problem Solving From Nature). Because of this specific purpose, the framework lacks a 
number of features, e.g. support for cross-over operators, but it is (quite funnily) a 
project under evolution and we invite everyone to contribute.

### How to use it

As a result of our work in the analysis of Genetic Programming's complexity, GPFramework
includes a number of fitness functions for sorting, order and majority. It currently
implements two underlying mechanisms to evolve programs:

    * SMO-GP, an algorithm inspired by the SEMO evolutionary multi-objective algorithm,
    * (m+m)-GP, a generic population-based evolutionary algorithm where the size of the
      population can be set as a parameter

with several selection criteria including:

    * Parsimony pressure,
    * strict/weak hill climbing,
    * multi-objective (for SMOGP).

The best way to learn how to use it is to read the wide inline documentation (javadoc) 
and have a look to how sorting, order and majority have been implemented. 

GPFramework is distributed together with a NetBeans project, but should be opened
fairly easily in other IDEs as well.

### Dependencies

Three libraries are needed in order to make GPFramework compile.

    * Colt, http://acs.lbl.gov/software/colt
    * Apache Commons Math >= 1.2
    * Apache Commons CLI >= 2.2

It is sufficient to add those to the project in order to use GPFramework.

### Licensing

This software is licensed under the MIT License. This means that you can use it for
whatever purpose, without any licensing limitation on the derivatives. See LICENSE 
file for more details.
