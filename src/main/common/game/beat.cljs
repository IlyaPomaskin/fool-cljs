(ns common.game.beat
  (:require
   [common.player :as p]
   [common.utils :refer [first-error]]
   [clojure.spec.alpha :as s]
   [specs]
   [common.card :as card]))

(def beat-args
  (s/cat :player :specs/player
         :game :specs/game
         :to :specs/card
         :by :specs/card))

(defn beat-check [player game to by]
  (first-error
   [[#(not (p/equals? (:defender game) player))
     "Player is not defender"]

    [#(not (p/player-has-card? by player))
     "Player dont have card"]

    [#(not (card/valid-beat? (:suit (:trump game)) to by))
     "Invalid card by beat"]

    [#(not (some (fn [[to']] (card/equals? to' to)) (:table game)))
     "Invalid card to beat"]]))

(s/fdef beat-check
  :args beat-args
  :ret (s/nilable string?))

(defn beat-action [player game to by]
  (let [error (beat-check player game to by)
        player-index (p/get-player-index (:players game) player)]
    (if (nil? error)
      (-> game
          (assoc :error nil)
          (update-in [:table]
                     #(map
                       (fn [[to' by']]
                         (if (card/equals? to' to) [to' by] [to' by']))
                       %))
          (update-in [:players player-index] #(p/remove-card by %)))
      (assoc game :error error))))

(s/fdef beat-action
  :args beat-args
  :ret :specs/game)

(defn beat [player game to by]
  (beat-action player game to by))

(s/fdef beat
  :args beat-args
  :ret :specs/game)