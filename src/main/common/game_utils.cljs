(ns common.game-utils
  (:require
   [common.player :as player]
   [common.table :as table]))

(defn defender? [player game]
  (= (:defender game) (:id player)))

(defn attacker? [player game]
  (= (:attacker game) (:id player)))

(defn player-can-move? [player game]
  (cond
    (not (empty? (:table game))) (attacker? player game)
    (defender? player game) false
    :else true))

(defn get-initial-trump-card [deck players]
  (let [last-card (last deck)
        last-player-card (last (:cards (last players)))]
    (if (not (nil? last-card))
      last-card
      last-player-card)))

(defn player-done? [player game]
  (and
   (empty? (:deck game))
   (empty? (:cards player))))

(defn player-lose? [player game]
  (and
   (= (player/players-with-cards-count (:players game)) 1)
   (not (player-done? player game))))

(defn can-take? [player game]
  (and
   (defender? player game)
   (not (empty? (:table game)))
   (not (table/all-beaten? (:table game)))))

(defn can-pass? [player game]
  (and
   (not (empty? (:table game)))
   (not (defender? player game))))

(defn passed? [player game]
  (or
   (empty? (:cards player))
   (some #(= (:id player) %) (:pass game))))

(defn all-passed? [game]
  (->>
   (:players game)
   (remove #(defender? % game))
   (every? #(passed? % game))))

(defn game-action [check-fn action-fn args]
  (let [check-result (apply check-fn args)]
     (if (nil? check-result)
       (apply action-fn args)
       check-result)))