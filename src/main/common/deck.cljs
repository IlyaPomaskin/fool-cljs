(ns common.deck
  (:require
   [common.card :as card]
   [common.consts :refer [ranks suits]]
   [clojure.spec.alpha :as s]))

(defn make []
  (shuffle
   (set
    (for [suit suits
          rank ranks]
      {:suit suit
       :rank rank}))))

(s/fdef make
  :ret :deck/deck)

(defn deal-cards [amount deck]
  (split-at amount deck))

(s/fdef deal-cards
  :args (s/cat :amount number? :deck :deck/deck)
  :ret (s/tuple :deck/deck :deck/deck))

(defn remove-card [card deck]
  (remove #(card/equals? card %) deck))

(s/fdef remove-card
  :args (s/cat :card :card/card :deck :deck/deck)
  :ret :deck/deck)

(defn get-smallest-valuable-card [trump deck]
  (reduce
   (fn [prev next] (card/get-smallest trump prev next))
   nil
   deck))

(s/fdef get-smallest-valuable-card
  :args (s/cat :trump :card/suit :deck :deck/deck)
  :ret :card/card)

(defn mask-deck [deck]
  (map {:hidden true} deck))

(s/fdef mask-deck
  :args (s/cat :deck :deck/deck)
  :ret (s/* :deck/deck))
