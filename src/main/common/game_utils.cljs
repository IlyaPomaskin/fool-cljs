(ns common.game-utils
  (:require
   [common.player :as player]
   [common.table :as table]
   [clojure.spec.alpha :as s]
   [specs]))

(defn defender? [player' game]
  (player/equals? (:defender game) player'))

(s/fdef defender?
  :args (s/cat :player :specs/player :game :specs/game)
  :ret boolean?)

(defn attacker? [player game]
  (= (:attacker game) (:id player)))

(s/fdef attacker?
  :args (s/cat :player :specs/player :game :specs/game)
  :ret boolean?)

(defn player-can-move? [player game]
  (cond
    (not (empty? (:table game))) (attacker? player game)
    (defender? player game) false
    :else true))

(s/fdef player-can-move?
  :args (s/cat :player :specs/player :game :specs/game)
  :ret boolean?)

(defn get-initial-trump-card [deck players]
  (let [last-card (last deck)
        last-player-card (last (:cards (last players)))]
    (if (not (nil? last-card))
      last-card
      last-player-card)))

(s/fdef get-initial-trump-card
  :args (s/cat :deck :specs/deck :players :specs/players)
  :ret :specs/card)

(defn player-done? [player game]
  (and
   (empty? (:deck game))
   (empty? (:cards player))))

(s/fdef player-done?
  :args (s/cat :player :specs/player :game :specs/game)
  :ret boolean?)

(defn player-lose? [player game]
  (and
   (= (player/players-with-cards-count (:players game)) 1)
   (not (player-done? player game))))

(s/fdef player-lose?
  :args (s/cat :player :specs/player :game :specs/game)
  :ret boolean?)

(defn can-take? [player game]
  (and
   (defender? player game)
   (not (empty? (:table game)))
   (not (table/all-beaten? (:table game)))))

(s/fdef can-take?
  :args (s/cat :player :specs/player :game :specs/game)
  :ret boolean?)

(defn can-pass? [player game]
  (and
   (not (empty? (:table game)))
   (not (defender? player game))))

(s/fdef can-pass?
  :args (s/cat :player :specs/player :game :specs/game)
  :ret boolean?)

(defn passed? [player game]
  (or
   (empty? (:cards player))
   (some #(= (:id player) %) (:pass game))))

(s/fdef passed?
  :args (s/cat :player :specs/player :game :specs/game)
  :ret boolean?)

(defn all-passed? [game]
  (->>
   (:players game)
   (remove #(defender? % game))
   (every? #(passed? % game))))

(s/fdef all-passed?
  :args (s/cat :game :specs/game)
  :ret boolean?)

(defn game-action [check-fn action-fn args]
  (let [check-result (apply check-fn args)]
    (if (nil? check-result)
      (apply action-fn args)
      check-result)))