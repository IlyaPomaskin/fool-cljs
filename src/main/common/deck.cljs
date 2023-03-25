(ns common.deck
  (:require
   [common.card :as card]
   [common.consts :refer [ranks suits]]))

(defn make []
  (shuffle
   (set
    (for [suit suits
          rank ranks]
      {:suit suit
       :rank rank}))))

(defn deal-cards [amount deck]
  (split-at amount deck))

(defn remove-card [card deck]
  (remove #(card/equals? card %) deck))

(defn get-smallest-valuable-card [trump deck]
  (reduce
   (fn [prev next] (card/get-smallest trump prev next))
   nil
   deck))

(defn mask-deck [deck]
  (map {:hidden true} deck))
