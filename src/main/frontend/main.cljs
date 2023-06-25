(ns frontend.main
  (:require
   [common.card :as c]
   [common.player :as p]
   [frontend.cardui :as card-ui]
   [frontend.game-context :as game-context]
   [frontend.playerui :as player-ui]
   [frontend.stackui :as stack-ui]
   [frontend.ui :as ui]
   [uix.core :refer [$ defui]]
   [uix.dom]))

(defui table [{:keys [class table]}]
  (let [[player-state set-player-state!] (uix.core/use-context player-ui/player-state-context)]

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

(defui game [{:keys [game]}]
  ($ :.flex.flex-col.gap-8
     ($ :.flex.gap-8
        ($ ui/panel
           ($ ui/title "Deck")
           ($ stack-ui/stack game))

        ($ table
           {:class "flex-1"
            :table (:table game)}))

     (when (:error game)
       ($ :.text-md.pb-2.select-none (:error game)))

     (map
      (fn [item]
        ($ player-ui/player
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
      ($ player-ui/player-state-provider
         ($ app)))
   root))









