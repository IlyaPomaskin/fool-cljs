(ns common.game.move
  (:require
   [common.player :as player]
   [common.table :as table]
   [common.utils :refer [first-error]]
   [clojure.spec.alpha :as s]
   [specs]))

(defn move-check [card' player' game']
  (first-error
   [[#(and (empty? (:table game'))
           (not (player/equals? (:attacker game') player')))
     "First move made not by attacked"]

    [#(player/equals? (:defender game') player')
     "Defender can't make move"]

    [#(table/max-cards? (:table game'))
     "Table is full"]

    [#(not (player/player-has-card? card' player'))
     "Player doesn't have this card"]

    [#(<= (count (:cards (:defender game')))
          (inc (count (table/get-unbeated-cards (:table game')))))
     "Defender don't have enought cards"]

    [#(and (not (table/correct-additional-card? card' (:table game')))
           (not (empty? (:table game'))))
     "Incorrect card"]]))

(s/fdef move-check
  :args (s/cat :card' :specs/card
               :player' :specs/player
               :game' :specs/game)
  :ret (s/nilable string?))

(comment
  (move-check
   {:suit :spades :rank :six}
   {:cards [] :id 333}
   {:table []
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 111}})

  (move-check
   {:suit :spades :rank :six}
   {:cards [] :id 111}
   {:table []
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 111}})

  (move-check
   {:suit :spades :rank :six}
   {:cards [] :id 111}
   {:table (repeat 6 [{:suit :spades :rank :six} nil])
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 222}})

  (move-check
   {:suit :spades :rank :six}
   {:cards [] :id 111}
   {:table []
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 222}})

  (move-check
   {:suit :hearts :rank :seven}
   {:cards [{:suit :hearts :rank :seven}] :id 111}
   {:table [[{:suit :spades :rank :six} nil]]
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 222}}))

(defn move-action [card player game]
  (let [error (move-check card player game)
        player-index (player/get-player-index (:players game) player)]
    (if (nil? error)
      (-> game
          (update-in [:players player-index] #(player/remove-card card %))
          (update :table #(conj % [card nil]))
          (assoc :error nil))
      (assoc game :error error))))

(s/fdef move-action
  :args (s/cat :card :specs/card
               :player :specs/player
               :game :specs/game)
  :ret :specs/game)

(defn move [card player game]
  (move-action card player game))

(s/fdef move
  :args (s/cat :card :specs/card
               :player :specs/player
               :game :specs/game)
  :ret :specs/game)

(comment
  (move
   {:suit :hearts :rank :king}
   {:cards [{:suit :hearts :rank :six}] :id 111}
   {:table [[{:suit :spades :rank :six} nil]]
    :players [{:cards [{:suit :hearts :rank :six}] :id 111}]
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 222}})

  (move
   {:suit :hearts :rank :six}
   {:cards [{:suit :hearts :rank :six}] :id 111}
   {:table [[{:suit :spades :rank :six} nil]]
    :players [{:cards [{:suit :hearts :rank :six}] :id 111}]
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 222}}))
