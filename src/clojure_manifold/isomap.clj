(ns clojure-manifold.isomap
  "Implementation of the Isomap algorithm for Clojure"
  (:refer-clojure :exclude [* - + == /])
  (:require [clojure.core.matrix :refer :all]
            [clojure.core.matrix.operators :refer :all]
            [clojure-manifold.mds :as mds]))

(set-current-implementation :vectorz)

(defn build-point-graph
  "A point graph is a k-NN graph. Edges between
   a point and its 3 nearest neigbors"
  ([indexed-points]
     (build-point-graph indexed-points 3))

  ([indexed-points num-neighbors]
     (reduce
      (fn [acc pt]
        (let [other-points (filter
                            (fn [x]
                              (not= (first x)
                                    (first pt)))
                            indexed-points)]
          (merge
           acc
           {(first pt) (map
                        first
                        (take num-neighbors
                              (sort-by
                               #(distance (first pt)
                                          (first %))
                               other-points)))})))
      {}
      indexed-points)))

(defn floyd-warshall-distance
  "Expected graph representation:
    {V -> neighboring-points}"
  [a-graph indexed-points]
  (let [indexed-points-dict (into {} indexed-points)
        edges    (reduce
                  (fn [acc [x neighbors]]
                    (concat acc (map (fn [n] [x n])
                                     neighbors)))
                  []
                  a-graph)

        inf-matrix (+ Double/POSITIVE_INFINITY
                      (zero-matrix (count indexed-points)
                                   (count indexed-points)))

        zero-diag  (reduce
                    (fn [acc i]
                      (mset acc i i 0))
                    inf-matrix
                    (-> indexed-points count range))

        weights-init (reduce
                      (fn [acc [x y]]
                        (mset acc
                              x
                              y
                              (distance
                               (indexed-points-dict x)
                               (indexed-points-dict y))))
                      zero-diag
                      edges)]

    (reduce
     (fn [old-distances [k i j]]
       (if (< (+ (mget old-distances i k)
                 (mget old-distances k j))
              (mget old-distances i j))
         (mset old-distances
               i
               j
               (+ (mget old-distances i k)
                  (mget old-distances k j)))
         old-distances))
     weights-init
     (for [k (-> indexed-points count range)
           i (-> indexed-points count range)
           j (-> indexed-points count range)]
       [k i j]))))

(defn isomap
  "Takes indexed-points and the target dimension"
  [indexed-points n]
  (let [graph (build-point-graph indexed-points)
        distances (floyd-warshall-distance graph indexed-points)]
    (mds/distances->points distances n)))
