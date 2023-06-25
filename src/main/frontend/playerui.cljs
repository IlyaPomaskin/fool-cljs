(ns frontend.playerui
  (:require
   [clojure.pprint :refer [pprint]]
   [common.card :as c]
   [frontend.cardui :as card-ui]
   [frontend.game-context :as game-context]
   [frontend.ui :as ui]
   [uix.core :refer [$ defui]]
   [uix.dom]))

(def player-state-context (uix.core/create-context []))
(defui player-state-provider [{:keys [children]}]
  (let [[player-state set-player-state!]
        (uix.core/use-state
         {:table-selected nil
          :player-selected nil})]
    (pprint player-state)
    ($ (.-Provider player-state-context) {:value [player-state set-player-state!]}
       children)))

(defui player-controls [{:keys [class player]}]
  (let [[_ dispatch!] (uix.core/use-context game-context/context)
        [player-state] (uix.core/use-context player-state-context)]
    ($ :.flex.gap-1 {:class class}
       ($ ui/button {:size :xs
                     :on-click #(dispatch! {:type :move
                                            :player player
                                            :card (:player-selected player-state)})}
          "Move")
       ($ ui/button {:size :xs
                     :on-click #(dispatch! {:type :pass
                                            :player player})}
          "Pass")
       ($ ui/button {:size :xs
                     :on-click #(dispatch! {:type :beat
                                            :player player
                                            :to (:table-selected player-state)
                                            :by (:player-selected player-state)})}
          "Beat")
       ($ ui/button {:size :xs
                     :on-click #(dispatch! {:type :take
                                            :player player})}
          "Take"))))

(defui player [{:keys [player attacker? defender?]}]
  (let [[player-state set-player-state!] (uix.core/use-context player-state-context)]
    ($ ui/panel
       ($ ui/title
          ($ :div.disp
             [(:id player)
              (when attacker? ($ :span.text-xs "\u00A0attack"))
              (when defender? ($ :span.text-xs "\u00A0defend"))]))

       ($ :.flex.flex-row.flex-wrap.gap-2
          (map
           (fn [card]
             ($ card-ui/visible
                {:key (card-ui/to-string card)
                 :card card
                 :on-click #(set-player-state!
                             (update
                              player-state
                              :player-selected
                              (fn [prev-card] (if (c/equals? prev-card card) nil card))))
                 :selected (c/equals? card (:player-selected player-state))}))
           (:cards player)))

       ($ player-controls
          {:class "pt-4"
           :player player}))))