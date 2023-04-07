(ns common.specs
  (:require
   [clojure.spec.alpha :as s]
   [common.consts :as consts]))

(defn no-key [key] #(not (contains? % key)))

(s/def :card/rank (set consts/ranks))
(s/def :card/suit (set consts/suits))
(s/def :card/hidden true?)

(s/def :card/visible
  (s/and
   (s/keys :req-un [:card/rank :card/suit]
           :opt-un [:card/hidden])
   (no-key :card/hidden)))

(s/def :card/hidden
  (s/and
   (s/keys :req-un [:card/hidden])
   (no-key :rank)
   (no-key :suit)))

(s/def :card/card
  ; (s/or
  ;  :card/visible :card/visible
  ;  :card/hidden :card/hidden)
  (s/keys :req-un [:card/rank :card/suit]
          :opt-un [:card/hidden]))

(s/def :deck/deck (s/* :card/card))

(s/def :table/pair (s/tuple :card/card :card/card))
(s/def :table/table (s/* :table/pair))

(s/def :player/id string?)
(s/def :player/session-id string?)
(s/def :player/hand :deck/deck)
(s/def :player/player
  (s/or
   :player/visible (s/keys :req [:player/id :player/session-id :player/hand])

   :player/masked (s/and
                   (s/keys :req [:player/id :player/hand])
                   (no-key :player/session-id))))

(s/def :game/pass (s/* :player/player))
(s/def :game/table :table/table)
(s/def :game/deck :deck/deck)
(s/def :game/trump :card/suit)
(s/def :game/attacker :player/player)
(s/def :game/defender :player/player)
(s/def :game/players (s/* :player/player))

(s/def :game/game
  (s/keys :req [:game/pass :game/table :game/deck :game/trump :game/attacker :game/defender :game/players]))

