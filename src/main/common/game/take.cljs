(ns common.game.take
  (:require
   [clojure.spec.alpha :as s]
   [common.game-utils :as gu]
   [common.player :as p]
   [common.table :as table]
   [common.utils :refer [first-error]]
   [specs]))

(defn take-check [player' game']
  (first-error
   [[#(not (gu/defender? player' game'))
     "Not defender can't take"]

    [#(empty? (:table game'))
     "Table is empty"]

    [#(table/all-beaten? (:table game'))
     "All cards are beated"]]))

(s/fdef take-check
  :args (s/cat :player' :specs/player
               :game' :specs/game)
  :ret (s/nilable string?))

(comment
  (take-check
   {:id 123}
   {:table []
    :defender 321})

  (take-check
   {:id 123}
   {:table []
    :defender 123})

  (take-check
   {:id 123}
   {:table [[{:suit :spades :rank :six} {:suit :spades :rank :seven}]]
    :defender 123})

  (take-check
   {:id 123}
   {:table [[{:suit :spades :rank :six}]]
    :defender 123}))

(defn take-action [player' game]
  (let [error (take-check player' game)
        player-index (p/get-player-index (:players game) player')]
    (if (nil? error)
      (-> game
          (update-in [:players player-index] #(p/add-cards (flatten (:table game)) %))
          (assoc :table [])
          (assoc :error nil))
      (assoc game :error error))))

(s/fdef take-action
  :args (s/cat :player :specs/player
               :game :specs/game)
  :ret :specs/game)

(comment
  (take-action
   {:id 123 :cards []}
   {:table [[{:suit :spades :rank :six} {:suit :spades :rank :seven}] [{:suit :spades :rank :eight}]]
    :defender 123
    :players [{:id 123 :cards [{:suit :spades :rank :nine}]}]}))

(defn take' [player game]
  (take-action player game))

(s/fdef take'˚
  :args (s/cat :player :specs/player
               :game :specs/game)
  :ret :specs/game)