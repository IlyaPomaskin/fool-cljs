(ns frontend.game-context
  (:require
   [common.game :as g]
   [common.game.move :as gm]
   [common.game.pass :as gp]
   [common.game.beat :as gb]
   [common.game.take :as gt]
   [clojure.pprint :refer [pprint]]
   [uix.core :refer [$ defui]]
   [uix.dom]
   [clojure.spec.alpha :as s]
   [specs]))

(defn action-move [card player]
  {:type :move
   :card card
   :player player})

(s/fdef action-move
  :args (s/cat :type :move
               :card :specs/card
               :player :specs/player))

(defn action-take [player]
  {:type :take
   :player player})

(s/fdef action-take
  :args (s/cat :type :take
               :player :specs/player))

(defn action-pass [player]
  {:type :pass
   :player player})

(s/fdef action-pass
  :args (s/cat :type :pass
               :player :specs/player))

(defn action-beat [to by player]
  {:type :beat
   :to to
   :by by
   :player player})

(s/fdef action-beat
  :args (s/cat :type :beat
               :player :specs/player
               :to :specs/card
               :by :specs/card))

(defn game-reducer [game {type :type
                          player :player
                          :as action}]
  (pprint action)
  (case type
    :move (gm/move (:card action) player game)

    :take (gt/take' player game)

    :pass (gp/pass player game)

    :beat (let [{by :by to :to} action]
            (gb/beat player game to by))))

(s/fdef game-reducer
  :args (s/cat :game :specs/game :action :spe))

(defn use-game-reducer [initial-value]
  (let [[value dispatch!] (uix.core/use-reducer game-reducer initial-value)]
    [value dispatch!]))

(def context (uix.core/create-context []))

(defui provider [{:keys [children player-ids]}]
  (let [[game dispatch!] (use-game-reducer (g/make-in-progress player-ids))]
    (pprint game)
    ($ (.-Provider context) {:value [game dispatch!]}
       children)))
