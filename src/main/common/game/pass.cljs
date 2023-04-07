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

; (defn move-action [card player game]
;   (let [error (move-check card player game)
;         player-index (player/get-player-index (:players game) player)]
;     (if (nil? error)
;       (-> game
;           (update-in [:players player-index] #(player/remove-card card %))
;           (update :table #(conj % [card nil]))
;           (assoc :error nil))
;       (assoc game :error error))))

(defn pass-action [player game]
  (let [error (pass-check player game)
        player-passed? (find-item #(p/equals? % player) (:pass game))]
    (if (nil? error)
      (-> game
          (update
           :pass
           (if player-passed?
             (fn [pass-list] (remove #(p/equals? % player) pass-list))
             #(conj % player)))
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
  :ret (s/or :error string?
             :game :specs/game))

(comment
  (pass
   {:id 111}
   {:pass [{:id 111}]})

  (pass
   {:id 222}
   {:pass [{:id 111}]}))