(ns common.game.pass
  (:require
   [common.player :as p]
   [common.utils :refer [find-item first-error]]
   [clojure.spec.alpha :as s]
   [specs]))

(defn pass-check [player' game']
  (first-error
   [[#(p/equals? (:attacker game') player')
     "Attacker can't pass"]

    [#(empty? (:table game'))
     "Round not started"]]))

(s/fdef pass-check
  :args (s/cat :player' :specs/player
               :game' :specs/game)
  :ret (s/nilable string?))

(defn toggle-pass [player pass-list]
  (let [player-passed? (find-item #(p/equals? % player) pass-list)]
    (if player-passed?
      (remove #(p/equals? % player) pass-list)
      (conj pass-list player))))

(s/fdef toggle-pass
  :args (s/cat :player :specs/player
               :pass-list :specs/pass))

(defn pass-action [player game]
  (let [error (pass-check player game)]
    (if (nil? error)
      (-> game
          (assoc :pass (toggle-pass player (:pass game)))
          (assoc :error nil))
      (assoc game :error error))))

(s/fdef pass-action
  :args (s/cat :player :specs/player
               :game :specs/game)
  :ret :specs/game)

(defn pass [player game]
  (pass-action player game))

(s/fdef pass
  :args (s/cat :player :specs/player
               :game :specs/game)
  :ret :specs/game)