(ns frontend.main
  (:require
   [clojure.pprint :refer [pprint]]
   [common.card :as c]
   [common.player :as p]
   [frontend.cardui :as card-ui]
   [frontend.ui :as ui]
   [uix.core :refer [$ defui]]
   [uix.dom]
   [frontend.game-context :as game-context]))

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
       ($ ui/button {:size :xs} "Pass")
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
       ($ ui/title (:id player))

       (when attacker?
         ($ :div "attack"))

       (when defender?
         ($ :div "defend"))

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

(defui table [{:keys [class table]}]
  (let [[player-state set-player-state!] (uix.core/use-context player-state-context)]

    ($ ui/panel
       {:class [class]}
       ($ ui/title "Table")

       ($ :.flex.gap-2
          (when (empty? table)
            ($ :.flex.flex-col.gap-2
               ($ card-ui/slot)))

          (map
           (fn [[to by]]
             ($ :.flex.flex-col.gap-2 {:key (card-ui/to-string to)}
                ($ card-ui/visible
                   {:card to
                    :selected (c/equals? to (:table-selected player-state))
                    :on-click #(set-player-state! {:table-selected to})})
                (if (nil? by)
                  ($ card-ui/slot)
                  ($ card-ui/visible {:card by}))))
           table)))))

(defui stack [{:keys [deck trump]}]
  (let [length (dec (count deck))
        has-cards? (> length 0)
        last-card (last deck)]
    ($ :.relative.w-24
       (if (or has-cards? last-card)
         ($ card-ui/slot {:class "relative z-10"})
         ($ card-ui/trump {:trump trump}))

       (when has-cards?
         ($ card-ui/stack
            {:class "absolute top-0 z-10"
             :amount length}))

       (when last-card
         ($ card-ui/visible
            {:class "absolute top-0 rotate-90 left-8"
             :card last-card})))))

(defui game [{:keys [game]}]
  ($ :.flex.flex-col.gap-8
     ($ :.flex.gap-8
        ($ ui/panel
           ($ ui/title "Deck")
           ($ stack game))

        ($ table
           {:class "flex-1"
            :table (:table game)}))

     (when (:error game)
       ($ :.text-md.pb-2.select-none (:error game)))

     (map
      (fn [item]
        ($ player
           {:key (:id item)
            :player item
            :attacker? (p/equals? (:attacker game) item)
            :defender? (p/equals? (:defender game) item)}))
      (:players game))))

(defui app []
  (let [[game-state] (uix.core/use-context game-context/context)]
    ($ :.bg-slate-50.p-8.h-full.w-full
       ($ :.container
          ($ game {:game game-state})))))

(defonce root
  (uix.dom/create-root (js/document.getElementById "root")))

(defn init []
  (uix.dom/render-root
   ($ game-context/provider
      {:player-ids ["qwe" "asd" "zxc"]}
      ($ player-state-provider
         ($ app)))
   root))









