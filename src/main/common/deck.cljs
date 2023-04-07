(ns common.deck
  (:require
   [common.card :as card]
   [common.consts :refer [ranks suits]]
   [clojure.spec.alpha :as s]
   [specs]))

(defn make []
  (shuffle
   (set
    (for [suit suits
          rank ranks]
      {:suit suit
       :rank rank}))))

(s/fdef make
  :ret :specs/deck)

(defn deal-cards [amount deck]
  (split-at amount deck))

(s/fdef deal-cards
  :args (s/cat :amount number? :deck :specs/deck)
  :ret (s/tuple :specs/deck :specs/deck))

(defn remove-card [card deck]
  (remove #(card/equals? card %) deck))

(s/fdef remove-card
  :args (s/cat :card :specs/card :deck :specs/deck)
  :ret :specs/deck)

(defn get-smallest-valuable-card [trump deck]
  (reduce
   (fn [prev next] (card/get-smallest trump prev next))
   nil
   deck))

(s/fdef get-smallest-valuable-card
  :args (s/cat :trump :specs/suit :deck :specs/deck)
  :ret :specs/card)

(defn mask-deck [deck]
  (map {:hidden true} deck))

(s/fdef mask-deck
  :args (s/cat :deck :specs/deck)
  :ret (s/* :specs/deck))
