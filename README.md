# clojure_manifold

[Manifold learning](http://en.wikipedia.org/wiki/Nonlinear_dimensionality_reduction)
algorithms in Clojure.

## Explanations:
* [MDS](http://blog.shriphani.com/2014/10/29/low-dimension-embeddings-for-visualization/)
* [Isomap](http://blog.shriphani.com/2014/11/12/the-isomap-algorithm/)

## Usage

In both cases, M is a matrix and we try to fit M to k dimensions where k < number of columns of M.
Currently the matrix is loaded from a CSV file using <code>clojure-manifold.data/load-data</code>.

```clojure
user> (use 'clojure-manifold.data :reload)
nil
user> (def M (load-data "foo.csv"))
```

### MDS algorithm:
```clojure
user> (use 'clojure-manifold.mds :reload)
nil
user> (mds M 2) ; M is a matrix, 2 is the desired number of dimensions
```

### Isomap algorithm:
```clojure
user> (use 'clojure-manifold.isomap :reload)
nil
user> (isomap M 2) ; M is a matrix, 2 is the desired number of dimensions
```

## License

Copyright © 2014 Shriphani Palakodety

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
