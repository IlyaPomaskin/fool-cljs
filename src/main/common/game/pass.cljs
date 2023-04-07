(ns common.game.pass
  (:require
   [common.game-utils :refer [game-action]]
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

(defn pass-action [player game]
  (let [player-passed? (find-item #(p/equals? % player) (:pass game))]
    (update
     game :pass
     (if player-passed?
       (fn [pass-list] (remove #(p/equals? % player) pass-list))
       #(conj % player)))))

(s/fdef pass-action
  :args (s/cat :player :specs/player
               :game :specs/game)
  :ret :specs/game)

(defn pass [player game]
  (game-action pass-check pass-action [player game]))

(s/fdef pass
  :args (s/cat :player :specs/player
               :game :specs/game)
  :ret (s/or :error string?
             :game :specs/game))

(comment
  (pass
   {:id 111}
   {:pass [{:id 111}]})

  (pass
   {:id 222}
   {:pass [{:id 111}]}))