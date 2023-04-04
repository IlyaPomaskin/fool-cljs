(ns common.table
  (:require
   [common.card :as c]))

(defn beaten? [[a b]]
  (and
   (not (nil? a))
   (not (nil? b))))

(defn all-beaten? [table]
  (and
   (not (empty? table))
   (every?
    (fn [pair]
      (println beaten? pair)
      (beaten? pair))
    table)))

(defn max-cards? [table]
  (= (count table) 6))

(defn get-unbeated-cards [table]
  (filter
   (fn [[_to by]] (nil? by))
   table))

(defn correct-additional-card? [card table]
  (some #(c/equals-by-rank? card %) (flatten table)))
