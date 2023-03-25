(ns common.card
  (:require
   [common.consts :refer [ranks]]
   [common.utils :refer [find-item-index]]))

(defn hidden? [card]
  (= (:hidden card) true))

(defn equals-by-suit? [a b]
  (and
   (not (hidden? a))
   (not (hidden? b))
   (= (:suit a) (:suit b))))

(defn equals-by-rank? [a b]
  (and
   (not (hidden? a))
   (not (hidden? b))
   (= (:rank a) (:rank b))))

(defn equals? [a b]
  (and
   (equals-by-rank? a b)
   (equals-by-suit? a b)))

(defn get-rank-index [card]
  (find-item-index
   (fn [rank] (= rank (:rank card)))
   ranks))

(defn lt-by-rank [a b]
  (< (get-rank-index a) (get-rank-index b)))


(defn gt-by-rank [a b]
  (not (lt-by-rank a b)))

(defn sort-by-rank [a b]
  (if (lt-by-rank a b)
    -1
    1))

(defn trump? [trump card]
  (= (:suit card) trump))

(defn beat-by-trump? [trump to by]
  (and
   (not (trump? trump to))
   (trump? trump by)))

(defn get-smallest [trump a b]
  (case [a b]
    [nil nil] nil
    [nil b] b
    [a nil] a
    (let [a-trump (trump? trump a)
          b-trump (trump? trump b)]
      (case [a-trump b-trump]
        [true false] a
        [false true] b
        (if (lt-by-rank a b) a b)))))

(defn valid-beat? [trump to by]
  (let [to-trump (trump? trump to)
        by-trump (trump? trump by)]
    (case [to-trump by-trump]
      [false true] true
      [true false] false
      [true true] (lt-by-rank to by)
      [false false] (and
                     (equals-by-suit? to by)
                     (lt-by-rank to by)))))
