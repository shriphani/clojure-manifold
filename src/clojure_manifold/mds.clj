(ns clojure-manifold.mds
  "Implementation of MDS"
  (:refer-clojure :exclude [* - + == /])
  (:require [clojure.core.matrix :refer :all]
            [clojure.core.matrix.operators :refer :all]
            [clojure.core.matrix.linear :refer :all]))

(set-current-implementation :vectorz)

(defn double-center
  "Double centering:
   0.5 * (matrix - row-mean - col-mean + grand-mean)"
  [a-matrix]
  (let [row-sum (apply + a-matrix)
        col-sum (->> a-matrix transpose (apply +))

        [num-rows num-cols]  (shape a-matrix)
        
        row-mean (->> (/ row-sum num-rows)
                      (repeat num-rows)
                      matrix)
        col-mean (->> (/ col-sum num-cols)
                      (repeat num-cols)
                      matrix
                      transpose)

        grand-mean (+ (/ (reduce + row-sum)
                         (* num-rows num-cols))
                      (new-matrix num-rows num-cols))]

    (* -0.5
       (-> a-matrix
           (- row-mean)
           (- col-mean)
           (+ grand-mean)))))

(defn proximity-matrix
  "Construct a proximity matrix (of euclidean distances)"
  [a-matrix]
  (map
   (fn [x]
     (map
      (fn [y]
        (Math/pow (distance x y)
                  2))
      a-matrix))
   a-matrix))

(defn distances->points
  "Takes a distance matrix and retrieves the
   points in the embedding that preserves these
   details"
  [D n]
  (let [centered-D (double-center D)
        {U :U
         S :S} (svd centered-D)]
    (* (submatrix U 1 [0 n])
       (submatrix S 0 n 0 n))))

(defn mds
  "Take a matrix and find a configuration in n dimensions
   that preserves directions"
  [a-matrix n]
  (let [D (proximity-matrix a-matrix)]
    (distances->points D n)))
