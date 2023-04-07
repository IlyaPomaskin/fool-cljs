(ns common.game.pass
  (:require
   [common.game-utils :refer [game-action]]
   [common.player :as p]
   [common.utils :refer [find-item first-error]]))

(defn pass-check [player' game']
  (first-error
   [[#(p/equals? (:attacker game') player')
     "Attacker can't pass"]

    [#(empty? (:table game'))
     "Round not started"]]))

(defn pass-action [player game]
  (let [player-passed? (find-item #(p/equals? % player) (:pass game))]
    (update
     game :pass
     (if player-passed?
       (fn [pass-list] (remove #(p/equals? % player) pass-list))
       #(conj % player)))))

(defn pass [player game]
  (game-action pass-check pass-action [player game]))

(comment
  (pass
   {:id 111}
   {:pass [{:id 111}]})

  (pass
   {:id 222}
   {:pass [{:id 111}]}))