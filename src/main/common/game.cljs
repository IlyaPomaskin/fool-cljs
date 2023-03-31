(ns common.game
  (:require
   [common.deck :as deck]
   [common.game-utils :as game-utils]
   [common.player :as player]))

(defn make-in-lobby [owner]
  {:owner owner
   :players [owner]})

(defn make-in-progress [player-ids]
  (let [[players deck] (player/deal-deck-to-players
                        (map player/make player-ids) (deck/make))]
    {:pass []
     :table []
     :deck deck
     :trump (game-utils/get-initial-trump-card players deck)
     :attacker nil
     :defender nil
     :players (vec players)}))

(def games-in-lobby-by-id (atom {}))
(def games-in-progress-by-id (atom {}))

(comment
  (reset! games-in-lobby-by-id {})
  @games-in-lobby-by-id)

(defn create-lobby! [player]
  (let [game-id (rand-int 100)]
    (swap! games-in-lobby-by-id
           assoc game-id (make-in-lobby player))
    game-id))

(defn game-exists?! [gameId]
  (when (nil? (get @games-in-lobby-by-id gameId))
    (throw "Game not exists")))

(defn enter-lobby! [player gameId]
  (game-exists?! gameId)
  (swap! games-in-lobby-by-id
         update-in [gameId :players] conj player))

(defn player-in-game?! [player gameId]
  (when (nil? (get-in [gameId (:id player)] @games-in-lobby-by-id gameId))
    (throw "Player not in game")))

(defn leave-lobby! [player gameId]
  (game-exists?! gameId)
  (enter-lobby! player gameId))

(defn start [game] nil)
(defn move [card player game] nil)
(defn pass [player game] nil)
(defn beat [by to player game] nil)
(defn take' [player game] nil)

