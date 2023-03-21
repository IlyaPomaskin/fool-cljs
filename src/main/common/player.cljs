(ns common.player
  (:require
   [clojure.string :as string]
   [common.deck :as deck]
   [common.utils :as utils]))

(defn make [login]
  {:id login
   :session-id (string/join "-" ["session" (rand 10000)])
   :cards []})

(defn equals? [a b]
  (= (:id a) (:id b)))

(defn remove-card [card player]
  (update player :cards #(deck/remove-card card %)))

(defn mask-player [playerId player]
  (if (= playerId (:id player))
    player
    {:id (:id player)
     :session-id :hidden
     :cards (deck/mask-deck (:cards player))}))

(defn player-has-card? [card player]
  (some #(= card %) (:cards player)))

; list

(defn get-next-player-id [id players]
  (id))

(defn find-first-attacker-id [trump players] nil)

(defn deal-to-player [player deck]
  (let [amount (max 0 (- 6 (count (:cards player))))
        [cards next-deck] (deck/deal-cards amount deck)]
    [(assoc player :deck cards) next-deck]))

(defn deal-deck-to-players [players deck]
  (reduce
   (fn [[acc-players acc-deck] player]
     (let [[next-players next-deck] (deal-to-player player acc-deck)]
       [(concat acc-players next-players) next-deck]))
   [[] deck]
   players))

(defn player-exists? [player players]
  (utils/find-item
   #(equals? player %)
   players))

(defn players-with-cards-count [players]
  (count
   (filter
    (fn [player] (not (empty? (:cards player))))
    players)))

(defn get-by-id [list id] nil)
