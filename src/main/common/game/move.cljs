(ns common.game.move
  (:require
   [common.player :as player]
   [common.table :as table]))

(defn check-move [card' player' game']
  (reduce
   (fn [acc-error [fn error]]
     (if (nil? acc-error)
       (when (apply fn nil) error)
       acc-error))

   nil

   [[#(and (empty? (:table game'))
           (not (player/equals? (:attacker game') player')))
     "First move made not by attacked"]

    [#(player/equals? (:defender game') player')
     "Defender can't make move"]

    [#(table/max-cards? (:table game'))
     "Table is full"]

    [#(not (player/player-has-card? card' player'))
     "Player doesn't have this card"]

    [#(>= (count (:cards (:defender game')))
          (inc (count (table/get-unbeated-cards (:table game')))))
     "Defender don't have enought cards"]

    [#(and (not (table/correct-additional-card? card' (:table game')))
           (not (empty? (:table game'))))
     "Incorrect card"]]))

(comment
  (check-move
   {:suit :spades :rank :six}
   {:cards [] :id 333}
   {:table []
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 111}})

  (check-move
   {:suit :spades :rank :six}
   {:cards [] :id 111}
   {:table []
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 111}})

  (check-move
   {:suit :spades :rank :six}
   {:cards [] :id 111}
   {:table (repeat 6 [{:suit :spades :rank :six} nil])
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 222}})

  (check-move
   {:suit :spades :rank :six}
   {:cards [] :id 111}
   {:table []
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 222}})

  (check-move
   {:suit :hearts :rank :seven}
   {:cards [{:suit :hearts :rank :seven}] :id 111}
   {:table [[{:suit :spades :rank :six} nil]]
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 222}}))

(defn move [card player game]
  (let [check-result (check-move card player game)
        player-index (player/get-player-index (:players game) player)]
    (if (nil? check-result)
      (-> game
          (update-in [:players player-index] #(player/remove-card card %))
          (update :table #(conj % [card nil])))
      check-result)))

(comment
  (move
   {:suit :hearts :rank :six}
   {:cards [{:suit :hearts :rank :six}] :id 111}
   {:table [[{:suit :spades :rank :six} nil]]
    :players [{:cards [{:suit :hearts :rank :six}] :id 111}]
    :attacker {:cards [] :id 111}
    :defender {:cards [] :id 222}}))
