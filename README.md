# clojure_manifold

[Manifold learning](http://en.wikipedia.org/wiki/Nonlinear_dimensionality_reduction)
algorithms in Clojure.

## Usage

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
