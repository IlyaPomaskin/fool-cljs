(ns common.card
  (:require
   [common.consts :refer [ranks]]
   [common.utils :refer [find-item-index]]
   [clojure.spec.alpha :as s]
   [specs]))

(defn hidden? [card]
  (true? (:card/hidden card)))

(s/fdef hidden?
  :args (s/cat :card :specs/card)
  :ret boolean?)

(defn equals-by-suit? [a b]
  (and
   (not (hidden? a))
   (not (hidden? b))
   (= (:suit a) (:suit b))))

(s/fdef equals-by-suit?
  :args (s/cat :a :specs/card :b :specs/card)
  :ret boolean?)

(defn equals-by-rank? [a b]
  (and
   (not (hidden? a))
   (not (hidden? b))
   (= (:rank a) (:rank b))))

(s/fdef equals-by-rank?
  :args (s/cat :a :specs/card :b :specs/card)
  :ret boolean?)

(defn equals? [a b]
  (and
   (not (nil? a))
   (not (nil? b))
   (equals-by-rank? a b)
   (equals-by-suit? a b)))

(s/fdef equals?
  :args (s/cat :a :specs/card :b :specs/card)
  :ret boolean?)

(defn get-rank-index [card]
  (find-item-index
   (fn [rank] (= rank (:rank card)))
   ranks))

(s/fdef get-rank-index
  :args (s/cat :card :specs/card)
  :ret int?)

(defn lt-by-rank [a b]
  (< (get-rank-index a) (get-rank-index b)))

(s/fdef lt-by-rank
  :args (s/cat :a :specs/card :b :specs/card)
  :ret boolean?)

(defn gt-by-rank [a b]
  (not (lt-by-rank a b)))

(s/fdef gt-by-rank
  :args (s/cat :a :specs/card :b :specs/card)
  :ret boolean?)

(defn sort-by-rank [a b]
  (if (lt-by-rank a b)
    -1
    1))

(s/fdef sort-by-rank
  :args (s/cat :a :specs/card :b :specs/card)
  :ret int?)

(defn trump? [trump card]
  (= (:suit card) trump))

(s/fdef trump?
  :args (s/cat :trump :specs/suit :b :specs/card)
  :ret boolean?)

(defn beat-by-trump? [trump to by]
  (and
   (not (trump? trump to))
   (trump? trump by)))

(s/fdef beat-by-trump?
  :args (s/cat :trump :specs/suit :to :specs/card :by :specs/card)
  :ret boolean?)

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

(s/fdef get-smallest
  :args (s/cat :trump :specs/suit
               :a :specs/card
               :b :specs/card)
  :ret :specs/card)

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

(s/fdef valid-beat?
  :args (s/cat :trump :specs/suit :to :specs/card :by :specs/card)
  :ret boolean?)
