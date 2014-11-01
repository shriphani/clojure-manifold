(ns clojure-manifold.data
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.core.matrix :refer :all]
            [clojure.string :as string]))

(set-current-implementation :vectorz)

(defn load-data
  [csv-file]
  (let [data (with-open [in-file (io/reader csv-file)]
               (doall
                (csv/read-csv in-file)))]
    (matrix
     (map
      (fn [row]
        (map #(Double/parseDouble %) row))
      data))))

(defn save-csv
  [a-matrix location]
  (with-open [wrtr (io/writer location)]
    (doall
     (binding [*out* wrtr]
       (doseq [row a-matrix]
         (println (string/join ", " row)))))))
