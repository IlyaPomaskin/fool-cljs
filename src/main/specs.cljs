(ns specs
  (:require
   [clojure.spec.alpha :as s]
   [common.consts :as consts]))

(defn no-key [key] #(not (contains? % key)))

(s/def ::rank (set consts/ranks))
(s/def ::suit (set consts/suits))
(s/def ::hidden true?)

(s/def ::card
  (s/keys :req-un [::rank ::suit]
          :opt-un [::hidden]))

(s/def ::deck (s/* ::card))

(s/def ::table-pair (s/tuple ::card ::card))
(s/def ::table (s/* ::table-pair))

(s/def ::id string?)
(s/def ::session-id string?)
(s/def ::hand ::deck)
(s/def ::player
  (s/or
   ::player-visible (s/keys :req-un [::id ::session-id ::hand])

   ::player-masked (s/and
                    (s/keys :req-un [::id ::hand])
                    (no-key ::session-id))))

(s/def ::pass (s/* ::player))
(s/def ::trump ::suit)
(s/def ::attacker ::player)
(s/def ::defender ::player)
(s/def ::players (s/* ::player))
(s/def ::error string?)

(s/def ::game
  (s/keys :req-un [::pass ::table ::deck ::trump ::attacker ::defender ::players ::error]))
